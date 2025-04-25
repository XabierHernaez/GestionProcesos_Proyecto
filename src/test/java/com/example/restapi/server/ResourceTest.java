package com.example.restapi.server;

import com.example.restapi.model.TipoPago;
import com.example.restapi.model.TipoUsuario;
import com.example.restapi.model.Usuario;
import com.example.restapi.server.jpa.UsuarioJPA;
import com.example.restapi.server.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ResourceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private Resource resource;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegistrarUsuario_Success() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setDni(12345678L);
        usuario.setEmail("test@example.com");
        usuario.setNombre("Juan");
        usuario.setApellidos("Pérez");
        usuario.setPassword("password123");
        usuario.setTelefono("123456789");
        usuario.setFechaNacimiento(Date.valueOf("1990-01-01"));
        usuario.setTipoPago(TipoPago.Visa);
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);

        when(usuarioRepository.findById(usuario.getEmail())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = resource.registrarUsuario(usuario);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Usuario registrado con éxito.", response.getBody());
        verify(usuarioRepository, times(1)).save(any(UsuarioJPA.class));
    }

    @Test
    public void testRegistrarUsuario_EmailAlreadyExists() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setNombre("Juan");
        usuario.setPassword("password123");

        UsuarioJPA existingUsuario = new UsuarioJPA();
        existingUsuario.setEmail("test@example.com");

        when(usuarioRepository.findById(usuario.getEmail())).thenReturn(Optional.of(existingUsuario));

        // Act
        ResponseEntity<String> response = resource.registrarUsuario(usuario);

        // Assert
        assertEquals(409, response.getStatusCode().value());
        assertEquals("El email ya está registrado.", response.getBody());
        verify(usuarioRepository, never()).save(any(UsuarioJPA.class));
    }

    @Test
    public void testRegistrarUsuario_MissingEmail() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan");
        usuario.setPassword("password123");

        // Act
        ResponseEntity<String> response = resource.registrarUsuario(usuario);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertEquals("El campo 'email' es obligatorio.", response.getBody());
        verify(usuarioRepository, never()).save(any(UsuarioJPA.class));
    }

    @Test
    public void testRegistrarUsuario_NombreIsNull() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");

        // Act
        ResponseEntity<String> response = resource.registrarUsuario(usuario);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertEquals("El campo 'nombre' es obligatorio.", response.getBody());
        verify(usuarioRepository, never()).save(any(UsuarioJPA.class));
    }

    @Test
    public void testRegistrarUsuario_PasswordIsNull() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setNombre("Juan");

        // Act
        ResponseEntity<String> response = resource.registrarUsuario(usuario);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertEquals("El campo 'password' es obligatorio.", response.getBody());
        verify(usuarioRepository, never()).save(any(UsuarioJPA.class));
    }

    @Test
    public void testRegistrarUsuario_ExceptionThrown() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setNombre("Juan");
        usuario.setPassword("password123");

        when(usuarioRepository.findById(usuario.getEmail())).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<String> response = resource.registrarUsuario(usuario);

        // Assert
        assertEquals(500, response.getStatusCode().value());
        assertEquals("Error al registrar el usuario.", response.getBody());
    }

    @Test
    public void testLoginUsuario_Success() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");

        UsuarioJPA existingUsuario = new UsuarioJPA();
        existingUsuario.setEmail("test@example.com");
        existingUsuario.setPassword("password123");
        existingUsuario.setNombre("Juan");

        when(usuarioRepository.findById(usuario.getEmail())).thenReturn(Optional.of(existingUsuario));

        // Act
        ResponseEntity<String> response = resource.loginUsuario(usuario);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Inicio de sesión exitoso. Bienvenido, Juan!", response.getBody());
    }

    @Test
    public void testLoginUsuario_EmailIsNull() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setPassword("password123");

        // Act
        ResponseEntity<String> response = resource.loginUsuario(usuario);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertEquals("El campo 'email' es obligatorio.", response.getBody());
    }

    @Test
    public void testLoginUsuario_EmailIsEmpty() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail(""); // Email vacío
        usuario.setPassword("password123");

        // Act
        ResponseEntity<String> response = resource.loginUsuario(usuario);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertEquals("El campo 'email' es obligatorio.", response.getBody());
    }

    @Test
    public void testLoginUsuario_PasswordIsNull() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");

        // Act
        ResponseEntity<String> response = resource.loginUsuario(usuario);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertEquals("El campo 'password' es obligatorio.", response.getBody());
    }

    @Test
    public void testLoginUsuario_PasswordIsEmpty() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setPassword(""); // Password vacío

        // Act
        ResponseEntity<String> response = resource.loginUsuario(usuario);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertEquals("El campo 'password' es obligatorio.", response.getBody());
    }

    @Test
    public void testLoginUsuario_WrongPassword() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setPassword("wrongpassword");

        UsuarioJPA existingUsuario = new UsuarioJPA();
        existingUsuario.setEmail("test@example.com");
        existingUsuario.setPassword("password123");

        when(usuarioRepository.findById(usuario.getEmail())).thenReturn(Optional.of(existingUsuario));

        // Act
        ResponseEntity<String> response = resource.loginUsuario(usuario);

        // Assert
        assertEquals(401, response.getStatusCode().value());
        assertEquals("Contraseña incorrecta.", response.getBody());
    }

    @Test
    public void testLoginUsuario_UserNotFound() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("notfound@example.com");
        usuario.setPassword("password123");

        when(usuarioRepository.findById(usuario.getEmail())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = resource.loginUsuario(usuario);

        // Assert
        assertEquals(404, response.getStatusCode().value());
        assertEquals("Usuario no encontrado.", response.getBody());
    }

    @Test
    public void testLoginUsuario_ExceptionThrown() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");

        when(usuarioRepository.findById(usuario.getEmail())).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<String> response = resource.loginUsuario(usuario);

        // Assert
        assertEquals(500, response.getStatusCode().value());
        assertEquals("Error interno del servidor.", response.getBody());
    }
}
