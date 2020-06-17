package com.creitu.minhasFinancas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
	
	@SpyBean
	UsuarioServiceImpl usuarioService;
	
	@MockBean
	UsuarioRepository usuarioRepository;
	
//	@BeforeEach
//	public void setUp() {
//		usuarioService = Mockito.spy(new UsuarioServiceImpl(usuarioRepository));
//	}
	

	@Test
	public void deveSalvarUmUsuario() {
		String nome = "nome";
		String email = "email@email.com";
		String senha = "senha";
		Long id = 1l;
		
		//cenario
		Mockito.doNothing().when(usuarioService).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().nome(nome).email(email).senha(senha).id(id).build();
		
		Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

		//acao
		assertDoesNotThrow(()-> {
			Usuario usuarioSalvo = usuarioService.salvarUsuario(new Usuario());
			assertThat(usuarioSalvo).isNotNull();
			assertThat(usuarioSalvo.getId()).isEqualTo(id);
			assertThat(usuarioSalvo.getNome()).isEqualTo(nome);
			assertThat(usuarioSalvo.getEmail()).isEqualTo(email);
			assertThat(usuarioSalvo.getSenha()).isEqualTo(senha);
		});
	}
	

	@Test
	public void naoDeveSalvarUsuarioComEmailJaCadastrado() {
		String email = "email@email.com";
		
		//cenario
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(usuarioService).validarEmail(email);
		
		//acao
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			usuarioService.salvarUsuario(usuario);
			
			//verificacao
			Mockito.verify( usuarioRepository, Mockito.never()).save(usuario);
		});
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
