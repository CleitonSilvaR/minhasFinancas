package com.creitu.minhasFinancas.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.creitu.minhasFinancas.exception.ErroAutenticacaoException;
import com.creitu.minhasFinancas.exception.RegraNegocioException;
import com.creitu.minhasFinancas.model.entity.Usuario;
import com.creitu.minhasFinancas.model.repository.UsuarioRepository;
import com.creitu.minhasFinancas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	UsuarioService usuarioService;
	
	@MockBean
	UsuarioRepository usuarioRepository;
	
	@BeforeEach
	public void setUp() {
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
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		//cenario
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when( usuarioRepository.findByEmail(email) ).thenReturn(Optional.of(usuario));

		assertDoesNotThrow(()-> {
			//acao
			Usuario result = usuarioService.autenticar(email, senha);
			
			//verificacao
			Assertions.assertNotNull(result);
		});
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastroComOEmailInformado() {
		//cenario
		Mockito.when( usuarioRepository.findByEmail(Mockito.anyString()) ).thenReturn(Optional.empty());
		
//		Assertions.assertThrows(ErroAutenticacaoException.class, () -> {
//			//acao
//			usuarioService.autenticar("email@email.com", "senha");
//		});
		
		Throwable throwable = catchThrowable( () -> usuarioService.autenticar("email@email.com", "senha") );
		assertThat(throwable).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Usuário não encontrado para o email informado.");
	}
	
	@Test
	public void deveLancarErroQuandoQuandoSenhaForInvalida() {
		//cenario
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).build();
		Mockito.when( usuarioRepository.findByEmail(Mockito.anyString()) ).thenReturn(Optional.of(usuario));
		
//		Assertions.assertThrows(ErroAutenticacaoException.class, () -> {
//			//acao
//			usuarioService.autenticar(email, "123456");
//		});
		
		Throwable throwable = catchThrowable( () -> usuarioService.autenticar(email, "123456") );
		assertThat(throwable).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Senha inválida.");
		
	}
}
