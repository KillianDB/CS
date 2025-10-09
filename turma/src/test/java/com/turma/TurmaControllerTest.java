package com.turma;

import com.turma.entidade.Turma;
import com.turma.repository.TurmaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TurmaControllerTest {

    @Mock
    private TurmaRepository turmaRepository;

    @InjectMocks
    private TurmaController turmaController;

    @Test
    void testBuscarPorCodigo_QuandoExistir() {
        Turma turma = new Turma("46504-04", "Construção de Software", "2NP4NP");
        when(turmaRepository.findById("46504-04")).thenReturn(Optional.of(turma));

        ResponseEntity<Turma> response = turmaController.buscarPorCodigo("46504-04");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("46504-04", response.getBody().getCodigo());
    }

    @Test
    void testBuscarPorCodigo_QuandoNaoExistir() {
        when(turmaRepository.findById("INVALIDO")).thenReturn(Optional.empty());

        ResponseEntity<Turma> response = turmaController.buscarPorCodigo("INVALIDO");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCriarTurma_Sucesso() {
        Turma turma = new Turma("46504-04", "Construção de Software", "2NP4NP");
        when(turmaRepository.existsByCodigo("46504-04")).thenReturn(false);
        when(turmaRepository.save(any(Turma.class))).thenReturn(turma);

        ResponseEntity<?> response = turmaController.criarTurma(turma);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof Turma);
        assertEquals("46504-04", ((Turma) response.getBody()).getCodigo());
    }

    @Test
    void testCriarTurma_QuandoCodigoJaExiste() {
        Turma turma = new Turma("46504-04", "Construção de Software", "2NP4NP");
        when(turmaRepository.existsByCodigo("46504-04")).thenReturn(true);

        ResponseEntity<?> response = turmaController.criarTurma(turma);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof String);
        assertTrue(((String) response.getBody()).contains("Já existe uma turma com o código"));
    }

    @Test
    void testAdicionarEstudante_Sucesso() {
        Turma turma = new Turma("46504-04", "Construção de Software", "2NP4NP");
        when(turmaRepository.findById("46504-04")).thenReturn(Optional.of(turma));
        when(turmaRepository.save(any(Turma.class))).thenReturn(turma);

        ResponseEntity<?> response = turmaController.adicionarEstudante("46504-04", "EST001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof String);
        assertTrue(((String) response.getBody()).contains("Estudante adicionado"));
    }

    @Test
    void testAdicionarEstudante_QuandoTurmaNaoExiste() {
        when(turmaRepository.findById("INVALIDO")).thenReturn(Optional.empty());

        ResponseEntity<?> response = turmaController.adicionarEstudante("INVALIDO", "EST001");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testBuscarPorNome() {
        Turma turma1 = new Turma("46504-04", "Construção de Software", "2NP4NP");
        Turma turma2 = new Turma("MAT201", "Construção de Software Avançada", "10:00-12:00");
        List<Turma> turmas = Arrays.asList(turma1, turma2);

        when(turmaRepository.findByNomeContainingIgnoreCase("Construção de Software")).thenReturn(turmas);

        ResponseEntity<List<Turma>> response = turmaController.buscarPorNome("Construção de Software");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testBuscarHorarioDaTurma() {
        when(turmaRepository.findHorarioByCodigo("46504-04")).thenReturn("2NP4NP");

        ResponseEntity<String> response = turmaController.buscarHorarioDaTurma("46504-04");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("2NP4NP", response.getBody());
    }

    @Test
    void testHealth() {
        ResponseEntity<String> response = turmaController.health();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Turma Service is running"));
    }

    @Test
    void testCriarTurma_Excecao() {
        Turma turma = new Turma("46504-04", "Construção de Software", "2NP4NP");
        when(turmaRepository.existsByCodigo("46504-04")).thenReturn(false);
        when(turmaRepository.save(any(Turma.class))).thenThrow(new RuntimeException("Erro de banco"));

        ResponseEntity<?> response = turmaController.criarTurma(turma);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof String);
        assertTrue(((String) response.getBody()).contains("Erro ao criar turma"));
    }

    @Test
    void testAdicionarEstudante_Excecao() {
        Turma turma = new Turma("46504-04", "Construção de Software", "2NP4NP");
        when(turmaRepository.findById("46504-04")).thenReturn(Optional.of(turma));
        when(turmaRepository.save(any(Turma.class))).thenThrow(new RuntimeException("Erro de banco"));

        ResponseEntity<?> response = turmaController.adicionarEstudante("46504-04", "EST001");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof String);
        assertTrue(((String) response.getBody()).contains("Erro ao adicionar estudante"));
    }
}