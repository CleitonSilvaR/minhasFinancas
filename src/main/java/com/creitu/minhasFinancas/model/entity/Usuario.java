package com.creitu.minhasFinancas.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table( name = "usuario", schema = "public")
@EqualsAndHashCode( of = {"id"})
@NoArgsConstructor
@ToString
public class Usuario {
	
	@Id
	@SequenceGenerator(name = "usuarioSeq", sequenceName = "usuario_id_multi_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "usuarioSeq")
	@Column(unique = true, nullable = false)
	@Getter private Long id;
	
	@Column(name = "nome", length = 150)
	@Getter @Setter private String nome;
	
	@Column(name = "email", length = 150)
	@Getter @Setter private String email;
	
	@Column(name = "senha", length = 30)
	@Getter @Setter private String senha;
}
