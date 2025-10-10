package com.reserva.repository;

import com.reserva.entidade.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, String> {

    String findHorarioByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

}