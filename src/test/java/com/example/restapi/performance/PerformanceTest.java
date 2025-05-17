package com.example.restapi.performance;

import com.example.restapi.controller.ConciertoController;
import com.example.restapi.server.jpa.ConciertoJPA;
import com.example.restapi.server.repository.ConciertoRepository;

import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class PerformanceTest {

    @Mock
    private ConciertoRepository conciertoRepository;

    @InjectMocks
    private ConciertoController conciertoController;

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
        percentiles = "99:2500ms", // Permite que el 99% esté bajo 2500ms
        allowedErrorPercentage = 5.0f // Solo permite hasta un 5% de errores
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
    @JUnitPerfTest(threads = 10, durationMs = 5000, warmUpMs = 2000)
    @JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "99:2500ms", allowedErrorPercentage = 30.0f)
    public void testFindConciertosByFechaPerformance() {
        // Fechas usando java.sql.Date
        java.sql.Date fechaInicio = new java.sql.Date(System.currentTimeMillis() - 86400000L); // Un día antes
        java.sql.Date fechaFin = new java.sql.Date(System.currentTimeMillis()); // Fecha actual

        when(conciertoRepository.findByFechaBetween(fechaInicio, fechaFin))
            .thenReturn(Arrays.asList(new ConciertoJPA(), new ConciertoJPA()));

        // Llamada al método y validaciones
        var conciertos = conciertoRepository.findByFechaBetween(fechaInicio, fechaFin);
        assertNotNull(conciertos);
        assertEquals(2, conciertos.size());
    }

   

}