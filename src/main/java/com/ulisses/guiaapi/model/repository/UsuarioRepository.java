package com.ulisses.guiaapi.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ulisses.guiaapi.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	//Query Methods do Spring Data
	Optional<Usuario> findByEmail(String email);
	
	boolean existsByEmail(String email);
	
}
