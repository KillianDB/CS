package com.reserva.entidade;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "reservas_salas")
public class ReservaSala extends Reserva {

    @Id
    @NotBlank(message = "Código da sala é obrigatório")
    @Size(min = 3, max = 30, message = "Código deve ter entre 3 e 30 caracteres")
    private String codigo;

    @Column(nullable = false)
    @NotBlank(message = "Tipo de sala é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoSala tipo;

    public ReservaSala(String codigoProfessor, String codigoSala, TipoSala tipo) {
        super();
        this.codigo = codigoProfessor + codigoSala;
        this.tipo = tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigoProfessor, String codigoSala) {
        this.codigo = codigoProfessor + codigoSala;
    }

    public TipoSala getTipo() {
        return tipo;
    }

    public void setTipo(TipoSala tipo) {
        this.tipo = tipo;
    }
}
