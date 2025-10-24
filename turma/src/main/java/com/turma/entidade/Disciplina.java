package com.turma.entidade;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "disciplinas")
public class Disciplina {

    @Id
    @NotBlank(message = "Código da disciplina é obrigatório")
    @Size(min = 3, max = 10, message = "Código deve ter entre 3 e 10 caracteres")
    private String codigo;

    @Column(nullable = false)
    @NotBlank(message = "Nome da disciplina é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @ElementCollection
    @CollectionTable(name = "turma_disciplina", joinColumns = @JoinColumn(name = "disciplina_codigo"))
    @Column(name = "turma_id")
    private List<String> idTurmas = new ArrayList<>();

    public Disciplina() {
    }

    public Disciplina(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
        this.idTurmas = new ArrayList<>();
    }

    public Disciplina(String codigo, String nome, List<String> idTurmas) {
        this.codigo = codigo;
        this.nome = nome;
        this.idTurmas = idTurmas != null ? new ArrayList<>(idTurmas) : new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getIdTurmas() {
        return idTurmas;
    }

    public void setIdTurmas(List<String> idTurmas) {
        this.idTurmas = idTurmas != null ? idTurmas : new ArrayList<>();
    }

    public void adicionaTurma(String turmaId) {
        if (turmaId != null && !turmaId.trim().isEmpty() && !this.idTurmas.contains(turmaId)) {
            this.idTurmas.add(turmaId);
        }
    }

    public void removeTurma(String turmaId) {
        this.idTurmas.remove(turmaId);
    }

    @Override
    public String toString() {
        return "Disciplina{" +
                "codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                ", idTurmas=" + idTurmas +
                '}';
    }
}