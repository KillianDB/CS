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

import java.util.Optional;

@RestController
@RequestMapping("/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;
    private final AuthService authService;

    public ReservaController(ReservaService reservaService, AuthService authService) {
        this.reservaService = reservaService;
        this.authService = authService;
    }

    @PostMapping("/sala")
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

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Reserva> buscarPorCodigo(@PathVariable String codigo) {
        Optional<Reserva> reserva = reservaService.findById(codigo);
        return reserva.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}/horario")
    public ResponseEntity<String> buscarHorarioDaReserva(@PathVariable String codigo) {
        String horario = reservaService.findHorarioByCodigo(codigo);
        return ResponseEntity.ok(horario);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Reserva Service is running on port 8081");
    }
}