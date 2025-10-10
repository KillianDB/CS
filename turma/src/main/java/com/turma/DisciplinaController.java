package com.turma;

import com.turma.entidade.Disciplina;
import com.turma.repository.DisciplinaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/disciplinas")
@CrossOrigin(origins = "*")
public class DisciplinaController {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Disciplina> buscarPorCodigo(@PathVariable String codigo) {
        Optional<Disciplina> disciplina = disciplinaRepository.findById(codigo);
        return disciplina.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criarDisciplina(@Valid @RequestBody Disciplina disciplina) {
        try {
            if (disciplinaRepository.existsByCodigo(disciplina.getCodigo())) {
                return ResponseEntity.badRequest()
                        .body("Já existe uma disciplina com o código: " + disciplina.getCodigo());
            }

            Disciplina disciplinaSalva = disciplinaRepository.save(disciplina);
            return ResponseEntity.status(HttpStatus.CREATED).body(disciplinaSalva);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Erro ao criar disciplina: " + e.getMessage());
        }
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Disciplina>> buscarPorNome(@PathVariable String nome) {
        List<Disciplina> disciplinas = disciplinaRepository.findByNomeContainingIgnoreCase(nome);
        return ResponseEntity.ok(disciplinas);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Disciplina Service is running on port 8081");
    }
}