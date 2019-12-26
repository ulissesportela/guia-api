package com.ulisses.guiaapi.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ulisses.guiaapi.model.entity.Usuario;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioReporitoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void  dadoQueExistaUmUsuarioComOEmaiInformadoProcessaExisteOEmailCadastradoRetornandoVerdadeiro( ) {
		//cenário
		Usuario usuario = criaUsuario();
		entityManager.persist(usuario);
		
		//Ação / Execução
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//Verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void dadoQueNaoExistaUmUsuarioComOEmaiInformadoProcessaExisteOEmailCadastradoRetornandoFalso( ) {
		//cenário
		
		//Ação / Execução
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//Verificação
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void dadoQueOsDadosDoUsuarioEstaoCorretosProcessaCadastraUmUsuarioRetornandoOUsuarioCadastrado() {
		//cenário
		Usuario usuario = criaUsuario();
		
		//Ação
		Usuario usuarioSalvo = entityManager.persist(usuario);
		
		//verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void dadoQueExisteOUsuarioCadastradoProcessaBuscarUmUsuarioPorEmailRetornandoOUsuarioEncontrato() {
		//cenário
		Usuario usuario = criaUsuario();
		
		//Ação
		Usuario usuarioSalvo = entityManager.persist(usuario);
		
		//verificação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void dadoQueNaoExisteOUsuarioCadastradoProcessaBuscarUmUsuarioPorEmailRetornandoVazio() {
		//cenário
		
		//Ação
		
		//verificação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	
	public static Usuario criaUsuario() {
		return Usuario
				.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.senha("senha")
				.build();
	}
}
