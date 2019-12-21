package com.ulisses.guiaapi.api.resource;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ulisses.guiaapi.api.dto.UsuarioDTO;
import com.ulisses.guiaapi.exception.ErroAutenticacao;
import com.ulisses.guiaapi.exception.RegraNegocioException;
import com.ulisses.guiaapi.model.entity.Usuario;
import com.ulisses.guiaapi.service.LancamentoService;
import com.ulisses.guiaapi.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor // Cria o construtor com todos os atributos obrigatórios
public class UsuarioResource {

	private final UsuarioService service;
	private final LancamentoService lancamentoService;

	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {

		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		} catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		Usuario usuario = Usuario.builder().nome(dto.getNome()).email(dto.getEmail()).senha(dto.getSenha()).build();

		try {
			Usuario usuarioCriado = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioCriado, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@GetMapping("{id}/saldo")
	public ResponseEntity obterSaldo(@PathVariable("id") Long id) {

		Optional<Usuario> usuario = service.obterPorId(id);

		if (!usuario.isPresent())
			return new ResponseEntity(HttpStatus.NOT_FOUND);

		BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);

		return ResponseEntity.ok(saldo);

	}
}
