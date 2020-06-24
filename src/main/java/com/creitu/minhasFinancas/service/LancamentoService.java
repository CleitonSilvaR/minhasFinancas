package com.creitu.minhasFinancas.service;

import java.util.List;

import com.creitu.minhasFinancas.model.entity.Lancamento;
import com.creitu.minhasFinancas.model.enums.EStatusLancamento;

public interface LancamentoService {

	Lancamento salvar( Lancamento lancamento );
	
	Lancamento atualizar( Lancamento lancamento );
	
	void deletar( Lancamento lancamento );
	
	List<Lancamento> buscar( Lancamento lancamentoFiltro );
	
	void atualizarStatus( Lancamento lancamento, EStatusLancamento statusLancamento );
	void validar( Lancamento lancamento );
}
