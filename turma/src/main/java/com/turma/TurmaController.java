package com.turma;

import com.turma.dto.TurmaDTO;
import com.turma.entidade.Turma;
import com.turma.repository.TurmaRepository;
import com.turma.service.TurmaService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/turmas")
@CrossOrigin(origins = "*")
public class TurmaController {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private TurmaService turmaService;

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Busca turma pelo código")
    @ApiResponse(responseCode = "200", description = "Turma encontrada")
    @ApiResponse(responseCode = "404", description = "Turma não encontrada")
    public ResponseEntity<Turma> buscarPorCodigo(@PathVariable String codigo) {
        Optional<Turma> turma = turmaRepository.findById(codigo);
        return turma.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/codigo/{codigo}/calendario/")
    public ResponseEntity<?> atualizaCalendario(@PathVariable String codigo, @RequestBody String horario){
        try{
            turmaService.atualizaCalendario(codigo, horario);
            Optional<String> horarioAtualizado = turmaRepository.findHorarioByCodigo(codigo);

            if(horarioAtualizado.isPresent()){
                return ResponseEntity.ok(horarioAtualizado.get());
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    @Operation(summary = "Criar nova turma")
    @ApiResponse(responseCode = "201", description = "Turma criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao criar turma")
    public ResponseEntity<?> criarTurma(@Valid @RequestBody TurmaDTO turma) {
        try {
            String turmaSalva = turmaService.criaTurma(turma);
            return ResponseEntity.status(HttpStatus.CREATED).body(turmaSalva);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Erro ao criar turma: " + e.getMessage());
        }
    }

    @PostMapping("/codigo/{codigo}/estudantes/{estudanteId}")
    @Operation(summary = "Adicionar estudante à turma")
    @ApiResponse(responseCode = "200", description = "Estudante adicionado com sucesso")
    @ApiResponse(responseCode = "404", description = "Turma não encontrada")
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

    @PostMapping("/estudantes/")
    public ResponseEntity<?> adicionarEstudanteArquivo(@RequestParam("file") MultipartFile file) {
        try {
            turmaService.addAlunos(file);

            return ResponseEntity.ok().body("Estudantes Cadastrados");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao fazer upload do arquivo: " + e.getMessage());
        }
    }

    @GetMapping("/codigo/{codigo}/horario")
    @Operation(summary = "Busca horário da turma pelo código")
    @ApiResponse(responseCode = "200", description = "Horário encontrado")
    @ApiResponse(responseCode = "404", description = "Turma não encontrada")
    public ResponseEntity<String> buscarHorarioDaTurma(@PathVariable String codigo) {
        Optional<String> horario = turmaRepository.findHorarioByCodigo(codigo);
        if(horario.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(horario.get());
    }


    @GetMapping("/aluno/{estudanteId}")
    @Operation(summary = "Busca turmas por estudante")
    @ApiResponse(responseCode = "200", description = "Turmas encontradas")
    @ApiResponse(responseCode = "404", description = "Estudante não encontrado")
    public ResponseEntity<List<Turma>> buscarTurmasPorEstudante(@PathVariable String estudanteId) {
        try {
            List<Turma> turmas = turmaRepository.findTurmasByEstudante(estudanteId);
            return ResponseEntity.ok(turmas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); 
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Health check turma")
    @ApiResponse(responseCode = "200", description = "Serviço está funcionando")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Turma Service is running on port 8081");
    }
}