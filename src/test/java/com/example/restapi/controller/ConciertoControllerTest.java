package com.example.restapi.controller;

import com.example.restapi.server.jpa.ConciertoJPA;
import com.example.restapi.server.repository.ConciertoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ConciertoControllerTest {

    @Mock
    private ConciertoRepository conciertoRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ConciertoController conciertoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testGetAllConciertos() {
        ConciertoJPA concierto1 = new ConciertoJPA();
        ConciertoJPA concierto2 = new ConciertoJPA();

        when(conciertoRepository.findAll()).thenReturn(Arrays.asList(concierto1, concierto2));

        List<ConciertoJPA> conciertos = conciertoController.getAllConciertos();

        assertEquals(2, conciertos.size());
        verify(conciertoRepository, times(1)).findAll();
    }

    @Test
    public void testGetConciertoById_Found() {
        int id = 1;
        ConciertoJPA concierto = new ConciertoJPA();

        when(conciertoRepository.findById(id)).thenReturn(Optional.of(concierto));

        ResponseEntity<ConciertoJPA> response = conciertoController.getConciertoById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(concierto, response.getBody());
        verify(conciertoRepository, times(1)).findById(id);
    }

    @Test
    public void testGetConciertoById_NotFound() {
        int id = 1;

        when(conciertoRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<ConciertoJPA> response = conciertoController.getConciertoById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(conciertoRepository, times(1)).findById(id);
    }

    @Test
    public void testAddConcierto() {
        ConciertoJPA nuevoConcierto = new ConciertoJPA();

        when(conciertoRepository.save(nuevoConcierto)).thenReturn(nuevoConcierto);

        ResponseEntity<ConciertoJPA> response = conciertoController.addConcierto(nuevoConcierto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(nuevoConcierto, response.getBody());
        verify(conciertoRepository, times(1)).save(nuevoConcierto);
    }

    @Test
    public void testUpdateConcierto_Found() {
        int id = 1;
        ConciertoJPA existente = new ConciertoJPA();
        ConciertoJPA datosActualizados = new ConciertoJPA();

        when(conciertoRepository.findById(id)).thenReturn(Optional.of(existente));
        when(conciertoRepository.save(any(ConciertoJPA.class))).thenReturn(existente);

        ResponseEntity<ConciertoJPA> response = conciertoController.updateConcierto(id, datosActualizados);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(conciertoRepository, times(1)).findById(id);
        verify(conciertoRepository, times(1)).save(existente);
    }

    @Test
    public void testUpdateConcierto_NotFound() {
        int id = 1;
        ConciertoJPA datosActualizados = new ConciertoJPA();

        when(conciertoRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<ConciertoJPA> response = conciertoController.updateConcierto(id, datosActualizados);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(conciertoRepository, times(1)).findById(id);
        verify(conciertoRepository, never()).save(any(ConciertoJPA.class));
    }

    @Test
    public void testDeleteConcierto_Found() {
        int id = 1;

        when(conciertoRepository.existsById(id)).thenReturn(true);
        doNothing().when(conciertoRepository).deleteById(id);
        doNothing().when(jdbcTemplate).execute(anyString());

        ResponseEntity<Void> response = conciertoController.deleteConcierto(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(conciertoRepository, times(1)).existsById(id);
        verify(conciertoRepository, times(1)).deleteById(id);
        verify(jdbcTemplate, times(1)).execute(anyString());
    }

    @Test
    public void testDeleteConcierto_NotFound() {
        int id = 1;

        when(conciertoRepository.existsById(id)).thenReturn(false);

        ResponseEntity<Void> response = conciertoController.deleteConcierto(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(conciertoRepository, times(1)).existsById(id);
        verify(conciertoRepository, never()).deleteById(id);
        verify(jdbcTemplate, never()).execute(anyString());
    }
}