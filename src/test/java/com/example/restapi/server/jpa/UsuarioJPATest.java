package com.example.restapi.server.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.restapi.model.TipoPago;
import com.example.restapi.model.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class UsuarioJPATest {

    private static ZonedDateTime timestamp = ZonedDateTime.of(2025, 4, 23, 19, 15, 22, 0, ZoneId.of("Europe/Madrid"));
    private UsuarioJPA usuario;

    @BeforeEach
    public void setUp() {
        usuario = new UsuarioJPA(
            12345678L,
            "Juan",
            "Pérez",
            "juan.perez@example.com",
            "password123",
            "123456789",
            Date.valueOf("1990-01-01"),
            TipoPago.Visa,
            TipoUsuario.CLIENTE
        );
    }

    @Test
    public void testGetDni() {
        assertEquals(12345678L, usuario.getDni());
    }

    @Test
    public void testSetDni() {
        usuario.setDni(87654321L);
        assertEquals(87654321L, usuario.getDni());
    }

    @Test
    public void testGetNombre() {
        assertEquals("Juan", usuario.getNombre());
    }

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
    public void testSetFechaNacimiento() {
        usuario.setFechaNacimiento(Date.valueOf("2000-01-01"));
        assertEquals(Date.valueOf("2000-01-01"), usuario.getFechaNacimiento());
    }

    @Test
    public void testSetTipoPago() {
        usuario.setTipoPago(TipoPago.Mastercard);
        assertEquals(TipoPago.Mastercard, usuario.getTipoPago());
    }

    @Test
    public void testSetTipoUsuario() {
        usuario.setTipoUsuario(TipoUsuario.ADMIN);
        assertEquals(TipoUsuario.ADMIN, usuario.getTipoUsuario());
    }

    @Test
    public void testToStringWithMockedTimestamp() {
        try (MockedStatic<ZonedDateTime> zonedDateTimeHelper = Mockito.mockStatic(ZonedDateTime.class)) {
            zonedDateTimeHelper.when(ZonedDateTime::now).thenReturn(timestamp);

            String expected = "UsuarioJPA{dni=12345678, nombre='Juan', apellidos='Pérez', email='juan.perez@example.com', password='password123', telefono='123456789', fechaNacimiento=1990-01-01, tipoPago=Visa, tipoUsuario=CLIENTE}";
            assertEquals(expected, usuario.toString());
        }
    }

    @Test
    public void testConstructorWithMockito() {
        try (MockedStatic<Date> dateMock = Mockito.mockStatic(Date.class)) {
            dateMock.when(() -> Date.valueOf("1990-01-01")).thenReturn(new Date(0));

            UsuarioJPA usuarioMock = new UsuarioJPA(
                87654321L,
                "Carlos",
                "Gómez",
                "carlos.gomez@example.com",
                "newpassword",
                "987654321",
                Date.valueOf("1990-01-01"),
                TipoPago.Mastercard,
                TipoUsuario.ADMIN
            );

            assertNotNull(usuarioMock);
            assertEquals(87654321L, usuarioMock.getDni());
            assertEquals("Carlos", usuarioMock.getNombre());
            assertEquals("Gómez", usuarioMock.getApellidos());
            assertEquals("carlos.gomez@example.com", usuarioMock.getEmail());
            assertEquals("newpassword", usuarioMock.getPassword());
            assertEquals("987654321", usuarioMock.getTelefono());
            assertEquals(Date.valueOf("1990-01-01"), usuarioMock.getFechaNacimiento());
            assertEquals(TipoPago.Mastercard, usuarioMock.getTipoPago());
            assertEquals(TipoUsuario.ADMIN, usuarioMock.getTipoUsuario());
        }
    }
    @Test
    public void testEmptyConstructor() {
        UsuarioJPA emptyUsuario = new UsuarioJPA();
        assertNotNull(emptyUsuario);
    }
}
