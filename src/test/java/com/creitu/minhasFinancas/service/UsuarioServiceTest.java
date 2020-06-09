package com.creitu.minhasFinancas.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.creitu.minhasFinancas.exception.RegraNegocioException;
import com.creitu.minhasFinancas.model.repository.UsuarioRepository;
import com.creitu.minhasFinancas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	UsuarioService usuarioService;
	UsuarioRepository usuarioRepository;
	
	@BeforeEach
	public void setUp() {
		usuarioRepository = Mockito.mock(UsuarioRepository.class);
		usuarioService = new UsuarioServiceImpl(usuarioRepository);
	}

	@Test
	public void deveValidarEmail() {
		//cenario
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);

		//acao
		assertDoesNotThrow(()-> usuarioService.validarEmail("email@email.com"));
	}
	
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//cenario
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

		//acao
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			usuarioService.validarEmail("email@email.com");
		});
	}
}
