package com.reserva.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Service
public class AuthService {

    private final RestTemplate rest;
    private final String baseUrl;

    public AuthService(RestTemplate rest,
            @Value("${usuario.service.url:http://localhost:8082}") String usuarioBaseUrl) {
        this.rest = rest;
        this.baseUrl = usuarioBaseUrl;
    }

    public boolean isProfessor(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank())
            return false;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorizationHeader);
            HttpEntity<Void> req = new HttpEntity<>(headers);
            ResponseEntity<UsuarioDTO> resp = rest.exchange(baseUrl + "/usuarios/me",
                    org.springframework.http.HttpMethod.GET,
                    req, UsuarioDTO.class);
            UsuarioDTO user = resp.getBody();
            return user != null && "PROFESSOR".equalsIgnoreCase(user.getTipoUsuario());
        } catch (Exception ex) {
            return false;
        }
    }
}