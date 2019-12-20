package com.ulisses.guiaapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ulisses.guiaapi.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
