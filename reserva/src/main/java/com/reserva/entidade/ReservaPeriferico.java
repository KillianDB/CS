package com.reserva.entidade;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "reservas_perifericos")
public class ReservaPeriferico extends Reserva {

    @Id
    @NotBlank(message = "Código do periférico é obrigatório")
    @Size(min = 3, max = 30, message = "Código deve ter entre 3 e 30 caracteres")
    private String codigo;

    @Column(nullable = false)
    @NotBlank(message = "Tipo de periférico é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoPeriferico tipo;

    public ReservaPeriferico(String codigoProfessor, String codigoSala, String codigoPeriferico, TipoPeriferico tipo) {
        super();
        this.codigo = codigoProfessor + codigoSala + codigoPeriferico;
        this.tipo = tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigoProfessor, String codigoSala, String codigoPeriferico) {
        this.codigo = codigoProfessor + codigoSala + codigoPeriferico;
    }

    public TipoPeriferico getTipo() {
        return tipo;
    }

    public void setTipo(TipoPeriferico tipo) {
        this.tipo = tipo;
    }
}
