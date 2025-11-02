package com.turma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.turma.entidade.Turma;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, String> {
    Optional<Turma> findById(String codigo);

    @Query("SELECT t.horario FROM Turma t WHERE t.codigo = :codigo")
    Optional<String> findHorarioByCodigo(@Param("codigo") String codigo);

    boolean existsByCodigo(String codigo);

    List<Turma> findByCodigoIn(List<String> codigos);

    // List<Turma> findByEstudantesContaining(String estudanteId);
}