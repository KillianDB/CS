package com.disciplina.entidade;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    @Test
    void testConstrutorComTresParametros() {
        Disciplina disciplina = new Disciplina("95300-04", "Cálculo I", "2LM4NP");

        assertEquals("95300-04", disciplina.getCodigo());
        assertEquals("Cálculo I", disciplina.getNome());
        assertEquals("2LM4NP", disciplina.getHorario());
        assertTrue(disciplina.getIdEstudante().isEmpty());
    }

    @Test
    void testConstrutorComQuatroParametros() {
        List<String> estudantes = Arrays.asList("EST001", "EST002");

        Disciplina disciplina = new Disciplina("4611C-06", "Fundamentos de Programação", "2JK4LM6NP", estudantes);

        assertEquals("4611C-06", disciplina.getCodigo());
        assertEquals("Fundamentos de Programação", disciplina.getNome());
        assertEquals("2JK4LM6NP", disciplina.getHorario());
        assertEquals(2, disciplina.getIdEstudante().size());
        assertTrue(disciplina.getIdEstudante().contains("EST001"));
    }

    @Test
    void testConstrutorComListaEstudantesNula() {
        Disciplina disciplina = new Disciplina("4611F-04", "POO", "2NP4NP", null);

        assertEquals("4611F-04", disciplina.getCodigo());
        assertTrue(disciplina.getIdEstudante().isEmpty());
    }

    @Test
    void testConstrutorVazio() {
        Disciplina disciplina = new Disciplina();

        assertNull(disciplina.getCodigo());
        assertNull(disciplina.getNome());
        assertNull(disciplina.getHorario());
        assertTrue(disciplina.getIdEstudante().isEmpty());
    }

    @Test
    void testSettersEGetters() {
        Disciplina disciplina = new Disciplina();

        disciplina.setCodigo("98708-04");
        disciplina.setNome("Intel. Artif.");
        disciplina.setHorario("2NP4NP");

        assertEquals("98708-04", disciplina.getCodigo());
        assertEquals("Intel. Artif.", disciplina.getNome());
        assertEquals("2NP4NP", disciplina.getHorario());
    }

    @Test
    void testAdicionaEstudante() {

        Disciplina disciplina = new Disciplina("98708-04", "Intel. Artif.", "2NP4NP");

        disciplina.adicionaEstudante("EST001");
        disciplina.adicionaEstudante("EST002");

        assertEquals(2, disciplina.getIdEstudante().size());
        assertTrue(disciplina.getIdEstudante().contains("EST001"));
    }

    @Test
    void testAdicionaEstudanteDuplicado() {

        Disciplina disciplina = new Disciplina("46504-04", "Construção de Software", "2NP4NP");

        disciplina.adicionaEstudante("EST001");
        disciplina.adicionaEstudante("EST001");

        assertEquals(1, disciplina.getIdEstudante().size());
    }

    @Test
    void testAdicionaEstudanteNulo() {

        Disciplina disciplina = new Disciplina("46504-04", "Construção de Software", "2NP4NP");

        disciplina.adicionaEstudante(null);

        assertTrue(disciplina.getIdEstudante().isEmpty());
    }

    @Test
    void testAdicionaEstudanteVazio() {

        Disciplina disciplina = new Disciplina("46504-04", "Construção de Software", "2NP4NP");

        disciplina.adicionaEstudante("");

        assertTrue(disciplina.getIdEstudante().isEmpty());
    }

    @Test
    void testRemoveEstudante() {

        List<String> estudantes = Arrays.asList("EST001", "EST002");
        Disciplina disciplina = new Disciplina("46504-04", "Construção de Software", "2NP4NP", estudantes);

        disciplina.removeEstudante("EST001");

        assertEquals(1, disciplina.getIdEstudante().size());
        assertFalse(disciplina.getIdEstudante().contains("EST001"));
        assertTrue(disciplina.getIdEstudante().contains("EST002"));
    }

    @Test
    void testRemoveEstudanteInexistente() {

        List<String> estudantes = Arrays.asList("EST001", "EST002");
        Disciplina disciplina = new Disciplina("46504-04", "Construção de Software", "2NP4NP", estudantes);

        disciplina.removeEstudante("EST999");

        assertEquals(2, disciplina.getIdEstudante().size());
    }
}