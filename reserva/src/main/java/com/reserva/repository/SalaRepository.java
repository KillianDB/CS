package com.reserva.repository;


import com.reserva.entidade.Sala;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaRepository extends JpaRepository<Sala, String>  {
    List<Sala> findAllByTipo(String tipo);
}
