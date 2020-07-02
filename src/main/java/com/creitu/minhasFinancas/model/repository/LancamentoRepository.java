package com.creitu.minhasFinancas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.creitu.minhasFinancas.model.entity.Lancamento;
import com.creitu.minhasFinancas.model.enums.ETipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
	@Query( value = " SELECT SUM(l.valor) FROM Lancamento l JOIN l.usuario u "
				  + " WHERE u.id =:_idUsuario AND l.tipoLancamento = :_tipoLancamento GROUP BY u")
	BigDecimal obterSaldoPorTipoLancamentoEUsuario( @Param("_idUsuario") Long idUsuario, @Param("_tipoLancamento") ETipoLancamento tipo );

}
