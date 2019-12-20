package com.ulisses.guiaapi.service;

import java.util.List;

import com.ulisses.guiaapi.model.entity.Lancamento;
import com.ulisses.guiaapi.model.enums.StatusLancamento;

public interface LancamentoService {

	Lancamento salvar(Lancamento lancamento);

	Lancamento atualizar(Lancamento lancamento);

	void deletar(Lancamento lancamento);

	List<Lancamento> buscar(Lancamento lancamentoFiltro);

	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
}
