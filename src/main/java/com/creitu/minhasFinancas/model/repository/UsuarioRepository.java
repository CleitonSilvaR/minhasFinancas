package com.creitu.minhasFinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creitu.minhasFinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
