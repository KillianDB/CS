package com.usuario.dto;

public class LoginResponse {
    private String token;
    private String tipoUsuario;
    private String nome;

    public LoginResponse(String token, String tipoUsuario, String nome) {
        this.token = token;
        this.tipoUsuario = tipoUsuario;
        this.nome = nome;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}