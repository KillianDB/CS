package com.disciplina;

import com.disciplina.entidade.Disciplina;
import com.disciplina.repository.DisciplinaRepository;
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
class DisciplinaControllerTest {

    @Mock
    private DisciplinaRepository disciplinaRepository;

    @InjectMocks
    private DisciplinaController disciplinaController;

    @Test
    void testBuscarPorCodigo_QuandoExistir() {
        Disciplina disciplina = new Disciplina("46504-04", "Construção de Software", "2NP4NP");
        when(disciplinaRepository.findById("46504-04")).thenReturn(Optional.of(disciplina));

        ResponseEntity<Disciplina> response = disciplinaController.buscarPorCodigo("46504-04");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("46504-04", response.getBody().getCodigo());
    }

    @Test
    void testBuscarPorCodigo_QuandoNaoExistir() {
        when(disciplinaRepository.findById("INVALIDO")).thenReturn(Optional.empty());

        ResponseEntity<Disciplina> response = disciplinaController.buscarPorCodigo("INVALIDO");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCriarDisciplina_Sucesso() {
        Disciplina disciplina = new Disciplina("46504-04", "Construção de Software", "2NP4NP");
        when(disciplinaRepository.existsByCodigo("46504-04")).thenReturn(false);
        when(disciplinaRepository.save(any(Disciplina.class))).thenReturn(disciplina);

        ResponseEntity<?> response = disciplinaController.criarDisciplina(disciplina);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof Disciplina);
        assertEquals("46504-04", ((Disciplina) response.getBody()).getCodigo());
    }

    @Test
    void testCriarDisciplina_QuandoCodigoJaExiste() {
        Disciplina disciplina = new Disciplina("46504-04", "Construção de Software", "2NP4NP");
        when(disciplinaRepository.existsByCodigo("46504-04")).thenReturn(true);

        ResponseEntity<?> response = disciplinaController.criarDisciplina(disciplina);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof String);
        assertTrue(((String) response.getBody()).contains("Já existe uma disciplina com o código"));
    }

    @Test
    void testAdicionarEstudante_Sucesso() {
        Disciplina disciplina = new Disciplina("46504-04", "Construção de Software", "2NP4NP");
        when(disciplinaRepository.findById("46504-04")).thenReturn(Optional.of(disciplina));
        when(disciplinaRepository.save(any(Disciplina.class))).thenReturn(disciplina);

        ResponseEntity<?> response = disciplinaController.adicionarEstudante("46504-04", "EST001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof String);
        assertTrue(((String) response.getBody()).contains("Estudante adicionado"));
    }

    @Test
    void testAdicionarEstudante_QuandoDisciplinaNaoExiste() {
        when(disciplinaRepository.findById("INVALIDO")).thenReturn(Optional.empty());

        ResponseEntity<?> response = disciplinaController.adicionarEstudante("INVALIDO", "EST001");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testBuscarPorNome() {
        Disciplina disciplina1 = new Disciplina("46504-04", "Construção de Software", "2NP4NP");
        Disciplina disciplina2 = new Disciplina("MAT201", "Construção de Software Avançada", "10:00-12:00");
        List<Disciplina> disciplinas = Arrays.asList(disciplina1, disciplina2);

        when(disciplinaRepository.findByNomeContainingIgnoreCase("Construção de Software")).thenReturn(disciplinas);

        ResponseEntity<List<Disciplina>> response = disciplinaController.buscarPorNome("Construção de Software");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testBuscarHorarioDaDisciplina() {
        when(disciplinaRepository.findHorarioByCodigo("46504-04")).thenReturn("2NP4NP");

        ResponseEntity<String> response = disciplinaController.buscarHorarioDaDisciplina("46504-04");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("2NP4NP", response.getBody());
    }

    @Test
    void testHealth() {
        ResponseEntity<String> response = disciplinaController.health();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Disciplina Service is running"));
    }

    @Test
    void testCriarDisciplina_Excecao() {
        Disciplina disciplina = new Disciplina("46504-04", "Construção de Software", "2NP4NP");
        when(disciplinaRepository.existsByCodigo("46504-04")).thenReturn(false);
        when(disciplinaRepository.save(any(Disciplina.class))).thenThrow(new RuntimeException("Erro de banco"));

        ResponseEntity<?> response = disciplinaController.criarDisciplina(disciplina);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof String);
        assertTrue(((String) response.getBody()).contains("Erro ao criar disciplina"));
    }

    @Test
    void testAdicionarEstudante_Excecao() {
        Disciplina disciplina = new Disciplina("46504-04", "Construção de Software", "2NP4NP");
        when(disciplinaRepository.findById("46504-04")).thenReturn(Optional.of(disciplina));
        when(disciplinaRepository.save(any(Disciplina.class))).thenThrow(new RuntimeException("Erro de banco"));

        ResponseEntity<?> response = disciplinaController.adicionarEstudante("46504-04", "EST001");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof String);
        assertTrue(((String) response.getBody()).contains("Erro ao adicionar estudante"));
    }
}