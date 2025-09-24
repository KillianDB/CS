package com.estudante;

import com.estudante.entidade.Estudante;
import com.estudante.repository.EstudanteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstudanteControllerTest {

    @Mock
    private EstudanteRepository estudanteRepository;

    @InjectMocks
    private EstudanteController estudanteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarEstudanteQuandoMatriculaExiste() {
        Estudante estudante = new Estudante("Maria", "12345", "maria@email.com");
        when(estudanteRepository.findByMatricula("12345"))
                .thenReturn(Optional.of(estudante));

        ResponseEntity<Estudante> resposta = estudanteController.buscarPorMatricula("12345");

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals("Maria", resposta.getBody().getNome());
    }

    @Test
    void deveRetornarNotFoundQuandoMatriculaNaoExiste() {
        when(estudanteRepository.findByMatricula("99999"))
                .thenReturn(Optional.empty());

        ResponseEntity<Estudante> resposta = estudanteController.buscarPorMatricula("99999");

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        assertNull(resposta.getBody());
    }

    @Test
    void deveRetornarListaDeEstudantesQuandoBuscarPorNome() {
        Estudante estudante1 = new Estudante("Lucas", "11111", "lucas@email.com");
        Estudante estudante2 = new Estudante("Lucas Silva", "22222", "lucass@email.com");

        when(estudanteRepository.findByNomeContainingIgnoreCase("lucas"))
                .thenReturn(List.of(estudante1, estudante2));

        ResponseEntity<List<Estudante>> resposta = estudanteController.buscarPorNome("lucas");

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(2, resposta.getBody().size());
    }

    @Test
    void deveCriarEstudanteQuandoDadosValidos() {
        Estudante estudante = new Estudante("João", "33333", "joao@email.com");

        when(estudanteRepository.existsByMatricula("33333")).thenReturn(false);
        when(estudanteRepository.existsByEmail("joao@email.com")).thenReturn(false);
        when(estudanteRepository.save(estudante)).thenReturn(estudante);

        ResponseEntity<?> resposta = estudanteController.criarEstudante(estudante);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertTrue(resposta.getBody() instanceof Estudante);
        assertEquals("João", ((Estudante) resposta.getBody()).getNome());
    }

    @Test
    void deveRetornarBadRequestQuandoMatriculaDuplicada() {
        Estudante estudante = new Estudante("Ana", "44444", "ana@email.com");

        when(estudanteRepository.existsByMatricula("44444")).thenReturn(true);

        ResponseEntity<?> resposta = estudanteController.criarEstudante(estudante);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertTrue(resposta.getBody().toString().contains("Já existe um estudante com a matrícula"));
    }

    @Test
    void deveRetornarBadRequestQuandoEmailDuplicado() {
        Estudante estudante = new Estudante("Pedro", "55555", "pedro@email.com");

        when(estudanteRepository.existsByMatricula("55555")).thenReturn(false);
        when(estudanteRepository.existsByEmail("pedro@email.com")).thenReturn(true);

        ResponseEntity<?> resposta = estudanteController.criarEstudante(estudante);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertTrue(resposta.getBody().toString().contains("Já existe um estudante com o email"));
    }
}
