package com.reserva.entidade;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "salas")
public class Sala {

    @Id
    @NotBlank(message = "Código da sala é obrigatório")
    @Size(min = 3, max = 10, message = "Código deve ter entre 3 e 10 caracteres")
    private String codigo;

    @Column(nullable = false)
    @NotBlank(message = "Tipo de sala é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoSala tipo;

}
