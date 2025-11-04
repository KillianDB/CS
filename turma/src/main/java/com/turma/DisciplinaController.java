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
    @Operation(summary = "Busca disciplina pelo código")
    @ApiResponse(responseCode = "200", description = "Disciplina encontrada")
    @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    public ResponseEntity<Disciplina> buscarPorCodigo(@PathVariable String codigo) {
        Optional<Disciplina> disciplina = disciplinaRepository.findById(codigo);
        return disciplina.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar nova disciplina")
    @ApiResponse(responseCode = "201", description = "Disciplina criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao criar disciplina")
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
    @Operation(summary = "Busca disciplinas pelo nome")
    @ApiResponse(responseCode = "200", description = "Disciplinas encontradas")
    @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    public ResponseEntity<List<Disciplina>> buscarPorNome(@PathVariable String nome) {
        List<Disciplina> disciplinas = disciplinaRepository.findByNomeContainingIgnoreCase(nome);
        return ResponseEntity.ok(disciplinas);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check disciplina")
    @ApiResponse(responseCode = "200", description = "Serviço está funcionando")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Disciplina Service is running on port 8081");
    }
}