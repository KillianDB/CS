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
    public ResponseEntity<Turma> buscarPorCodigo(@PathVariable String codigo) {
        Optional<Turma> turma = turmaRepository.findById(codigo);
        return turma.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/codigo/{codigo}/calendario")
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
    public ResponseEntity<String> buscarHorarioDaTurma(@PathVariable String codigo) {
        Optional<String> horario = turmaRepository.findHorarioByCodigo(codigo);
        if(horario.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(horario.get());
    }

    @GetMapping("/aluno/{estudanteId}")
    public ResponseEntity<List<Turma>> buscarTurmasPorEstudante(@PathVariable String estudanteId) {
        try {
            List<Turma> turmas = turmaRepository.findTurmasByEstudante(estudanteId);
            return ResponseEntity.ok(turmas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Turma Service is running on port 8081");
    }
}