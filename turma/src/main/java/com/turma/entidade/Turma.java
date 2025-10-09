package com.turma.entidade;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "turmas")
public class Turma {

    @Id
    @NotBlank(message = "Código da turma é obrigatório")
    @Size(min = 3, max = 10, message = "Código deve ter entre 3 e 10 caracteres")
    private String codigo;

    @Column(nullable = false)
    @NotBlank(message = "Horário é obrigatório")
    private String horario;

    @ElementCollection
    @CollectionTable(name = "turma_disciplina", joinColumns = @JoinColumn(name = "turma_codigo"))
    @Column(name = "disciplina_id")
    private String idDisciplina;

    @ElementCollection
    @CollectionTable(name = "turma_aluno", joinColumns = @JoinColumn(name = "turma_codigo"))
    @Column(name = "professor_id")
    private String idProfessor;

    @ElementCollection
    @CollectionTable(name = "turma_estudantes", joinColumns = @JoinColumn(name = "turma_codigo"))
    @Column(name = "estudante_id")
    private List<String> idEstudante = new ArrayList<>();

    public Turma() {
    }

    public Turma(String codigo, String horario) {
        this.codigo = codigo;
        this.horario = horario;
        this.idEstudante = new ArrayList<>();
    }

    public Turma(String codigo, String horario, String idDisciplina, String idProfessor, List<String> idEstudante) {
        this.codigo = codigo;
        this.horario = horario;
        this.idDisciplina = idDisciplina;
        this.idProfessor = idProfessor;
        this.idEstudante = idEstudante != null ? new ArrayList<>(idEstudante) : new ArrayList<>();
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

    public String getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(String idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public String getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(String idProfessor) {
        this.idProfessor = idProfessor;
    }

    public List<String> getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(List<String> idEstudante) {
        this.idEstudante = idEstudante != null ? idEstudante : new ArrayList<>();
    }

    public void adicionaEstudante(String estudanteId) {
        if (estudanteId != null && !estudanteId.trim().isEmpty() && !this.idEstudante.contains(estudanteId)) {
            this.idEstudante.add(estudanteId);
        }
    }

    public void removeEstudante(String estudanteId) {
        this.idEstudante.remove(estudanteId);
    }

    @Override
    public String toString() {
        return "Turma{" +
                "codigo='" + codigo + '\'' +
                ", horario='" + horario + '\'' +
                ", idDisciplina='" + idDisciplina + '\'' +
                ", idProfessor='" + idProfessor + '\'' +
                ", idEstudante=" + idEstudante +
                '}';
    }
}