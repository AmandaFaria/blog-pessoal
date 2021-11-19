package br.org.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.generation.blogpessoal.model.Tema;
import br.org.generation.blogpessoal.repository.TemaRepository;

@RestController
@RequestMapping("/temas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TemaController {


	@Autowired
	private TemaRepository temaRepository; 
	
	@GetMapping
	public ResponseEntity<List<Tema>> getAll(){
		return ResponseEntity.ok(temaRepository.findAll());		
		// select * from tb_postagens;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Tema> getById(@PathVariable long id){
		return temaRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))	//if tiver uma resposta, mostrar a resposta
				.orElse(ResponseEntity.notFound().build());		//else, mostrar um not Found
				// select * from tb_temas where id = 1;
	}
	
	@GetMapping("/descricao/{descricao}")
	public ResponseEntity<List<Tema>> getByDescricao(@PathVariable String descricao){
		return ResponseEntity.ok(temaRepository.findAllByDescricaoContainingIgnoreCase(descricao));		
		// select * from tb_temas;
	}
	
	//salvar os dados
	@PostMapping
	public ResponseEntity<Tema> postTema(@RequestBody Tema tema){
		return ResponseEntity.status(HttpStatus.CREATED).body(temaRepository.save(tema)); //devolve um 201 = created		
	}
	
	//realiza a atualização da postagem
	@PutMapping
	 public ResponseEntity<Tema> putTema(@RequestBody Tema tema){
        return temaRepository.findById(tema.getId())
                .map(resposta -> {
                    Tema updateTema = temaRepository.save(tema);
                    return ResponseEntity.ok().body(updateTema);
                })
                .orElse(ResponseEntity.notFound().build());
    }
	
	//deletando pelo id
	@DeleteMapping("/{id}")
	 public ResponseEntity<?> deletePostagem(@PathVariable long id) {
        return temaRepository.findById(id)
        .map(resposta -> {
            temaRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        })
        .orElse(ResponseEntity.notFound().build());

    }
	/*public void deletePostagem(@PathVariable long id) {
		postagemRepository.deleteById(id);
	}*/
}

