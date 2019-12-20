package com.ulisses.guiaapi.service.impl;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import com.ulisses.guiaapi.model.entity.Lancamento;
import com.ulisses.guiaapi.model.enums.StatusLancamento;
import com.ulisses.guiaapi.model.repository.LancamentoRepository;
import com.ulisses.guiaapi.service.LancamentoService;

@Service // Cria o service geranciavel
public class LancamentoServiceImpl implements LancamentoService {

	private LancamentoRepository repository;
	
	public LancamentoServiceImpl(LancamentoRepository repository) {
		this.repository = repository;
	}
	
	@Override
	@Transactional //Inicia a tranzação e ao final realiza o commit ou o rolback
	public Lancamento salvar(Lancamento lancamento) {
		return repository.save(lancamento);
	}

	@Override
	@Transactional //Inicia a tranzação e ao final realiza o commit ou o rolback
	public Lancamento atualizar(Lancamento lancamento) {
		 Objects.requireNonNull(lancamento.getId());
		return repository.save(lancamento);
	}

	@Override
	@Transactional //Inicia a tranzação e ao final realiza o commit ou o rolback
	public void deletar(Lancamento lancamento) {
		repository.delete(lancamento);
	}

	@Override
	@Transactional
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		
		//Monta o filtro, com ignoreCase e contendo a string
		Example<Lancamento> example = Example.of(lancamentoFiltro, 
				ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
	}

}
