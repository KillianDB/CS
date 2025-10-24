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

    @Column(name = "tipo_sala", nullable = false)
    @NotBlank(message = "Tipo de sala é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoSala tipo;

    public ReservaSala(String codigoProfessor, String codigoTurma, TipoSala tipo, String hora, String data) {
        super(codigoProfessor + codigoTurma, hora, data);
        this.codigoProfessor = codigoProfessor;
        this.codigoTurma = codigoTurma;
        this.tipo = tipo;
    }

    public String getCodigo() {
        return super.getCodigo();
    }

    public void setCodigo(String codigoProfessor, String codigoTurma) {
        super.setCodigo(codigoProfessor + codigoTurma);
    }

    public String getHora() {
        return super.getHora();
    }

    public void setHora(String hora) {
        super.setHora(hora);
    }

    public String getData() {
        return super.getData();
    }

    public void setData(String data) {
        super.setData(data);
    }

    public TipoSala getTipo() {
        return tipo;
    }

    public void setTipo(TipoSala tipo) {
        this.tipo = tipo;
    }
}
