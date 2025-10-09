package com.turma.repository;

import com.turma.entidade.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, String> {

    Optional<Turma> findByNomeIgnoreCase(String nome);

    List<Turma> findByNomeContainingIgnoreCase(String nome);

    String findHorarioByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

}