package com.creitu.minhasFinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.creitu.minhasFinancas.model.entity.Usuario;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
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
