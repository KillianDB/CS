package com.estudante.entidade;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstudanteTest {

    @Test
    void testConstrutorComNome() {
        Estudante estudante = new Estudante("João");
        assertEquals("João", estudante.getNome());
        assertNull(estudante.getMatricula());
        assertNull(estudante.getEmail());
    }

    @Test
    void testConstrutorComNomeEMatricula() {
        Estudante estudante = new Estudante("Maria", "12345");
        assertEquals("Maria", estudante.getNome());
        assertEquals("12345", estudante.getMatricula());
        assertNull(estudante.getEmail());
    }

    @Test
    void testConstrutorCompleto() {
        Estudante estudante = new Estudante("Ana", "67890", "ana@email.com");
        assertEquals("Ana", estudante.getNome());
        assertEquals("67890", estudante.getMatricula());
        assertEquals("ana@email.com", estudante.getEmail());
    }

    @Test
    void testGettersSetters() {
        Estudante estudante = new Estudante();
        estudante.setNome("Pedro");
        estudante.setMatricula("11223");
        estudante.setEmail("pedro@email.com");

        assertEquals("Pedro", estudante.getNome());
        assertEquals("11223", estudante.getMatricula());
        assertEquals("pedro@email.com", estudante.getEmail());
    }

    @Test
    void testToString() {
        Estudante estudante = new Estudante("Lucas", "44556", "lucas@email.com");
        String toString = estudante.toString();
        assertTrue(toString.contains("Lucas"));
        assertTrue(toString.contains("44556"));
        assertTrue(toString.contains("lucas@email.com"));
    }
}
