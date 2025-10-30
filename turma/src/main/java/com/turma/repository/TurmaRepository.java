package com.turma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turma.entidade.Turma;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, String> {

    String findHorarioByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<Turma> findByEstudantesContaining(String estudanteId);

}