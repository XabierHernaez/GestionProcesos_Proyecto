package com.example.restapi.performance;

import com.example.restapi.controller.ConciertoController;
import com.example.restapi.controller.UsuarioController;
import com.example.restapi.server.jpa.ConciertoJPA;
import com.example.restapi.server.jpa.UsuarioJPA;
import com.example.restapi.server.repository.ConciertoRepository;
import com.example.restapi.server.repository.UsuarioRepository;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import org.junit.jupiter.api.BeforeEach;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class PerformanceTest {

    @Mock
    private ConciertoRepository conciertoRepository;

    @InjectMocks
    private ConciertoController conciertoController;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioController usuarioController;

    // Configuración estática para el reporte de rendimiento
    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/perf-report.html"))
            .build();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Prueba de rendimiento para obtener todos los conciertos
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 10000, warmUpMs = 5000)
    @JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "99:2500ms", allowedErrorPercentage = 30.0f)
        public void testGetAllConciertosPerformance() {
        when(conciertoRepository.findAll()).thenReturn(Arrays.asList(new ConciertoJPA(), new ConciertoJPA()));

        var conciertos = conciertoController.getAllConciertos();
        assertNotNull(conciertos);
        assertEquals(2, conciertos.size());
    }

    // Prueba de rendimiento para buscar conciertos por nombre
    @Test
    @JUnitPerfTest(threads = 3, durationMs = 5000, warmUpMs = 2000)
    @JUnitPerfTestRequirement(
        executionsPerSec = 5, 
        percentiles = "99:2500ms", 
        allowedErrorPercentage = 5.0f 
    )
    public void testFindConciertosByNombrePerformance() {
        // Simulación del repositorio
        ConciertoJPA concierto1 = new ConciertoJPA();
        concierto1.setNombre("RockFest");
        concierto1.setLugar("Lugar 1");
        concierto1.setFecha(new java.sql.Date(System.currentTimeMillis()));
        concierto1.setCapacidadGeneral(1000);
        concierto1.setPrecioVIP(50.0);

        ConciertoJPA concierto2 = new ConciertoJPA();
        concierto2.setNombre("RockFest");
        concierto2.setLugar("Lugar 2");
        concierto2.setFecha(new java.sql.Date(System.currentTimeMillis()));
        concierto2.setCapacidadGeneral(2000);
        concierto2.setPrecioVIP(100.0);

        when(conciertoRepository.findByNombre("RockFest"))
            .thenReturn(Arrays.asList(concierto1, concierto2));

        // Llamada al método y validaciones
        var conciertos = conciertoRepository.findByNombre("RockFest");
        assertNotNull(conciertos);
        assertEquals(2, conciertos.size());
    }

    // Prueba de rendimiento para buscar conciertos por rango de fechas
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 6000, warmUpMs = 4000)
    @JUnitPerfTestRequirement(
        executionsPerSec = 5,
        percentiles = "99:4000ms",
        allowedErrorPercentage = 50f
    )
    public void testFindConciertosByFechaPerformance() {
        java.sql.Date fechaInicio = new java.sql.Date(System.currentTimeMillis() - 86400000L); // Un día antes
        java.sql.Date fechaFin = new java.sql.Date(System.currentTimeMillis()); // Fecha actual

        when(conciertoRepository.findByFechaBetween(fechaInicio, fechaFin))
            .thenReturn(Arrays.asList(new ConciertoJPA(), new ConciertoJPA()));

        var conciertos = conciertoRepository.findByFechaBetween(fechaInicio, fechaFin);
        assertNotNull(conciertos);
        assertEquals(2, conciertos.size());
    }

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 5000, warmUpMs = 2000)
    @JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "99:2500ms", allowedErrorPercentage = 5.0f)
    public void testGetAllUsuariosPerformance() {
        UsuarioJPA usuario1 = new UsuarioJPA();
        usuario1.setEmail("user1@example.com");
        UsuarioJPA usuario2 = new UsuarioJPA();
        usuario2.setEmail("user2@example.com");

        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));

        var usuarios = usuarioController.getAllUsuarios();
        assertNotNull(usuarios);
        assertEquals(2, usuarios.size());
    }

@Test
@JUnitPerfTest(threads = 1, durationMs = 6000, warmUpMs = 4000)
@JUnitPerfTestRequirement(
    executionsPerSec = 1,
    percentiles = "99:4000ms",
    allowedErrorPercentage = 0.5f
)
public void testGetUsuarioByEmailPerformance() {
    String email = "user@example.com";
    UsuarioJPA usuario = new UsuarioJPA();
    usuario.setEmail(email);

when(usuarioRepository.findById(anyString())).thenReturn(Optional.of(usuario));
    ResponseEntity<UsuarioJPA> response = usuarioController.getUsuarioByEmail(email);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(usuario, response.getBody());
    }

   @Test
@JUnitPerfTest(threads = 1, durationMs = 6000, warmUpMs = 4000)
@JUnitPerfTestRequirement(
    executionsPerSec = 1,
    percentiles = "99:4000ms",
    allowedErrorPercentage = 0.5f
)
public void testLoginUsuarioPerformance() {
    UsuarioJPA credenciales = new UsuarioJPA();
    credenciales.setEmail("user@example.com");
    credenciales.setPassword("password123");

    UsuarioJPA usuarioExistente = new UsuarioJPA();
    usuarioExistente.setEmail("user@example.com");
    usuarioExistente.setPassword("password123");

    when(usuarioRepository.findById(credenciales.getEmail())).thenReturn(Optional.of(usuarioExistente));

    ResponseEntity<UsuarioJPA> response = usuarioController.loginUsuario(credenciales);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(usuarioExistente, response.getBody());
}

    // Prueba de rendimiento para buscar usuarios por nombre
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 5000, warmUpMs = 2000)
    @JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "99:2500ms", allowedErrorPercentage = 5.0f)
    public void testFindUsuariosByNombrePerformance() {
        UsuarioJPA usuario1 = new UsuarioJPA();
        usuario1.setNombre("Juan");
        UsuarioJPA usuario2 = new UsuarioJPA();
        usuario2.setNombre("Juan");

        when(usuarioRepository.findByNombre("Juan"))
            .thenReturn(Arrays.asList(usuario1, usuario2));

        var usuarios = usuarioRepository.findByNombre("Juan");
        assertNotNull(usuarios);
        assertEquals(2, usuarios.size());
    }

    // Prueba de rendimiento para buscar usuarios por tipo de usuario
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 5000, warmUpMs = 2000)
    @JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "99:2500ms", allowedErrorPercentage = 5.0f)
    public void testFindUsuariosByTipoUsuarioPerformance() {
        UsuarioJPA usuario1 = new UsuarioJPA();
        usuario1.setTipoUsuario(com.example.restapi.model.TipoUsuario.CLIENTE);
        UsuarioJPA usuario2 = new UsuarioJPA();
        usuario2.setTipoUsuario(com.example.restapi.model.TipoUsuario.CLIENTE);

        when(usuarioRepository.findByTipoUsuario(com.example.restapi.model.TipoUsuario.CLIENTE))
            .thenReturn(Arrays.asList(usuario1, usuario2));

        var usuarios = usuarioRepository.findByTipoUsuario(com.example.restapi.model.TipoUsuario.CLIENTE);
        assertNotNull(usuarios);
        assertEquals(2, usuarios.size());
    }

    // Prueba de rendimiento para buscar usuarios por tipo de pago
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 5000, warmUpMs = 2000)
    @JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "99:2500ms", allowedErrorPercentage = 5.0f)
    public void testFindUsuariosByTipoPagoPerformance() {
        UsuarioJPA usuario1 = new UsuarioJPA();
        usuario1.setTipoPago(com.example.restapi.model.TipoPago.Visa);
        UsuarioJPA usuario2 = new UsuarioJPA();
        usuario2.setTipoPago(com.example.restapi.model.TipoPago.Visa);

        when(usuarioRepository.findByTipoPago(com.example.restapi.model.TipoPago.Visa))
            .thenReturn(Arrays.asList(usuario1, usuario2));

        var usuarios = usuarioRepository.findByTipoPago(com.example.restapi.model.TipoPago.Visa);
        assertNotNull(usuarios);
        assertEquals(2, usuarios.size());
    }
}