package com.turma;

import com.turma.dto.TurmaDTO;
import com.turma.entidade.Turma;
import com.turma.repository.TurmaRepository;
import com.turma.service.TurmaService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

// --- Swagger imports (MISSING IN YOUR CODE) ---
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/turmas")
@CrossOrigin(origins = "*")
public class TurmaController {

    private final TurmaRepository turmaRepository;
    private final TurmaService turmaService;

    public TurmaController(TurmaRepository turmaRepository, TurmaService turmaService) {
        this.turmaRepository = turmaRepository;
        this.turmaService = turmaService;
    }

    // ------------------------------------------------------------------------

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Busca turma pelo código")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Turma encontrada"),
        @ApiResponse(responseCode = "404", description = "Turma não encontrada")
    })
    public ResponseEntity<Turma> buscarPorCodigo(@PathVariable String codigo) {
        return turmaRepository.findById(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ------------------------------------------------------------------------

    @PostMapping("/codigo/{codigo}/calendario")
    @Operation(summary = "Atualiza calendário da turma")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Calendário atualizado"),
        @ApiResponse(responseCode = "404", description = "Turma não encontrada")
    })
    public ResponseEntity<?> atualizaCalendario(@PathVariable String codigo,
                                                @RequestBody String horario) {
        try {
            turmaService.atualizaCalendario(codigo, horario);
            Optional<String> horarioAtualizado = turmaRepository.findHorarioByCodigo(codigo);

            return horarioAtualizado
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar calendário: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------------

    @PostMapping
    @Operation(summary = "Criar nova turma")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Turma criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao criar turma")
    })
    public ResponseEntity<?> criarTurma(@Valid @RequestBody TurmaDTO turma) {
        try {
            String turmaCriada = turmaService.criaTurma(turma);
            return ResponseEntity.status(HttpStatus.CREATED).body(turmaCriada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao criar turma: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------------

    @PostMapping("/codigo/{codigo}/estudantes/{estudanteId}")
    @Operation(summary = "Adicionar estudante à turma")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudante adicionado"),
        @ApiResponse(responseCode = "404", description = "Turma não encontrada")
    })
    public ResponseEntity<?> adicionarEstudante(
            @PathVariable String codigo,
            @PathVariable String estudanteId) {

        try {
            Optional<Turma> turmaOpt = turmaRepository.findById(codigo);

            if (turmaOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Turma turma = turmaOpt.get();
            turma.adicionaEstudante(estudanteId);
            turmaRepository.save(turma);

            return ResponseEntity.ok("Estudante adicionado à turma com sucesso");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao adicionar estudante: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------------

    @PostMapping("/estudantes")
    @Operation(summary = "Adicionar estudantes via arquivo CSV/Excel")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudantes adicionados"),
        @ApiResponse(responseCode = "400", description = "Erro ao processar arquivo")
    })
    public ResponseEntity<?> adicionarEstudanteArquivo(@RequestParam("file") MultipartFile file) {
        try {
            turmaService.addAlunos(file);
            return ResponseEntity.ok("Estudantes cadastrados");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao fazer upload: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------------

    @GetMapping("/codigo/{codigo}/horario")
    @Operation(summary = "Busca horário da turma")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Horário encontrado"),
        @ApiResponse(responseCode = "404", description = "Turma não encontrada")
    })
    public ResponseEntity<String> buscarHorarioDaTurma(@PathVariable String codigo) {
        return turmaRepository.findHorarioByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ------------------------------------------------------------------------

    @GetMapping("/aluno/{estudanteId}")
    @Operation(summary = "Busca turmas por estudante")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Turmas encontradas"),
        @ApiResponse(responseCode = "404", description = "Estudante não encontrado")
    })
    public ResponseEntity<List<Turma>> buscarTurmasPorEstudante(@PathVariable String estudanteId) {
        try {
            List<Turma> turmas = turmaRepository.findTurmasByEstudante(estudanteId);
            return ResponseEntity.ok(turmas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ------------------------------------------------------------------------

    @GetMapping("/health")
    @Operation(summary = "Health check turma")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Serviço está funcionando")
    })
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Turma Service is running");
    }
}
