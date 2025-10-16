package com.reserva;

import org.springframework.stereotype.Service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.reserva.entidade.Reserva;
import com.reserva.repository.ReservaRepository;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public boolean checaDisponibilidade(Reserva reserva){
        return reservaRepository.checkDisponibilidade(reserva.getData(), reserva.getHora(), reserva.getCodigo()).isPresent();
    }
}
