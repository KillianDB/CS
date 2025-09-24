package com.estudante.repository;

import com.estudante.entidade.Estudante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudanteRepository extends JpaRepository<Estudante, Long> {
    
    Optional<Estudante> findByMatricula(String matricula);
    
    List<Estudante> findByNomeContainingIgnoreCase(String nome);
    
    boolean existsByMatricula(String matricula);
    
    boolean existsByEmail(String email);

}