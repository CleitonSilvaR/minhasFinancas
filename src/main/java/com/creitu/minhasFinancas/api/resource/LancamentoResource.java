package com.creitu.minhasFinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	public LancamentoResource( LancamentoService lancamentoService ) {
		this.lancamentoService = lancamentoService;
	}
	
	@PostMapping("/salvar")
	public ResponseEntity salvar(@RequestBody LancamentoDTO lancamentoDTO ) {
		try {
			Lancamento lancamento = converter(lancamentoDTO);
			
			lancamento = lancamentoService.salvar(lancamento);
			
			return new ResponseEntity(lancamento, HttpStatus.CREATED);
			
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	private Lancamento converter(LancamentoDTO lancamentoDTO) {
		
		Usuario usuarioConsulta = usuarioService.obterPorId(lancamentoDTO.getId())
				.orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o id informado."));
		
		Lancamento lancamento = new Lancamento(lancamentoDTO, usuarioConsulta);
		
		return lancamento;
	}
}
