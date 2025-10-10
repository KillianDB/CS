package com.usuario.repository;

import com.usuario.entidade.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByMatricula(String matricula);

    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    boolean existsByMatricula(String matricula);
    
    boolean existsByEmail(String email);

}