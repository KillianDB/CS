package com.turma;

import com.turma.entidade.Turma;
import com.turma.repository.TurmaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/turmas")
@CrossOrigin(origins = "*")
public class TurmaController {

    @Autowired
    private TurmaRepository turmaRepository;

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Turma> buscarPorCodigo(@PathVariable String codigo) {
        Optional<Turma> turma = turmaRepository.findById(codigo);
        return turma.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criarTurma(@Valid @RequestBody Turma turma) {
        try {
            if (turmaRepository.existsByCodigo(turma.getCodigo())) {
                return ResponseEntity.badRequest()
                        .body("Já existe uma turma com o código: " + turma.getCodigo());
            }

            Turma turmaSalva = turmaRepository.save(turma);
            return ResponseEntity.status(HttpStatus.CREATED).body(turmaSalva);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Erro ao criar turma: " + e.getMessage());
        }
    }

    @PostMapping("/codigo/{codigo}/estudantes/{estudanteId}")
    public ResponseEntity<?> adicionarEstudante(@PathVariable String codigo,
            @PathVariable String estudanteId) {
        try {
            Optional<Turma> turmaOpt = turmaRepository.findById(codigo);

            if (turmaOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Turma turma = turmaOpt.get();
            turma.adicionaEstudante(estudanteId);
            turmaRepository.save(turma);

            return ResponseEntity.ok().body("Estudante adicionado à turma com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Erro ao adicionar estudante: " + e.getMessage());
        }
    }

    @GetMapping("/codigo/{codigo}/horario")
    public ResponseEntity<String> buscarHorarioDaTurma(@PathVariable String codigo) {
        String horario = turmaRepository.findHorarioByCodigo(codigo);
        return ResponseEntity.ok(horario);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Turma Service is running on port 8081");
    }
}