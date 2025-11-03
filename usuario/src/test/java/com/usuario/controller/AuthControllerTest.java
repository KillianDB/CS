package com.usuario.controller;

import com.usuario.dto.LoginRequest;
import com.usuario.dto.LoginResponse;
import com.usuario.entidade.TipoUsuario;
import com.usuario.entidade.Usuario;
import com.usuario.repository.UsuarioRepository;
import com.usuario.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        usuarioRepository.deleteAll();
    }

    @Test
    void deveAutenticarUsuarioComSucesso() {
        // Criar usuário
        Usuario usuario = new Usuario();
        usuario.setMatricula("12345");
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        usuario.setTipoUsuario(TipoUsuario.ALUNO);
        usuarioRepository.save(usuario);

        // Criar request de login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMatricula("12345");
        loginRequest.setSenha("123456");

        // Fazer requisição de login
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
            baseUrl + "/auth/login",
            loginRequest,
            LoginResponse.class
        );

        // Verificações
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getToken());
        assertEquals("ALUNO", response.getBody().getTipoUsuario());
        assertEquals("Teste", response.getBody().getNome());
    }

    @Test
    void deveRetornarErroQuandoCredenciaisInvalidas() {
        // Criar request de login com credenciais inválidas
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMatricula("invalido");
        loginRequest.setSenha("invalido");

        // Fazer requisição de login
        ResponseEntity<String> response = restTemplate.postForEntity(
            baseUrl + "/auth/login",
            loginRequest,
            String.class
        );

        // Verificações
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Usuário não encontrado"));
    }
}