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
@RequestMapping("/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Reserva> buscarPorCodigo(@PathVariable String codigo) {
        Optional<Reserva> reserva = reservaRepository.findById(codigo);
        return reserva.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criarReserva(@Valid @RequestBody Reserva reserva) {
        try {
            if (reservaRepository.existsByCodigo(reserva.getCodigo())) {
                return ResponseEntity.badRequest()
                        .body("Já existe uma reserva com o código: " + reserva.getCodigo());
            }

            Reserva reservaSalva = reservaRepository.save(reserva);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservaSalva);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Erro ao criar reserva: " + e.getMessage());
        }
    }

    @GetMapping("/codigo/{codigo}/horario")
    public ResponseEntity<String> buscarHorarioDaReserva(@PathVariable String codigo) {
        String horario = reservaRepository.findHorarioByCodigo(codigo);
        return ResponseEntity.ok(horario);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Reserva Service is running on port 8081");
    }
}