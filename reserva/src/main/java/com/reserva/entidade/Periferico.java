package com.reserva.entidade;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "perifericos")
public class Periferico {

    @Id
    @NotBlank(message = "Código do periférico é obrigatório")
    @Size(min = 3, max = 10, message = "Código deve ter entre 3 e 10 caracteres")
    private String codigo;

    @Column(nullable = false)
    @NotBlank(message = "Tipo de periférico é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoPeriferico tipo;

}
