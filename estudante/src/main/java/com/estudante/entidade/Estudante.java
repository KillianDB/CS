package com.estudante.entidade;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "estudantes")
public class Estudante {
    
    @Id
    @Column(unique = true)
    @Size(min = 5, max = 20, message = "Matrícula deve ter entre 5 e 20 caracteres")
    private String matricula;
    
    @Column(nullable = false)
    @NotBlank(message = "Nome do estudante é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;
    
    @Column
    private String email;

    public Estudante() {}

    public Estudante(String nome) {
        this.nome = nome;
    }

    public Estudante(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
    }

    public Estudante(String nome, String matricula, String email) {
        this.nome = nome;
        this.matricula = matricula;
        this.email = email;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Estudante{" +
                "nome='" + nome + '\'' +
                ", matricula='" + matricula + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}