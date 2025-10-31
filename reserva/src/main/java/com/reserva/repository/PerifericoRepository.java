package com.reserva.repository;


import com.reserva.entidade.Periferico;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PerifericoRepository extends JpaRepository<Periferico, String>  {
    List<Periferico> findAllByTipo(String tipo);
}
