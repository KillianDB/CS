package com.usuario.entidade;

public class TipoUsuario {
    public enum Tipo {
        ADMIN,
        ESTUDANTE,
        PROFESSOR
    }

    private String tipo;

    public TipoUsuario(Tipo tipo) {
        this.tipo = tipo.name();
    }

    public String getTipo() {
        return tipo;
    }
}
