package com.creitu.minhasFinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creitu.minhasFinancas.api.dto.LancamentoDTO;
import com.creitu.minhasFinancas.exception.RegraNegocioException;
import com.creitu.minhasFinancas.model.entity.Lancamento;
import com.creitu.minhasFinancas.model.entity.Usuario;
import com.creitu.minhasFinancas.service.LancamentoService;
import com.creitu.minhasFinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {

	private LancamentoService lancamentoService;
	private UsuarioService usuarioService;
	
	public LancamentoResource( LancamentoService lancamentoService, UsuarioService usuarioService ) {
		this.lancamentoService = lancamentoService;
		this.usuarioService = usuarioService;
	}
	
	@PostMapping("/salvar")
	public ResponseEntity salvar(@RequestBody LancamentoDTO lancamentoDTO ) {
		try {
			System.out.println(lancamentoDTO);
			Lancamento lancamento = converter(lancamentoDTO);
			
			lancamento = lancamentoService.salvar(lancamento);
			
			return new ResponseEntity(lancamento, HttpStatus.CREATED);
			
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO lancamentoDto) {
		try {
			return lancamentoService.obterPorId(id).map(item -> {
				Lancamento lancamento = converter(lancamentoDto);
				lancamento.setId(id);
				lancamentoService.atualizar(lancamento);
				
				return new ResponseEntity(lancamento, HttpStatus.OK);
			}).orElseGet( () -> 
				new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
			
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return lancamentoService.obterPorId(id).map( item -> {
			lancamentoService.deletar(item);
			return new ResponseEntity( HttpStatus.NO_CONTENT );
		}).orElseGet( () -> 
			new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
	}
	
	private Lancamento converter(LancamentoDTO lancamentoDTO) {
		
		System.out.println(lancamentoDTO.getUsuario());
		Usuario usuarioConsulta = usuarioService.obterPorId(lancamentoDTO.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o id informado."));
		
		Lancamento lancamento = new Lancamento(lancamentoDTO, usuarioConsulta);
		
		return lancamento;
	}
}
