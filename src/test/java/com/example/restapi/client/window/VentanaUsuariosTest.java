package com.example.restapi.client.window;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.sql.Date;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import com.example.restapi.model.TipoPago;
import com.example.restapi.model.TipoUsuario;
import com.example.restapi.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.swing.*;

class VentanaUsuariosTest {

    private Client clientMock;
    private WebTarget webTargetMock;
    private Invocation.Builder builderMock;
    private Response responseMock;

    @BeforeEach
    void setUp() {
        clientMock = mock(Client.class);
        webTargetMock = mock(WebTarget.class);
        builderMock = mock(Invocation.Builder.class);
        responseMock = mock(Response.class);

        when(clientMock.target(anyString())).thenReturn(webTargetMock);
        when(webTargetMock.request(anyString())).thenReturn(builderMock);
        when(builderMock.get()).thenReturn(responseMock);
    }

    @Test
    void testCargaUsuarios_RespuestaOk() {
        // Preparamos datos
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan");
        usuario.setApellidos("Pérez");
        usuario.setEmail("juan@example.com");
        usuario.setPassword("1234");
        usuario.setTelefono("123456789");
        usuario.setDni(12345678L);
        usuario.setFechaNacimiento(new java.sql.Date(System.currentTimeMillis()));
        usuario.setTipoPago(TipoPago.Visa); // Enum real
        usuario.setTipoUsuario(TipoUsuario.ADMIN); // Enum real

        List<Usuario> usuarios = Arrays.asList(usuario);

        when(responseMock.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(responseMock.readEntity(any(GenericType.class))).thenReturn(usuarios);

        // Ejecutamos
        VentanaUsuarios ventana = new VentanaUsuarios(clientMock);

        // Validaciones
        JTable tabla = ventana.getTabla();
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();

        assertEquals(1, modelo.getRowCount());
        assertEquals("Juan", modelo.getValueAt(0, 0));
        assertEquals("Pérez", modelo.getValueAt(0, 1));
        assertEquals("juan@example.com", modelo.getValueAt(0, 2));
        assertEquals("1234", modelo.getValueAt(0, 3));
        assertEquals("123456789", modelo.getValueAt(0, 4));
        assertEquals(12345678L, modelo.getValueAt(0, 5));
        assertNotNull(modelo.getValueAt(0, 6)); // Fecha de nacimiento
        assertEquals(TipoPago.Visa, modelo.getValueAt(0, 7));
        assertEquals(TipoUsuario.ADMIN, modelo.getValueAt(0, 8));
    }

    @Test
    void testCargaUsuarios_ErrorCodigo() {
        when(responseMock.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        try (MockedStatic<JOptionPane> mocked = mockStatic(JOptionPane.class)) {
            VentanaUsuarios ventana = new VentanaUsuarios(clientMock);

            JTable tabla = ventana.getTabla();
            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();

            assertEquals(0, modelo.getRowCount());
        }
    }

    @Test
    void testCargaUsuarios_Excepcion() {
        when(clientMock.target(anyString())).thenThrow(new RuntimeException("Servidor no disponible"));

        try (MockedStatic<JOptionPane> mocked = mockStatic(JOptionPane.class)) {
            VentanaUsuarios ventana = new VentanaUsuarios(clientMock);

            JTable tabla = ventana.getTabla();
            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();

            assertEquals(0, modelo.getRowCount());
        }
    }
}