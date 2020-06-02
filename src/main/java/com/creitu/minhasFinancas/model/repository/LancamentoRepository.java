package com.creitu.minhasFinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creitu.minhasFinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
