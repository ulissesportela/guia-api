package com.ulisses.guiaapi.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.ulisses.guiaapi.model.entity.Lancamento;
import com.ulisses.guiaapi.model.enums.StatusLancamento;

public interface LancamentoService {

	Lancamento salvar(Lancamento lancamento);

	Lancamento atualizar(Lancamento lancamento);

	void deletar(Lancamento lancamento);

	List<Lancamento> buscar(Lancamento lancamentoFiltro);

	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	Optional<Lancamento> obterPorId(Long id);
	
	BigDecimal obterSaldoPorUsuario( Long id);

	void validar(Lancamento lancamento);
}
