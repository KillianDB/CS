package com.disciplina.entidade;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Disciplina {
    private String codigo;
    private String nome;
    private String horario;
    private List<String> idEstudante;

    public Disciplina(String codigo, String nome, String horario, List<String> idEstudante) {
        this.codigo = codigo;
        this.nome = nome;
        this.horario = horario;
        this.idEstudante = idEstudante;
    }

    public void AdicionaEstudante(String estudante) {
        idEstudante.add(estudante);
    }
}