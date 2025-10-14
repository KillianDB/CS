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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotBlank(message = "Código da turma é obrigatório")
    @Size(min = 3, max = 10, message = "Código deve ter entre 3 e 10 caracteres")
    private String codigo;

    @Column(nullable = false)
    @NotBlank(message = "Horário é obrigatório")
    private String horario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @Column(name = "professor_id")
    @NotBlank(message = "Professor é obrigatório")
    private String idProfessor;

    @Column(name = "estudante_id")
    private List<String> idEstudante = new ArrayList<>();

    public Turma() {
    }

    public Turma(String codigo, String horario) {
        this.codigo = codigo;
        this.horario = horario;
        this.idEstudante = new ArrayList<>();
    }

    public Turma(String codigo, String horario, Disciplina disciplina, String idProfessor) {
        this.codigo = codigo;
        this.horario = horario;
        this.disciplina = disciplina;
        this.idProfessor = idProfessor;
        this.idEstudante = new ArrayList<>();
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

    public String getDisciplina() {
        return disciplina.getCodigo();
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
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
                ", disciplina='" + disciplina.getCodigo() + '\'' +
                ", idProfessor='" + idProfessor + '\'' +
                ", idEstudante=" + idEstudante +
                '}';
    }
}