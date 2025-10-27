package com.reserva.entidade;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "reservas_salas")
public class ReservaSala extends Reserva {

    @Column(name = "codigo_professor", nullable = false)
    @NotBlank(message = "Código do professor é obrigatório")
    private String codigoProfessor;

    @Column(name = "codigo_turma", nullable = false)
    @NotBlank(message = "Código da turma é obrigatório")
    private String codigoTurma;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_sala", referencedColumnName = "codigo")
    private Sala sala;

    @Column(name = "tipo_sala", nullable = false)
    @NotBlank(message = "Tipo de sala é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoSala tipo;

    public ReservaSala(String codigoProfessor, String codigoTurma, String hora, String data, Sala sala) {
        super(codigoProfessor + codigoTurma + data, hora, data);
        this.codigoProfessor = codigoProfessor;
        this.codigoTurma = codigoTurma;
        this.sala = sala;
        this.tipo = sala.getTipo();
    }

    public ReservaSala() {
    }

    public String getCodigoProfessor() {
        return codigoProfessor;
    }

    public void setCodigoProfessor(String codigoProfessor) {
        this.codigoProfessor = codigoProfessor;
    }

    public String getCodigoTurma() {
        return codigoTurma;
    }

    public void setCodigoTurma(String codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public TipoSala getTipo() {
        return tipo;
    }

    public void setTipo(TipoSala tipo) {
        this.tipo = tipo;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }
}
