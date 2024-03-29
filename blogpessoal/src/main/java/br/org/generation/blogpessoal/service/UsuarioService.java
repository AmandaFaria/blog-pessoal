package br.org.generation.blogpessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.org.generation.blogpessoal.model.Usuario;
import br.org.generation.blogpessoal.model.UsuarioLogin;
import br.org.generation.blogpessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	//verificando se existe um usuario cadastrado
	public Optional<Usuario> cadastrarUsuario(Usuario usuario){
		if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) 
			return Optional.empty();  //se ele nao existe retorna o vazio
		
		usuario.setSenha(criptografarSenha(usuario.getSenha())); //pega a senha do usuario, criptografa e grava de novo no usuario
		
		return Optional.of(usuarioRepository.save(usuario));  //salvei no meu usuario a senha
	}
	
	//atualizar o usuario, verificando se ele existe pelo id
	public Optional<Usuario> atualizarUsuario(Usuario usuario){
		if (usuarioRepository.findById(usuario.getId()).isPresent()) {
			Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());
			
			if(buscaUsuario.isPresent()) {
				if(buscaUsuario.get().getId() != usuario.getId()) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Usuário já existente!", null);
				}
			}
			usuario.setSenha(criptografarSenha(usuario.getSenha())); //pega a senha do usuario, criptografa e grava de novo no usuario
			return Optional.of(usuarioRepository.save(usuario));  //salvei no meu usuario a senha
		}
		return Optional.empty();
	}
	
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {

		Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());

		if (usuario.isPresent()) {
			if (compararSenha(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {

				String token = gerarBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha());

				usuarioLogin.get().setId(usuario.get().getId());				
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setToken(gerarBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));
				usuarioLogin.get().setSenha(usuario.get().getSenha());				

				return usuarioLogin;

			}
		}	
		
		return Optional.empty();
		
	}
	
	private String gerarBasicToken(String usuario, String senha) {

		String token = usuario + ":" + senha;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(tokenBase64);

	}
	
	//criptografando a senha
	private String criptografarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(senha);
	}

	private Boolean compararSenha(String senhaDigitada, String senhaBanco) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
		return encoder.matches(senhaDigitada, senhaBanco);
	}
}
