package com.ulisses.guiaapi.api.resource;

import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ulisses.guiaapi.api.dto.AtualizarStatusDTO;
import com.ulisses.guiaapi.api.dto.LancamentoDTO;
import com.ulisses.guiaapi.exception.RegraNegocioException;
import com.ulisses.guiaapi.model.entity.Lancamento;
import com.ulisses.guiaapi.model.entity.Usuario;
import com.ulisses.guiaapi.model.enums.StatusLancamento;
import com.ulisses.guiaapi.model.enums.TipoLancamento;
import com.ulisses.guiaapi.service.LancamentoService;
import com.ulisses.guiaapi.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController // Controller
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor // Cria o construtor com todos os atributos obrigatórios
public class LancamentoResource {

	private final LancamentoService lancamentoService;
	private final UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano, @RequestParam("usuario") Long usuario) {

		Optional<Usuario> usuarioFiltro = usuarioService.obterPorId(usuario);
		if (!usuarioFiltro.isPresent()) {
			return ResponseEntity.badRequest()
					.body("Não foi possivel encontrar o usuario para o lancamento informado.");
		} else {
			usuarioFiltro.get().setSenha(null);
			Lancamento lancamentoFiltro = Lancamento.builder().usuario(usuarioFiltro.get()).ano(ano).mes(mes)
					.descricao(descricao).build();
			List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
			return ResponseEntity.ok().body(lancamentos);
		}

	}

	@GetMapping("{id}")
	public ResponseEntity obterLancamento (@PathVariable("id") Long id) {
		return lancamentoService.obterPorId(id)
				.map( lancamento -> new ResponseEntity( converter( lancamento ) , HttpStatus.OK))
				.orElseGet( () -> new ResponseEntity(HttpStatus.NOT_FOUND));		
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		try {
			Lancamento lancamento = converter(dto);
			lancamento = lancamentoService.salvar(lancamento);
			return new ResponseEntity(lancamento, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {

		return lancamentoService.obterPorId(id).map(Entity -> {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(id);
				lancamentoService.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lancamento Não encontrado na base de dados.", HttpStatus.BAD_REQUEST));

	}

	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizarStatusDTO dto) {
		return lancamentoService.obterPorId(id).map(lancamento -> {

			StatusLancamento status = StatusLancamento.valueOf(dto.getStatus());

			if (status == null)
				return ResponseEntity.badRequest().body("Informe um status válido.");

			try {
				lancamentoService.atualizarStatus(lancamento, status);

				return ResponseEntity.ok(lancamento);
			} catch (Exception e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}

		}).orElseGet(() -> new ResponseEntity("Erro ao atualiszar status do lancamento.", HttpStatus.BAD_REQUEST));

	}

	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return lancamentoService.obterPorId(id).map(lancamento -> {
			lancamentoService.deletar(lancamento);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> ResponseEntity.badRequest().body("Lancamento não encontrado na base de dados."));
	}

	private LancamentoDTO converter(Lancamento lancamento) {
		return LancamentoDTO.builder()
					.id(lancamento.getId())
					.descricao(lancamento.getDescricao())
					.valor(lancamento.getValor())
					.mes(lancamento.getMes())
					.ano(lancamento.getAno())
					.status(lancamento.getStatus().name())
					.tipo(lancamento.getTipo().name())
					.usuario(lancamento.getUsuario().getId())
					.build();
					
					
	}
	
	private Lancamento converter(LancamentoDTO dto) {
		Usuario usuario = usuarioService.obterPorId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuario não encontrado para o lancamento informado."));

		Lancamento lancamento = Lancamento.builder().descricao(dto.getDescricao()).mes(dto.getMes()).ano(dto.getAno())
				.valor(dto.getValor()).usuario(usuario).build();

		if (dto.getTipo() != null)
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));

		if (dto.getStatus() != null)
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));

		return lancamento;
	}
	
}
