package com.creitu.minhasFinancas.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.creitu.minhasFinancas.exception.RegraNegocioException;
import com.creitu.minhasFinancas.model.entity.Usuario;
import com.creitu.minhasFinancas.model.repository.UsuarioRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	UsuarioRepository usuarioRepository;

	@Test
	public void deveValidarEmail() {
		//cenario
		usuarioRepository.deleteAll();

		//acao
		assertDoesNotThrow(()-> usuarioService.validarEmail("email@email.com"));
	}
	
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//cenario
		Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
		usuarioRepository.save(usuario);

		//acao
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			usuarioService.validarEmail("email@email.com");
		});
	}
}
