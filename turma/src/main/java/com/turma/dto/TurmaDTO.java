package com.turma.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TurmaDTO {

    @NotBlank(message = "Código da turma é obrigatório")
    @Size(min = 3, max = 10, message = "Código deve ter entre 3 e 10 caracteres")
    private String codigo;

    @NotBlank(message = "Horário é obrigatório")
    private String horario;

    @NotBlank(message = "Disciplina é obrigatória")
    private String disciplinaCodigo; // ou disciplinaId

    @NotBlank(message = "Professor é obrigatório")
    private String idProfessor;


    public TurmaDTO() {}

    public TurmaDTO(String codigo, String horario, String disciplinaCodigo, String idProfessor) {
        this.codigo = codigo;
        this.horario = horario;
        this.disciplinaCodigo = disciplinaCodigo;
        this.idProfessor = idProfessor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDisciplinaCodigo() {
        return disciplinaCodigo;
    }

    public void setDisciplinaCodigo(String disciplinaCodigo) {
        this.disciplinaCodigo = disciplinaCodigo;
    }

    public String getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(String idProfessor) {
        this.idProfessor = idProfessor;
    }
}