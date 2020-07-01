package com.creitu.minhasFinancas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.creitu.minhasFinancas.model.entity.Lancamento;
import com.creitu.minhasFinancas.model.enums.EStatusLancamento;

public interface LancamentoService {

	Lancamento salvar( Lancamento lancamento );
	
	Lancamento atualizar( Lancamento lancamento );
	
	void deletar( Lancamento lancamento );
	
	List<Lancamento> buscar( Lancamento lancamentoFiltro );
	
	void atualizarStatus( Lancamento lancamento, EStatusLancamento statusLancamento );
	
	void validar( Lancamento lancamento );
	
//	@Query("SELECT l FROM Lancamento l JOIN FETCH l.usuario WHERE l.id = (:_id)")
	Optional<Lancamento> obterPorId(@Param("_id") Long id );
}
