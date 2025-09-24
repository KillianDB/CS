package com.disciplina.entidade;

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
    
    @Column(nullable = false)
    @NotBlank(message = "Horário é obrigatório")
    private String horario;
    
    @ElementCollection
    @CollectionTable(name = "disciplina_estudantes", joinColumns = @JoinColumn(name = "disciplina_codigo"))
    @Column(name = "estudante_id")
    private List<String> idEstudante = new ArrayList<>();

    public Disciplina() {}

    public Disciplina(String codigo, String nome, String horario) {
        this.codigo = codigo;
        this.nome = nome;
        this.horario = horario;
        this.idEstudante = new ArrayList<>();
    }

    public Disciplina(String codigo, String nome, String horario, List<String> idEstudante) {
        this.codigo = codigo;
        this.nome = nome;
        this.horario = horario;
        this.idEstudante = idEstudante != null ? new ArrayList<>(idEstudante) : new ArrayList<>();
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

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
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
        return "Disciplina{" +
                "codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                ", horario='" + horario + '\'' +
                ", idEstudante=" + idEstudante +
                '}';
    }
}