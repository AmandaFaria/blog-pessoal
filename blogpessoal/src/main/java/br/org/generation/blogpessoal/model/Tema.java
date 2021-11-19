package br.org.generation.blogpessoal.model;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tb_temas")
public class Tema {

	@Id			//falando que isso é uma chave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY)		//auto_increment
	private long id;
	
	@NotNull(message = "O campo tema é obrigatório!")	//obrigatoriamente a pessoa vai ter que digitar um tema
	@Size(min = 5, max = 100, message = "Min de caractéres 5 e máx 100")	//definindo o tamanho de de caracteres
	private String descricao;
	
	@OneToMany(mappedBy = "tema", cascade = CascadeType.ALL) //vai dizer se, por exemplo, um tema for apagado o CASCADETYPE.ALL nos garante que todas as postagens referentes ao tema serão apagadas
	@JsonIgnoreProperties("tema")
	private List<Postagem> postagem;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}
	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}
}
