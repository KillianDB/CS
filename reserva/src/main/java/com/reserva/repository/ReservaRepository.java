package com.reserva.repository;

import com.reserva.entidade.Reserva;
import com.reserva.entidade.ReservaPeriferico;
import com.reserva.entidade.ReservaSala;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, String> {

        @Query("SELECT r.hora FROM Reserva r WHERE r.codigo = :codigo")
        String findHorarioByCodigo(@Param("codigo") String codigo);

        boolean existsByCodigo(String codigo);

        List<Reserva> findByData(String data);

        @Query("SELECT r FROM ReservaSala r WHERE r.sala.codigo = :codigoSala AND r.data = :data AND r.hora = :hora")
        Optional<ReservaSala> checkDisponibilidadeSala(@Param("codigoSala") String codigoSala,
                        @Param("data") String data,
                        @Param("hora") String hora);

        @Query("SELECT r FROM ReservaPeriferico r WHERE r.periferico.codigo = :codigoPeriferico AND r.data = :data AND r.hora = :hora")
        Optional<ReservaPeriferico> checkDisponibilidadePeriferico(@Param("codigoPeriferico") String codigoPeriferico,
                        @Param("data") String data,
                        @Param("hora") String hora);

        @Query("SELECT r FROM Reserva r WHERE r.codigo LIKE CONCAT('%', :prefix, '%')")
        List<Reserva> findByCodigoContaining(@Param("prefix") String prefix);
}