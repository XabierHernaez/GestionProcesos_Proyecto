package com.example.restapi.controller;

import com.example.restapi.server.jpa.UsuarioJPA;
import com.example.restapi.server.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UsuarioControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsuarios() {
        UsuarioJPA usuario1 = new UsuarioJPA();
        usuario1.setEmail("user1@example.com");
        UsuarioJPA usuario2 = new UsuarioJPA();
        usuario2.setEmail("user2@example.com");

        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));

        List<UsuarioJPA> usuarios = usuarioController.getAllUsuarios();

        assertEquals(2, usuarios.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    public void testGetUsuarioByEmail_Found() {
        String email = "user@example.com";
        UsuarioJPA usuario = new UsuarioJPA();
        usuario.setEmail(email);

        when(usuarioRepository.findById(email)).thenReturn(Optional.of(usuario));

        ResponseEntity<UsuarioJPA> response = usuarioController.getUsuarioByEmail(email);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(usuario, response.getBody());
        verify(usuarioRepository, times(1)).findById(email);
    }

    @Test
    public void testGetUsuarioByEmail_NotFound() {
        String email = "user@example.com";

        when(usuarioRepository.findById(email)).thenReturn(Optional.empty());

        ResponseEntity<UsuarioJPA> response = usuarioController.getUsuarioByEmail(email);

        assertEquals(404, response.getStatusCode().value());
        verify(usuarioRepository, times(1)).findById(email);
    }

    @Test
    public void testCreateUsuario_Success() {
        UsuarioJPA usuario = new UsuarioJPA();
        usuario.setEmail("user@example.com");

        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        ResponseEntity<UsuarioJPA> response = usuarioController.createUsuario(usuario);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(usuario, response.getBody());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    public void testCreateUsuario_Exception() {
        UsuarioJPA usuario = new UsuarioJPA();
        usuario.setEmail("user@example.com");

        when(usuarioRepository.save(usuario)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<UsuarioJPA> response = usuarioController.createUsuario(usuario);

        assertEquals(400, response.getStatusCode().value());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    public void testUpdateUsuario_Found() {
        String email = "user@example.com";
        UsuarioJPA usuarioExistente = new UsuarioJPA();
        usuarioExistente.setEmail(email);
        UsuarioJPA usuarioDetalles = new UsuarioJPA();
        usuarioDetalles.setNombre("Updated Name");

        when(usuarioRepository.findById(email)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(UsuarioJPA.class))).thenReturn(usuarioExistente);

        ResponseEntity<UsuarioJPA> response = usuarioController.updateUsuario(email, usuarioDetalles);

        assertEquals(200, response.getStatusCode().value());
        verify(usuarioRepository, times(1)).findById(email);
        verify(usuarioRepository, times(1)).save(usuarioExistente);
    }

    @Test
    public void testUpdateUsuario_NotFound() {
        String email = "user@example.com";
        UsuarioJPA usuarioDetalles = new UsuarioJPA();
        usuarioDetalles.setNombre("Updated Name");

        when(usuarioRepository.findById(email)).thenReturn(Optional.empty());

        ResponseEntity<UsuarioJPA> response = usuarioController.updateUsuario(email, usuarioDetalles);

        assertEquals(404, response.getStatusCode().value());
        verify(usuarioRepository, times(1)).findById(email);
        verify(usuarioRepository, never()).save(any(UsuarioJPA.class));
    }

    @Test
    public void testDeleteUsuario_Found() {
        String email = "user@example.com";
        UsuarioJPA usuario = new UsuarioJPA();
        usuario.setEmail(email);

        when(usuarioRepository.findById(email)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).deleteById(email);

        ResponseEntity<Void> response = usuarioController.deleteUsuario(email);

        assertEquals(204, response.getStatusCode().value());
        verify(usuarioRepository, times(1)).findById(email);
        verify(usuarioRepository, times(1)).deleteById(email);
    }

    @Test
    public void testDeleteUsuario_NotFound() {
        String email = "user@example.com";

        when(usuarioRepository.findById(email)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = usuarioController.deleteUsuario(email);

        assertEquals(404, response.getStatusCode().value());
        verify(usuarioRepository, times(1)).findById(email);
        verify(usuarioRepository, never()).deleteById(email);
    }

    @Test
    public void testLoginUsuario_Success() {
        UsuarioJPA credenciales = new UsuarioJPA();
        credenciales.setEmail("user@example.com");
        credenciales.setPassword("password123");

        UsuarioJPA usuarioExistente = new UsuarioJPA();
        usuarioExistente.setEmail("user@example.com");
        usuarioExistente.setPassword("password123");

        when(usuarioRepository.findById(credenciales.getEmail())).thenReturn(Optional.of(usuarioExistente));

        ResponseEntity<UsuarioJPA> response = usuarioController.loginUsuario(credenciales);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(usuarioExistente, response.getBody());
        verify(usuarioRepository, times(1)).findById(credenciales.getEmail());
    }

    @Test
    public void testLoginUsuario_WrongPassword() {
        UsuarioJPA credenciales = new UsuarioJPA();
        credenciales.setEmail("user@example.com");
        credenciales.setPassword("wrongpassword");

        UsuarioJPA usuarioExistente = new UsuarioJPA();
        usuarioExistente.setEmail("user@example.com");
        usuarioExistente.setPassword("password123");

        when(usuarioRepository.findById(credenciales.getEmail())).thenReturn(Optional.of(usuarioExistente));

        ResponseEntity<UsuarioJPA> response = usuarioController.loginUsuario(credenciales);

        assertEquals(401, response.getStatusCode().value());
        verify(usuarioRepository, times(1)).findById(credenciales.getEmail());
    }

    @Test
    public void testLoginUsuario_NotFound() {
        UsuarioJPA credenciales = new UsuarioJPA();
        credenciales.setEmail("user@example.com");
        credenciales.setPassword("password123");

        when(usuarioRepository.findById(credenciales.getEmail())).thenReturn(Optional.empty());

        ResponseEntity<UsuarioJPA> response = usuarioController.loginUsuario(credenciales);

        assertEquals(404, response.getStatusCode().value());
        verify(usuarioRepository, times(1)).findById(credenciales.getEmail());
    }
}
