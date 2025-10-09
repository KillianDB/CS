package main.java.com.turma.repository;

import com.turma.entidade.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, String> {

    Optional<Disciplina> findByNomeIgnoreCase(String nome);

    List<Disciplina> findByNomeContainingIgnoreCase(String nome);

    boolean existsByCodigo(String codigo);

}
