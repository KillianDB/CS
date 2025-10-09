package com.usuario;

import com.usuario.entidade.Usuario;
import com.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.usuario.entidade.TipoUsuario.Tipo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarUsuarioQuandoMatriculaExiste() {
        Usuario usuario = new Usuario("Maria", "12345", "maria@email.com", "senha123",
                new com.usuario.entidade.TipoUsuario(Tipo.ADMIN));
        when(usuarioRepository.findByMatricula("12345"))
                .thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> resposta = usuarioController.buscarPorMatricula("12345");

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals("Maria", resposta.getBody().getNome());
    }

    @Test
    void deveRetornarNotFoundQuandoMatriculaNaoExiste() {
        when(usuarioRepository.findByMatricula("99999"))
                .thenReturn(Optional.empty());

        ResponseEntity<Usuario> resposta = usuarioController.buscarPorMatricula("99999");

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        assertNull(resposta.getBody());
    }

    @Test
    void deveRetornarListaDeUsuariosQuandoBuscarPorNome() {
        Usuario usuario1 = new Usuario("Lucas", "11111", "lucas@email.com", "senha123",
                new com.usuario.entidade.TipoUsuario(Tipo.PROFESSOR));
        Usuario usuario2 = new Usuario("Lucas Silva", "22222", "lucass@email.com", "senha123",
                new com.usuario.entidade.TipoUsuario(Tipo.ESTUDANTE));

        when(usuarioRepository.findByNomeContainingIgnoreCase("lucas"))
                .thenReturn(List.of(usuario1, usuario2));

        ResponseEntity<List<Usuario>> resposta = usuarioController.buscarPorNome("lucas");

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(2, resposta.getBody().size());
    }

    @Test
    void deveCriarUsuarioQuandoDadosValidos() {
        Usuario usuario = new Usuario("João", "33333", "joao@email.com", "senha123",
                new com.usuario.entidade.TipoUsuario(Tipo.ESTUDANTE));

        when(usuarioRepository.existsByMatricula("33333")).thenReturn(false);
        when(usuarioRepository.existsByEmail("joao@email.com")).thenReturn(false);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        ResponseEntity<?> resposta = usuarioController.criarUsuario(usuario);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertTrue(resposta.getBody() instanceof Usuario);
        assertEquals("João", ((Usuario) resposta.getBody()).getNome());
    }

    @Test
    void deveRetornarBadRequestQuandoMatriculaDuplicada() {
        Usuario usuario = new Usuario("Ana", "44444", "ana@email.com", "senha123",
                new com.usuario.entidade.TipoUsuario(Tipo.ADMIN));

        when(usuarioRepository.existsByMatricula("44444")).thenReturn(true);

        ResponseEntity<?> resposta = usuarioController.criarUsuario(usuario);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertTrue(resposta.getBody().toString().contains("Já existe um usuário com a matrícula"));
    }

    @Test
    void deveRetornarBadRequestQuandoEmailDuplicado() {
        Usuario usuario = new Usuario("Pedro", "55555", "pedro@email.com", "senha123",
                new com.usuario.entidade.TipoUsuario(Tipo.PROFESSOR));

        when(usuarioRepository.existsByMatricula("55555")).thenReturn(false);
        when(usuarioRepository.existsByEmail("pedro@email.com")).thenReturn(true);

        ResponseEntity<?> resposta = usuarioController.criarUsuario(usuario);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertTrue(resposta.getBody().toString().contains("Já existe um usuário com o email"));
    }
}
