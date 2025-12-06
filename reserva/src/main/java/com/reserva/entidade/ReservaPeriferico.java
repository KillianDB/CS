package com.reserva.entidade;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class ReservaPeriferico extends Reserva {

    @Column(name = "codigo_professor", nullable = false)
    @NotBlank(message = "Código do professor é obrigatório")
    private String codigoProfessor;

    @Column(name = "codigo_turma", nullable = false)
    @NotBlank(message = "Código da turma é obrigatório")
    private String codigoTurma;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_periferico", referencedColumnName = "codigo")
    private Periferico periferico;

    @Column(name = "tipo_periferico", nullable = false)
    @NotBlank(message = "Tipo de periferico é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoPeriferico tipo;

    public ReservaPeriferico(String codigoProfessor, String codigoTurma, String hora, String data,
            Periferico periferico) {
        super(codigoProfessor + codigoTurma + data, hora, data);
        this.codigoProfessor = codigoProfessor;
        this.codigoTurma = codigoTurma;
        this.periferico = periferico;
        this.tipo = periferico.getTipo();
    }

    public ReservaPeriferico() {
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

    public TipoPeriferico getTipo() {
        return tipo;
    }

    public void setTipo(TipoPeriferico tipo) {
        this.tipo = tipo;
    }

    public Periferico getPeriferico() {
        return periferico;
    }

    public void setPeriferico(Periferico periferico) {
        this.periferico = periferico;
    }
}
