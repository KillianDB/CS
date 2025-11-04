package com.reserva;

import com.reserva.entidade.Reserva;
import com.reserva.entidade.ReservaPeriferico;
import com.reserva.entidade.ReservaSala;
import com.reserva.utils.ApiResponse;
import com.reserva.utils.AuthService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import com.reserva.utils.TurmaDTO; 
import java.util.Arrays; 
import java.util.ArrayList; 
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;
    private final AuthService authService;

    @Autowired
    private RestTemplate restTemplate;
    

    public ReservaController(ReservaService reservaService, AuthService authService) {
        this.reservaService = reservaService;
        this.authService = authService;
    }

    @PostMapping("/sala")
    @Operation(summary = "Criar nova reserva")
    @ApiResponse(responseCode = "201", description = "Reserva de sala criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao criar reserva de sala")
    public ResponseEntity<ApiResponse<ReservaSala>> criarReservaSala(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody ReservaSala reserva) {
        try {
            if (!authService.isProfessor(authorization)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Acesso negado: somente professores podem reservar salas"));
            }
            ReservaSala reservaSalva = reservaService.saveSala(reserva);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(reservaSalva));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao criar reserva de sala: " + e.getMessage()));
        }
    }

    @PostMapping("/periferico")
    @Operation(summary = "Criar nova reserva de periférico")
    @ApiResponse(responseCode = "201", description = "Reserva de periférico criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao criar reserva de periférico")
    public ResponseEntity<ApiResponse<ReservaPeriferico>> criarReservaPeriferico(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody ReservaPeriferico reserva) {
        try {
            if (!authService.isProfessor(authorization)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Acesso negado: somente professores podem reservar salas"));
            }
            ReservaPeriferico reservaSalva = reservaService.savePeriferico(reserva);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(reservaSalva));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao criar reserva de periferico: " + e.getMessage()));
        }
    }

    @GetMapping("/items/tipo")
    @Operation(summary = "Buscar todos os itens por tipo")
    @ApiResponse(responseCode = "200", description = "Itens encontrados")
    @ApiResponse(responseCode = "404", description = "Itens não encontrados")
    public ResponseEntity<List<?>> buscarTodosItemsPorTipo(@RequestParam String tipoItem, @RequestParam String tipo) {
        List<?> reservas = reservaService.findAllByTipo(tipoItem, tipo);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/usuario")
    @Operation(summary = "Buscar reservas por usuário")
    @ApiResponse(responseCode = "200", description = "Reservas encontradas")
    @ApiResponse(responseCode = "404", description = "Reservas não encontradas")
    public ResponseEntity<List<Reserva>> buscarReservasPorUsuario(
            @RequestParam(value = "prefix", required = true) String prefix,
            @RequestParam(value = "tipo", required = false) String tipo) {
        List<Reserva> reservas = reservaService.findByCodigoContaining(prefix);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar reserva por código")
    @ApiResponse(responseCode = "200", description = "Reserva encontrada")
    @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    public ResponseEntity<Reserva> buscarPorCodigo(@PathVariable String codigo) {
        Optional<Reserva> reserva = reservaService.findById(codigo);
        return reserva.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}/horario")
    @Operation(summary = "Busca horário da reserva pelo código")
    @ApiResponse(responseCode = "200", description = "Horário encontrado")
    @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    public ResponseEntity<String> buscarHorarioDaReserva(@PathVariable String codigo) {
        String horario = reservaService.findHorarioByCodigo(codigo);
        return ResponseEntity.ok(horario);
    }

    @GetMapping("/aluno/{matricula}/laboratorios")
    @Operation(summary = "Buscar laboratórios reservados por aluno")
    @ApiResponse(responseCode = "200", description = "Laboratórios encontrados")
    @ApiResponse(responseCode = "404", description = "Laboratórios não encontrados")
    public ResponseEntity<ApiResponse<List<ReservaSala>>> buscarLaboratoriosDoAluno(
            @PathVariable String matricula,
            @RequestHeader(value = "Authorization") String authorization,
            @RequestParam(value = "disciplina", required = false) String disciplina,
            @RequestParam(value = "horario", required = false) String horario) {

        try {
            String alunoId = matricula;

            String urlTurmas = "http://localhost:8082/turmas/aluno/" + alunoId; 

            ResponseEntity<TurmaDTO[]> responseTurmas = restTemplate.getForEntity(urlTurmas, TurmaDTO[].class);
            
            if (!responseTurmas.getStatusCode().is2xxSuccessful() || responseTurmas.getBody() == null) {
                return ResponseEntity.ok(ApiResponse.success(new ArrayList<>()));
            }

            List<TurmaDTO> turmasDoAluno = Arrays.asList(responseTurmas.getBody());

            if (turmasDoAluno.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.success(new ArrayList<>()));
            }

            List<String> codigosDasTurmas = turmasDoAluno.stream()
                                                    .map(TurmaDTO::getCodigo)
                                                    .collect(Collectors.toList());

            List<ReservaSala> reservas = reservaService.findReservasByCodigosTurma(codigosDasTurmas);

            List<ReservaSala> reservasFiltradas = reservas.stream()
                .filter(reserva -> {
                    boolean matchHorario = (horario == null) || 
                                           (reserva.getHora() != null && reserva.getHora().contains(horario)); 

                    TurmaDTO turmaAssociada = turmasDoAluno.stream()
                        .filter(t -> t.getCodigo().equals(reserva.getCodigoTurma()))
                        .findFirst().orElse(null);

                    boolean matchDisciplina = (disciplina == null) || 
                                              (turmaAssociada != null && 
                                               turmaAssociada.getNomeDisciplina() != null &&
                                               turmaAssociada.getNomeDisciplina().contains(disciplina));

                    return matchHorario && matchDisciplina;
                })
                .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success(reservasFiltradas));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao buscar laboratórios: " + e.getMessage()));
        }
    }
    
    @GetMapping("/health")
    @Operation(summary = "Health check reservas")
    @ApiResponse(responseCode = "200", description = "Serviço está funcionando")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Reserva Service is running on port 8081");
    }
}