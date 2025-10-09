package com.usuario.entidade;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testConstrutorComNome() {
        Usuario usuario = new Usuario("João");
        assertEquals("João", usuario.getNome());
        assertNull(usuario.getMatricula());
        assertNull(usuario.getEmail());
        assertNull(usuario.getTipoUsuario());
    }

    @Test
    void testConstrutorCompleto() {
        Usuario usuario = new Usuario("Ana", "67890", "ana@email.com", "senha123",
                new TipoUsuario(TipoUsuario.Tipo.ESTUDANTE));
        assertEquals("Ana", usuario.getNome());
        assertEquals("67890", usuario.getMatricula());
        assertEquals("ana@email.com", usuario.getEmail());
        assertEquals("ESTUDANTE", usuario.getTipoUsuario());
    }

    @Test
    void testGettersSetters() {
        Usuario usuario = new Usuario();
        usuario.setNome("Pedro");
        usuario.setMatricula("11223");
        usuario.setEmail("pedro@email.com");

        assertEquals("Pedro", usuario.getNome());
        assertEquals("11223", usuario.getMatricula());
        assertEquals("pedro@email.com", usuario.getEmail());
    }

    @Test
    void testToString() {
        Usuario usuario = new Usuario("Lucas", "44556", "lucas@email.com", "senha123",
                new TipoUsuario(TipoUsuario.Tipo.PROFESSOR));
        String toString = usuario.toString();
        assertTrue(toString.contains("Lucas"));
        assertTrue(toString.contains("44556"));
        assertTrue(toString.contains("lucas@email.com"));
        assertTrue(toString.contains("PROFESSOR"));
    }
}
