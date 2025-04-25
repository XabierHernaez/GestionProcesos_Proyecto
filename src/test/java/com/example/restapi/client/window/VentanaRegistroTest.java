package com.example.restapi.client.window;

import com.example.restapi.model.TipoPago;
import com.example.restapi.model.TipoUsuario;
import com.example.restapi.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class VentanaRegistroTest {

    @Mock
    private Client mockClient;

    @Mock
    private WebTarget mockWebTarget;

    @Mock
    private Invocation.Builder mockInvocationBuilder;

    @Mock
    private Response mockResponse;

    private VentanaRegistro ventanaRegistro;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ventanaRegistro = new VentanaRegistro(null); // Pasamos `null` como parent para simplificar

        // Configurar el cliente HTTP simulado
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockInvocationBuilder);
        when(mockInvocationBuilder.post(any(Entity.class))).thenReturn(mockResponse);
    }

/*
    @Test
    public void testRegistrarUsuario_Success() {
        // Arrange
        when(mockResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        // Simular datos en los campos de texto usando getters
        ventanaRegistro.getTxtNombre().setText("cliente");
        ventanaRegistro.getTxtApellido().setText("Pérez");
        ventanaRegistro.getTxtEmail().setText("c.perez@example.com");
        ventanaRegistro.getTxtPassword().setText("password123");
        ventanaRegistro.getTxtTelefono().setText("123456789");
        ventanaRegistro.getTxtFechaNacimiento().setText("1990-01-01");
        ventanaRegistro.getTxtDni().setText("12345678");
        ventanaRegistro.getComboTipoUsuario().setSelectedItem(TipoUsuario.CLIENTE);
        ventanaRegistro.getComboTipoPago().setSelectedItem(TipoPago.Visa);

        // Act
        ventanaRegistro.registrar();

        // Assert
        verify(mockClient.target(anyString())).request(MediaType.APPLICATION_JSON).post(any(Entity.class));
        assertEquals("", ventanaRegistro.getTxtNombre().getText()); // Verificar que los campos se limpian
    }

    @Test
    public void testRegistrarUsuario_EmailAlreadyExists() {
        // Arrange
        when(mockResponse.getStatus()).thenReturn(Response.Status.CONFLICT.getStatusCode());

        // Simular datos en los campos de texto usando getters
        ventanaRegistro.getTxtNombre().setText("Juan");
        ventanaRegistro.getTxtApellido().setText("Pérez");
        ventanaRegistro.getTxtEmail().setText("juan.perez@example.com");
        ventanaRegistro.getTxtPassword().setText("password123");
        ventanaRegistro.getTxtTelefono().setText("123456789");
        ventanaRegistro.getTxtFechaNacimiento().setText("1990-01-01");
        ventanaRegistro.getTxtDni().setText("12345678");
        ventanaRegistro.getComboTipoUsuario().setSelectedItem(TipoUsuario.CLIENTE);
        ventanaRegistro.getComboTipoPago().setSelectedItem(TipoPago.Visa);

        // Act
        ventanaRegistro.registrar();

        // Assert
        // Verificar que se realizó la solicitud al cliente HTTP
        verify(mockWebTarget).request(MediaType.APPLICATION_JSON);
        verify(mockInvocationBuilder).post(any(Entity.class));

        // Verificar que el email no se limpia
        assertEquals("juan.perez@example.com", ventanaRegistro.getTxtEmail().getText());
    }
*/

    @Test
    public void testRegistrarUsuario_InvalidDniOrTelefono() {
        // Arrange
        ventanaRegistro.getTxtNombre().setText("Juan");
        ventanaRegistro.getTxtApellido().setText("Pérez");
        ventanaRegistro.getTxtEmail().setText("juan.perez@example.com");
        ventanaRegistro.getTxtPassword().setText("password123");
        ventanaRegistro.getTxtTelefono().setText("invalid"); // Teléfono inválido
        ventanaRegistro.getTxtFechaNacimiento().setText("1990-01-01");
        ventanaRegistro.getTxtDni().setText("invalid"); // DNI inválido
        ventanaRegistro.getComboTipoUsuario().setSelectedItem(TipoUsuario.CLIENTE);
        ventanaRegistro.getComboTipoPago().setSelectedItem(TipoPago.Visa);

        // Act
        ventanaRegistro.registrar();

        // Assert
        // Verificar que no se realiza ninguna solicitud al cliente
        verify(mockClient, never()).target(anyString());
    }

    @Test
    public void testRegistrarUsuario_MissingFields() {
        // Arrange
        ventanaRegistro.getTxtNombre().setText(""); // Campo vacío
        ventanaRegistro.getTxtApellido().setText("Pérez");
        ventanaRegistro.getTxtEmail().setText("juan.perez@example.com");
        ventanaRegistro.getTxtPassword().setText("password123");
        ventanaRegistro.getTxtTelefono().setText("123456789");
        ventanaRegistro.getTxtFechaNacimiento().setText("1990-01-01");
        ventanaRegistro.getTxtDni().setText("12345678");
        ventanaRegistro.getComboTipoUsuario().setSelectedItem(TipoUsuario.CLIENTE);
        ventanaRegistro.getComboTipoPago().setSelectedItem(TipoPago.Visa);

        // Act
        ventanaRegistro.registrar();

        // Assert
        // Verificar que no se realiza ninguna solicitud al cliente
        verify(mockClient, never()).target(anyString());
    }

    @Test
    public void testModificarUsuario_Success() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            // Arrange
            when(mockResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
            Usuario usuario = new Usuario();
            usuario.setNombre("Juan");
            usuario.setApellidos("Pérez");
            usuario.setEmail("juan.perez@example.com");
            usuario.setPassword("password123");
            usuario.setTelefono("123456789");
            usuario.setFechaNacimiento(java.sql.Date.valueOf("1990-01-01"));
            usuario.setDni(12345678L);
            usuario.setTipoUsuario(TipoUsuario.CLIENTE);
            usuario.setTipoPago(TipoPago.Visa);

            // Simular que los datos del usuario se cargan en los campos
            ventanaRegistro.getTxtNombre().setText(usuario.getNombre());
            ventanaRegistro.getTxtApellido().setText(usuario.getApellidos());
            ventanaRegistro.getTxtEmail().setText(usuario.getEmail());
            ventanaRegistro.getTxtPassword().setText(usuario.getPassword());
            ventanaRegistro.getTxtTelefono().setText(usuario.getTelefono());
            ventanaRegistro.getTxtFechaNacimiento().setText(usuario.getFechaNacimiento().toString());
            ventanaRegistro.getTxtDni().setText(String.valueOf(usuario.getDni()));
            ventanaRegistro.getComboTipoUsuario().setSelectedItem(usuario.getTipoUsuario());
            ventanaRegistro.getComboTipoPago().setSelectedItem(usuario.getTipoPago());

            // Act
            ventanaRegistro.getBtnModificar().doClick();

            // Assert
            assertEquals("Juan", ventanaRegistro.getTxtNombre().getText());
            assertEquals("Pérez", ventanaRegistro.getTxtApellido().getText());
            assertEquals("juan.perez@example.com", ventanaRegistro.getTxtEmail().getText());
            assertEquals("password123", new String(ventanaRegistro.getTxtPassword().getPassword()));
            assertEquals("123456789", ventanaRegistro.getTxtTelefono().getText());
            assertEquals("1990-01-01", ventanaRegistro.getTxtFechaNacimiento().getText());
            assertEquals("12345678", ventanaRegistro.getTxtDni().getText());
            assertEquals(TipoUsuario.CLIENTE, ventanaRegistro.getComboTipoUsuario().getSelectedItem());
            assertEquals(TipoPago.Visa, ventanaRegistro.getComboTipoPago().getSelectedItem());
        });
    }

    @Test
    public void testModificarUsuario_NotFound() {
        // Arrange
        when(mockResponse.getStatus()).thenReturn(Response.Status.NOT_FOUND.getStatusCode());

        // Simular que el botón está en la fase de "Buscar Usuario"
        ventanaRegistro.getBtnModificar().setText("Modificar datos");

        // Act
        ventanaRegistro.getBtnModificar().doClick();

        // Assert
        assertEquals("", ventanaRegistro.getTxtNombre().getText()); // Verificar que los campos no se llenan
    }
}
