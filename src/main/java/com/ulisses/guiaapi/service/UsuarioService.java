package com.ulisses.guiaapi.service;

import java.util.Optional;

import com.ulisses.guiaapi.model.entity.Usuario;

public interface UsuarioService {

	Usuario autenticar(String email, String senha);

	Usuario salvarUsuario(Usuario usuario);

	void validarEmail(String email);

	Optional<Usuario> obterPorId(Long id);
}
