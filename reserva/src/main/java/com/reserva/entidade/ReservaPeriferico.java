package com.reserva.entidade;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "reservas_perifericos")
public class ReservaPeriferico extends Reserva {

    @Column(name = "codigo_professor", nullable = false)
    @NotBlank(message = "Código do professor é obrigatório")
    private String codigoProfessor;

    @Column(name = "codigo_turma", nullable = false)
    @NotBlank(message = "Código da turma é obrigatório")
    private String codigoTurma;

    @Column(name = "tipo_periferico", nullable = false)
    @NotBlank(message = "Tipo de periférico é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoPeriferico tipo;

    public ReservaPeriferico(String codigoProfessor, String codigoTurma, TipoPeriferico tipo, String hora,
            String data) {
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

    public TipoPeriferico getTipo() {
        return tipo;
    }

    public void setTipo(TipoPeriferico tipo) {
        this.tipo = tipo;
    }
}
