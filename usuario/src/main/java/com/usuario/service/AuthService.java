package com.usuario.service;

import com.usuario.dto.LoginRequest;
import com.usuario.dto.LoginResponse;
import com.usuario.entidade.Usuario;
import com.usuario.repository.UsuarioRepository;
import com.usuario.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponse autenticar(LoginRequest loginRequest) {
        Usuario usuario = usuarioRepository.findByMatricula(loginRequest.getMatricula())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        String jwt = tokenProvider.generateToken(usuario.getMatricula());
        return new LoginResponse(jwt, usuario.getTipoUsuario(), usuario.getNome());
    }
}