package com.example.restapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Date;

public class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario();
        usuario.setNombre("Juan");
        usuario.setApellidos("Pérez");
        usuario.setEmail("juan.perez@example.com");
        usuario.setPassword("password123");
        usuario.setTelefono("123456789");
        usuario.setDni(12345678L);
        usuario.setFechaNacimiento(Date.valueOf("1990-01-01"));
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);
        usuario.setTipoPago(TipoPago.Visa);
    }

    // Pruebas para los getters
    @Test
    public void testGetNombre() {
        assertEquals("Juan", usuario.getNombre());
    }

    @Test
    public void testGetApellidos() {
        assertEquals("Pérez", usuario.getApellidos());
    }

    @Test
    public void testGetEmail() {
        assertEquals("juan.perez@example.com", usuario.getEmail());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password123", usuario.getPassword());
    }

    @Test
    public void testGetTelefono() {
        assertEquals("123456789", usuario.getTelefono());
    }

    @Test
    public void testGetDni() {
        assertEquals(Long.valueOf(12345678), usuario.getDni());
    }

    @Test
    public void testGetFechaNacimiento() {
        assertEquals(Date.valueOf("1990-01-01"), usuario.getFechaNacimiento());
    }

    @Test
    public void testGetTipoUsuario() {
        assertEquals(TipoUsuario.CLIENTE, usuario.getTipoUsuario());
    }

    @Test
    public void testGetTipoPago() {
        assertEquals(TipoPago.Visa, usuario.getTipoPago());
    }

    // Pruebas para los setters
    @Test
    public void testSetNombre() {
        usuario.setNombre("Carlos");
        assertEquals("Carlos", usuario.getNombre());
    }

    @Test
    public void testSetApellidos() {
        usuario.setApellidos("Gómez");
        assertEquals("Gómez", usuario.getApellidos());
    }

    @Test
    public void testSetEmail() {
        usuario.setEmail("carlos.gomez@example.com");
        assertEquals("carlos.gomez@example.com", usuario.getEmail());
    }

    @Test
    public void testSetPassword() {
        usuario.setPassword("newpassword");
        assertEquals("newpassword", usuario.getPassword());
    }

    @Test
    public void testSetTelefono() {
        usuario.setTelefono("987654321");
        assertEquals("987654321", usuario.getTelefono());
    }

    @Test
    public void testSetDni() {
        usuario.setDni(87654321L);
        assertEquals(Long.valueOf(87654321), usuario.getDni());
    }

    @Test
    public void testSetFechaNacimientoWithMockito() {
        try (MockedStatic<Date> dateMock = Mockito.mockStatic(Date.class)) {
            dateMock.when(() -> Date.valueOf("2000-01-01")).thenReturn(new Date(0));

            usuario.setFechaNacimiento(Date.valueOf("2000-01-01"));
            assertEquals(new Date(0), usuario.getFechaNacimiento());
        }
    }

    @Test
    public void testSetTipoUsuario() {
        usuario.setTipoUsuario(TipoUsuario.ADMIN);
        assertEquals(TipoUsuario.ADMIN, usuario.getTipoUsuario());
    }

    @Test
    public void testSetTipoPago() {
        usuario.setTipoPago(TipoPago.Mastercard);
        assertEquals(TipoPago.Mastercard, usuario.getTipoPago());
    }

    @Test
    public void testToString() {
        String expected = "Usuario{nombre='Juan', apellidos='Pérez', email='juan.perez@example.com', telefono='123456789', dni=12345678, fechaNacimiento=1990-01-01, tipoPago=Visa}";
        assertEquals(expected, usuario.toString());
    }

    @Test
    public void testConstructorWithEmailAndTipoUsuario() {
        Usuario usuarioParam = new Usuario("carlos.gomez@example.com", TipoUsuario.ADMIN);

        assertNotNull(usuarioParam);
        assertEquals("carlos.gomez@example.com", usuarioParam.getEmail());
        assertEquals(TipoUsuario.ADMIN, usuarioParam.getTipoUsuario());
    }

    @Test
    public void testConstructorWithParameters() {
        Usuario usuarioParam = new Usuario(
            "Carlos",
            "Gómez",
            "carlos.gomez@example.com",
            "newpassword",
            "987654321",
            87654321L,
            Date.valueOf("1990-01-01"),
            TipoUsuario.ADMIN
        );

        assertNotNull(usuarioParam);
        assertEquals("Carlos", usuarioParam.getNombre());
        assertEquals("Gómez", usuarioParam.getApellidos());
        assertEquals("carlos.gomez@example.com", usuarioParam.getEmail());
        assertEquals("newpassword", usuarioParam.getPassword());
        assertEquals("987654321", usuarioParam.getTelefono());
        assertEquals(Long.valueOf(87654321), usuarioParam.getDni());
        assertEquals(Date.valueOf("1990-01-01"), usuarioParam.getFechaNacimiento());
        assertEquals(TipoUsuario.ADMIN, usuarioParam.getTipoUsuario());
    }
}