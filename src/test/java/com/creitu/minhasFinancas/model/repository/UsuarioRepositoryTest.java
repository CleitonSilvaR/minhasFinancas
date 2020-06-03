package com.creitu.minhasFinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.creitu.minhasFinancas.model.entity.Usuario;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository usuarioRepository;

	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
		usuarioRepository.save(usuario);
		
		boolean result = usuarioRepository.existsByEmail(usuario.getEmail());
		
		Assertions.assertThat(result).isTrue();
		
	}
}
