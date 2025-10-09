package com.turma.entidade;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    @Test
    void testConstrutorComTresParametros() {
        Reserva turma = new Reserva("95300-04", "Cálculo I", "2LM4NP");

        assertEquals("95300-04", turma.getCodigo());
        assertEquals("Cálculo I", turma.getNome());
        assertEquals("2LM4NP", turma.getHorario());
        assertTrue(turma.getIdEstudante().isEmpty());
    }

    @Test
    void testConstrutorComQuatroParametros() {
        List<String> estudantes = Arrays.asList("EST001", "EST002");

        Reserva turma = new Reserva("4611C-06", "Fundamentos de Programação", "2JK4LM6NP", estudantes);

        assertEquals("4611C-06", turma.getCodigo());
        assertEquals("Fundamentos de Programação", turma.getNome());
        assertEquals("2JK4LM6NP", turma.getHorario());
        assertEquals(2, turma.getIdEstudante().size());
        assertTrue(turma.getIdEstudante().contains("EST001"));
    }

    @Test
    void testConstrutorComListaEstudantesNula() {
        Reserva turma = new Reserva("4611F-04", "POO", "2NP4NP", null);

        assertEquals("4611F-04", turma.getCodigo());
        assertTrue(turma.getIdEstudante().isEmpty());
    }

    @Test
    void testConstrutorVazio() {
        Reserva turma = new Reserva();

        assertNull(turma.getCodigo());
        assertNull(turma.getNome());
        assertNull(turma.getHorario());
        assertTrue(turma.getIdEstudante().isEmpty());
    }

    @Test
    void testSettersEGetters() {
        Reserva turma = new Reserva();

        turma.setCodigo("98708-04");
        turma.setNome("Intel. Artif.");
        turma.setHorario("2NP4NP");

        assertEquals("98708-04", turma.getCodigo());
        assertEquals("Intel. Artif.", turma.getNome());
        assertEquals("2NP4NP", turma.getHorario());
    }

    @Test
    void testAdicionaEstudante() {

        Reserva turma = new Reserva("98708-04", "Intel. Artif.", "2NP4NP");

        turma.adicionaEstudante("EST001");
        turma.adicionaEstudante("EST002");

        assertEquals(2, turma.getIdEstudante().size());
        assertTrue(turma.getIdEstudante().contains("EST001"));
    }

    @Test
    void testAdicionaEstudanteDuplicado() {

        Reserva turma = new Reserva("46504-04", "Construção de Software", "2NP4NP");

        turma.adicionaEstudante("EST001");
        turma.adicionaEstudante("EST001");

        assertEquals(1, turma.getIdEstudante().size());
    }

    @Test
    void testAdicionaEstudanteNulo() {

        Reserva turma = new Reserva("46504-04", "Construção de Software", "2NP4NP");

        turma.adicionaEstudante(null);

        assertTrue(turma.getIdEstudante().isEmpty());
    }

    @Test
    void testAdicionaEstudanteVazio() {

        Reserva turma = new Reserva("46504-04", "Construção de Software", "2NP4NP");

        turma.adicionaEstudante("");

        assertTrue(turma.getIdEstudante().isEmpty());
    }

    @Test
    void testRemoveEstudante() {

        List<String> estudantes = Arrays.asList("EST001", "EST002");
        Reserva turma = new Reserva("46504-04", "Construção de Software", "2NP4NP", estudantes);

        turma.removeEstudante("EST001");

        assertEquals(1, turma.getIdEstudante().size());
        assertFalse(turma.getIdEstudante().contains("EST001"));
        assertTrue(disciplina.getIdEstudante().contains("EST002"));
    }

    @Test
    void testRemoveEstudanteInexistente() {

        List<String> estudantes = Arrays.asList("EST001", "EST002");
        Reserva turma = new Reserva("46504-04", "Construção de Software", "2NP4NP", estudantes);

        turma.removeEstudante("EST999");

        assertEquals(2, turma.getIdEstudante().size());
    }
}