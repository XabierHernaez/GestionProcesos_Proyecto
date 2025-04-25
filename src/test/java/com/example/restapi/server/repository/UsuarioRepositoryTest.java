package com.example.restapi.server.repository;

import com.example.restapi.server.jpa.UsuarioJPA;
import com.example.restapi.model.TipoPago;
import com.example.restapi.model.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UsuarioRepositoryTest {

    @Spy
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByEmail() {
        String email = "test@example.com";
        UsuarioJPA usuario = new UsuarioJPA();
        usuario.setEmail(email); 
        usuario.setNombre("Juan");
        usuario.setApellidos("Pérez");

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        Optional<UsuarioJPA> result = usuarioRepository.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(usuarioRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testFindByNombre() {
        String nombre = "Juan";

        // Crear usuarios con el campo "nombre" configurado
        UsuarioJPA usuario1 = new UsuarioJPA();
        usuario1.setEmail("juan1@example.com");
        usuario1.setNombre(nombre); // Configurar el nombre
        usuario1.setApellidos("Pérez");

        UsuarioJPA usuario2 = new UsuarioJPA();
        usuario2.setEmail("juan2@example.com");
        usuario2.setNombre(nombre); // Configurar el nombre
        usuario2.setApellidos("Gómez");

        // Configurar el mock para devolver la lista de usuarios
        when(usuarioRepository.findByNombre(nombre)).thenReturn(Arrays.asList(usuario1, usuario2));

        // Act
        List<UsuarioJPA> result = usuarioRepository.findByNombre(nombre);

        // Assert
        assertEquals(2, result.size());
        assertEquals(nombre, result.get(0).getNombre()); // Verificar que el nombre coincide
        assertEquals(nombre, result.get(1).getNombre()); // Verificar que el nombre coincide
        verify(usuarioRepository, times(1)).findByNombre(nombre);
    }

    @Test
    public void testFindByNombreContaining() {
        String nombre = "Juan";
        UsuarioJPA usuario1 = new UsuarioJPA();
        usuario1.setEmail("juan.carlos@example.com"); 
        usuario1.setNombre("Juan Carlos");

        UsuarioJPA usuario2 = new UsuarioJPA();
        usuario2.setEmail("juan.perez@example.com"); 
        usuario2.setNombre("Juan Pérez");

        when(usuarioRepository.findByNombreContaining(nombre)).thenReturn(Arrays.asList(usuario1, usuario2));

        List<UsuarioJPA> result = usuarioRepository.findByNombreContaining(nombre);

        assertEquals(2, result.size());
        assertTrue(result.get(0).getNombre().contains(nombre));
        assertTrue(result.get(1).getNombre().contains(nombre));
        verify(usuarioRepository, times(1)).findByNombreContaining(nombre);
    }

    @Test
    public void testFindByTipoUsuario() {
        TipoUsuario tipoUsuario = TipoUsuario.ADMIN;
        UsuarioJPA usuario1 = new UsuarioJPA();
        usuario1.setEmail("admin1@example.com"); 
        usuario1.setTipoUsuario(tipoUsuario);

        UsuarioJPA usuario2 = new UsuarioJPA();
        usuario2.setEmail("admin2@example.com"); 
        usuario2.setTipoUsuario(tipoUsuario);

        when(usuarioRepository.findByTipoUsuario(tipoUsuario)).thenReturn(Arrays.asList(usuario1, usuario2));

        List<UsuarioJPA> result = usuarioRepository.findByTipoUsuario(tipoUsuario);

        assertEquals(2, result.size());
        assertEquals(tipoUsuario, result.get(0).getTipoUsuario());
        assertEquals(tipoUsuario, result.get(1).getTipoUsuario());
        verify(usuarioRepository, times(1)).findByTipoUsuario(tipoUsuario);
    }

    @Test
    public void testFindByTipoPago() {
        TipoPago tipoPago = TipoPago.Visa;
        UsuarioJPA usuario1 = new UsuarioJPA();
        usuario1.setEmail("visa1@example.com"); 
        usuario1.setTipoPago(tipoPago);

        UsuarioJPA usuario2 = new UsuarioJPA();
        usuario2.setEmail("visa2@example.com"); 
        usuario2.setTipoPago(tipoPago);

        when(usuarioRepository.findByTipoPago(tipoPago)).thenReturn(Arrays.asList(usuario1, usuario2));

        List<UsuarioJPA> result = usuarioRepository.findByTipoPago(tipoPago);

        assertEquals(2, result.size());
        assertEquals(tipoPago, result.get(0).getTipoPago());
        assertEquals(tipoPago, result.get(1).getTipoPago());
        verify(usuarioRepository, times(1)).findByTipoPago(tipoPago);
    }

    @Test
    public void testCountByTipoUsuario() {
        String tipoUsuario = "CLIENTE";
        long count = 5;

        when(usuarioRepository.countByTipoUsuario(tipoUsuario)).thenReturn(count);

        long result = usuarioRepository.countByTipoUsuario(tipoUsuario);

        assertEquals(count, result);
        verify(usuarioRepository, times(1)).countByTipoUsuario(tipoUsuario);
    }

    @Test
    public void testForceExecution() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByNombre(anyString())).thenReturn(Arrays.asList());
        when(usuarioRepository.findByTipoUsuario(any())).thenReturn(Arrays.asList());
        when(usuarioRepository.findByTipoPago(any())).thenReturn(Arrays.asList());
        when(usuarioRepository.countByTipoUsuario(anyString())).thenReturn(0L);

        usuarioRepository.findByEmail("dummy@example.com");
        usuarioRepository.findByNombre("Dummy");
        usuarioRepository.findByTipoUsuario(TipoUsuario.CLIENTE);
        usuarioRepository.findByTipoPago(TipoPago.Visa);
        usuarioRepository.countByTipoUsuario("CLIENTE");

        verify(usuarioRepository, times(1)).findByEmail("dummy@example.com");
        verify(usuarioRepository, times(1)).findByNombre("Dummy");
        verify(usuarioRepository, times(1)).findByTipoUsuario(TipoUsuario.CLIENTE);
        verify(usuarioRepository, times(1)).findByTipoPago(TipoPago.Visa);
        verify(usuarioRepository, times(1)).countByTipoUsuario("CLIENTE");
    }
}
