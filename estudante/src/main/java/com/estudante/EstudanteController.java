package com.estudante;

import com.estudante.entidade.Estudante;
import com.estudante.repository.EstudanteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/estudante")
@CrossOrigin(origins = "*")
public class EstudanteController {

    @Autowired
    private EstudanteRepository estudanteRepository;

    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<Estudante> buscarPorMatricula(@PathVariable String matricula) {
        Optional<Estudante> estudante = estudanteRepository.findByMatricula(matricula);
        return estudante.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Estudante>> buscarPorNome(@PathVariable String nome) {
        List<Estudante> estudantes = estudanteRepository.findByNomeContainingIgnoreCase(nome);
        return ResponseEntity.ok(estudantes);
    }

    @PostMapping
    public ResponseEntity<?> criarEstudante(@Valid @RequestBody Estudante estudante) {
        try {
            if (estudante.getMatricula() != null && 
                estudanteRepository.existsByMatricula(estudante.getMatricula())) {
                return ResponseEntity.badRequest()
                    .body("Já existe um estudante com a matrícula: " + estudante.getMatricula());
            }

            if (estudante.getEmail() != null && 
                estudanteRepository.existsByEmail(estudante.getEmail())) {
                return ResponseEntity.badRequest()
                    .body("Já existe um estudante com o email: " + estudante.getEmail());
            }

            Estudante estudanteSalvo = estudanteRepository.save(estudante);
            return ResponseEntity.status(HttpStatus.CREATED).body(estudanteSalvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Erro ao criar estudante: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Estudante Service is running on port 8082");
    }
}