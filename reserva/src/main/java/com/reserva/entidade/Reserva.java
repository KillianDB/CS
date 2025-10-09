package com.reserva.entidade;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @NotBlank(message = "Código da reserva é obrigatório")
    @Size(min = 3, max = 10, message = "Código deve ter entre 3 e 10 caracteres")
    private String codigo;

    @Column(nullable = false)
    @NotBlank(message = "Horário é obrigatório")
    private String hora;

    @Column(nullable = false)
    @NotBlank(message = "Data é obrigatória")
    private String data;

    @ElementCollection
    @NotBlank(message = "Turma é obrigatória")
    @CollectionTable(name = "reserva_turma", joinColumns = @JoinColumn(name = "reserva_codigo"))
    @Column(name = "turma_id")
    private String idTurma;

    public Reserva() {
    }

    public Reserva(String codigo, String hora, String data, String idTurma) {
        this.codigo = codigo;
        this.hora = hora;
        this.data = data;
        this.idTurma = idTurma;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(String idTurma) {
        this.idTurma = idTurma;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "codigo='" + codigo + '\'' +
                ", hora='" + hora + '\'' +
                ", data='" + data + '\'' +
                ", idTurma='" + idTurma + '\'' +
                '}';
    }
}