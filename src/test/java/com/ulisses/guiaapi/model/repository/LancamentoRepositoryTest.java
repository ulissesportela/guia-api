package com.ulisses.guiaapi.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ulisses.guiaapi.model.entity.Lancamento;
import com.ulisses.guiaapi.model.enums.StatusLancamento;
import com.ulisses.guiaapi.model.enums.TipoLancamento;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

	@Autowired
	LancamentoRepository repository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void devevSalvarUmLancamento() {
		Lancamento lancamento = criarLancamento();

		lancamento = repository.save(lancamento);

		assertThat(lancamento.getId()).isNotNull();
	}

	@Test
	public void deveExcluirUmLancamento() {
		Lancamento lancamento = CriaEPersisteUmLancamento();

		entityManager.find(Lancamento.class, lancamento.getId());

		repository.delete(lancamento);

		Lancamento lancamentoExcluido = entityManager.find(Lancamento.class, lancamento.getId());

		assertThat(lancamentoExcluido).isNull();

	}

	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamento = CriaEPersisteUmLancamento();

		lancamento.setAno(2018);
		lancamento.setMes(11);
		lancamento.setDescricao("Lancamento aleatório alterado");
		lancamento.setValor(BigDecimal.valueOf(11));
		lancamento.setTipo(TipoLancamento.DESPESA);
		lancamento.setStatus(StatusLancamento.CANCELADO);
		lancamento.setDataCadastro(LocalDate.of(2018, 12, 22));

		repository.save(lancamento);

		Lancamento lancamentoAlterado = entityManager.find(Lancamento.class, lancamento.getId());

		assertThat(lancamento).isEqualTo(lancamentoAlterado);

	}

	@Test
	public void deveBuscarLancamentoPorId() {
		Lancamento lancamento = CriaEPersisteUmLancamento();

		Optional<Lancamento> lancamentoPesquisado = repository.findById(lancamento.getId());

		assertThat(lancamentoPesquisado.isPresent()).isTrue();

	}

	private Lancamento CriaEPersisteUmLancamento() {
		return entityManager.persist(criarLancamento());
	}

	public static Lancamento criarLancamento() {
		return Lancamento.builder().ano(2019).mes(12).descricao("Lancamento aleatório").valor(BigDecimal.valueOf(10))
				.tipo(TipoLancamento.RECEITA).status(StatusLancamento.PENDENTE).dataCadastro(LocalDate.now()).build();
	}

}
