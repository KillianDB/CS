package com.reserva;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;

import com.reserva.entidade.Reserva;
import com.reserva.entidade.ReservaPeriferico;
import com.reserva.entidade.ReservaSala;
import com.reserva.repository.ReservaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    public Optional<Reserva> findById(String codigo) {
        return reservaRepository.findById(codigo);
    }

    public String findHorarioByCodigo(String codigo) {
        return reservaRepository.findHorarioByCodigo(codigo);
    }

    public boolean checaDisponibilidade(Reserva reserva) {
        return !Optional.of(checkDisponibilidade(reserva)).isPresent();
    }

    public ReservaSala saveSala(ReservaSala reserva) {
        return (ReservaSala) save(reserva);
    }

    public ReservaPeriferico savePeriferico(ReservaPeriferico reserva) {
        return (ReservaPeriferico) save(reserva);
    }

    @Transactional
    public <T extends Reserva> T save(T reserva) {
        int tentativa = 0;
        while (true) {
            tentativa++;
            Optional<Reserva> conflict = Optional.of(checkDisponibilidade(reserva));
            if (conflict.isPresent()) {
                throw new IllegalStateException(
                        "Item já reservado para data/hora/item informados.");
            }

            if (reserva.getCodigo() == null) {
                if (reserva instanceof ReservaSala) {
                    ReservaSala s = (ReservaSala) reserva;

                    String prefix = "SALA-" + s.getCodigoProfessor() + "-" + s.getCodigoTurma() + "-" + s.getData();
                    int next = calcularProximo(prefix);
                    reserva.setCodigo(prefix + next);
                } else if (reserva instanceof ReservaPeriferico) {
                    ReservaPeriferico p = (ReservaPeriferico) reserva;

                    String prefix = "PERI-" + p.getCodigoProfessor() + "-" + p.getCodigoTurma() + "-" + p.getData();
                    int next = calcularProximo(prefix);
                    reserva.setCodigo(prefix + next);
                }
            }

            try {
                return reservaRepository.save(reserva);
            } catch (DataIntegrityViolationException ex) {
                if (tentativa >= 5)
                    throw ex;
                reserva.setCodigo(null);
            }
        }
    }

    public <T extends Reserva> Reserva checkDisponibilidade(T reserva) {
        if (reserva.getCodigo() == null) {
            if (reserva instanceof ReservaSala) {
                ReservaSala s = (ReservaSala) reserva;

                Optional<ReservaSala> conflict = reservaRepository.checkDisponibilidadeSala(s.getData(),
                        s.getHora(),
                        s.getSala().getCodigo());
                if (conflict.isPresent()) {
                    return conflict.get();
                }
            } else if (reserva instanceof ReservaPeriferico) {
                ReservaPeriferico p = (ReservaPeriferico) reserva;

                Optional<ReservaPeriferico> conflict = reservaRepository.checkDisponibilidadePeriferico(p.getData(),
                        p.getHora(),
                        p.getPeriferico().getCodigo());
                if (conflict.isPresent()) {
                    return conflict.get();
                }
            }
            return null;
        }
        return null;
    }

    private int calcularProximo(String prefix) {
        List<Reserva> existentes = reservaRepository.findByCodigoStartingWith(prefix);
        int max = 0;
        for (Reserva r : existentes) {
            String s = r.getCodigo();
            if (s.length() <= prefix.length())
                continue;
            String suf = s.substring(prefix.length());
            try {
                int v = Integer.parseInt(suf);
                if (v > max)
                    max = v;
            } catch (NumberFormatException ignored) {
            }
        }
        return max + 1;
    }
}