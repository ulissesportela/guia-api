package com.ulisses.guiaapi.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ulisses.guiaapi.exception.ErroAutenticacao;
import com.ulisses.guiaapi.exception.RegraNegocioException;
import com.ulisses.guiaapi.model.entity.Usuario;
import com.ulisses.guiaapi.model.repository.UsuarioRepository;
import com.ulisses.guiaapi.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	private UsuarioRepository repository;
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida.");
		}
		
		return Usuario.builder().email(usuario.get().getEmail()).nome(usuario.get().getNome()).id(usuario.get().getId()).build();
		// usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if (existe) {
			throw new RegraNegocioException("Já existe um usuario cadastrado com este email.");
		}
		
	}

}
