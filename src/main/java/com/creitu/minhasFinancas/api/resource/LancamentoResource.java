package com.creitu.minhasFinancas.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.creitu.minhasFinancas.api.dto.AtualizaStatusDTO;
import com.creitu.minhasFinancas.api.dto.LancamentoDTO;
import com.creitu.minhasFinancas.exception.RegraNegocioException;
import com.creitu.minhasFinancas.model.entity.Lancamento;
import com.creitu.minhasFinancas.model.entity.Usuario;
import com.creitu.minhasFinancas.model.enums.EStatusLancamento;
import com.creitu.minhasFinancas.service.LancamentoService;
import com.creitu.minhasFinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoResource {

	private final LancamentoService lancamentoService;
	private final UsuarioService usuarioService;
	
	@GetMapping
	public ResponseEntity buscar(
			@RequestParam(value ="descricao", required = false) String descricao,
			@RequestParam(value ="mes", required = false) Integer mes,
			@RequestParam(value ="ano", required = false) Integer ano,
			@RequestParam("usuario") Long idUsuario
			) {
		Lancamento lancamentoFiltro = new Lancamento(descricao, mes, ano);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		
		if (!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o Id informado.");
		} else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);

		
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
				lancamento.setDataCadastro(item.getDataCadastro());
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
	
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO atualizaStatusDTO) {
		try {
			return lancamentoService.obterPorId(id).map(item -> {
				EStatusLancamento statusSelecionado = EStatusLancamento.valueOf(atualizaStatusDTO.getStatus());
				
				if (statusSelecionado == null) {
					return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento, envie um status válido.");
				}
				
				item.setStatusLancamento(statusSelecionado);
				lancamentoService.atualizar(item);
				
				return new ResponseEntity(item, HttpStatus.OK);
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
