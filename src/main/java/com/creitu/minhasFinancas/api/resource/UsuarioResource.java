package com.creitu.minhasFinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creitu.minhasFinancas.api.dto.UsuarioDTO;
import com.creitu.minhasFinancas.exception.ErroAutenticacaoException;
import com.creitu.minhasFinancas.exception.RegraNegocioException;
import com.creitu.minhasFinancas.model.entity.Usuario;
import com.creitu.minhasFinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {
	
	private UsuarioService usuarioService;
	
	public UsuarioResource( UsuarioService usuarioService ) {
		this.usuarioService = usuarioService;
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO usuarioDTO ) {
		try {
			
			Usuario usuarioAutenticado = usuarioService.autenticar(usuarioDTO.getEmail(), usuarioDTO.getSenha());
			
			return new ResponseEntity(usuarioAutenticado, HttpStatus.OK);
			
		} catch (ErroAutenticacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PostMapping("/salvar")
	public ResponseEntity salvar(@RequestBody UsuarioDTO usuarioDTO ) {
		try {
			Usuario usuario = Usuario.builder()
					.nome(usuarioDTO.getNome())
					.email(usuarioDTO.getEmail())
					.senha(usuarioDTO.getSenha())
					.build();
			
			Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
			
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
			
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}

}
