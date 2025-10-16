package com.reserva.repository;

import com.reserva.entidade.Reserva;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, String> {
    String findHorarioByCodigo(String codigo);
    boolean existsByCodigo(String codigo);

    @Query("SELECT * FROM reservas r WHERE r.data = ?1")
    Collection<Reserva> findReservasByData(String data);

    @Query("SELECT * FROM reservas r WHERE r.data = ?1 AND r.hora = ?2 AND r.id = ?3")
    Optional<Reserva> checkDisponibilidade(String data, String hora, String id);
}