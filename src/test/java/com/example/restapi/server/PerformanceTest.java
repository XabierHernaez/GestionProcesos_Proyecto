package com.example.restapi.server;

import com.example.restapi.controller.ConciertoController;
import com.example.restapi.controller.UsuarioController;
import com.example.restapi.server.jpa.ConciertoJPA;
import com.example.restapi.server.repository.ConciertoRepository;
import com.example.restapi.server.repository.UsuarioRepository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.data.EvaluationContext;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class PerformanceTest {

    @Mock
    private ConciertoRepository conciertoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ConciertoController conciertoController;

    @InjectMocks
    private UsuarioController usuarioController;

    private static final HtmlReportGenerator HTML_REPORT_GENERATOR = new HtmlReportGenerator("target/junitperf-reports/performance-report.html");
    private static final LinkedHashSet<EvaluationContext> evaluationContexts = new LinkedHashSet<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 5000, warmUpMs = 1000)
    public void testGetAllConciertosPerformance() {
        // Simular datos para la prueba
        when(conciertoRepository.findAll()).thenReturn(Arrays.asList(new ConciertoJPA(), new ConciertoJPA()));

        var conciertos = conciertoController.getAllConciertos();
        assertNotNull(conciertos);
        assertEquals(2, conciertos.size());
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 5000, warmUpMs = 1000)
    public void testGetConciertoByIdPerformance() {
        // Simular datos para la prueba
        ConciertoJPA concierto = new ConciertoJPA();
        when(conciertoRepository.findById(1)).thenReturn(Optional.of(concierto));

        ResponseEntity<ConciertoJPA> response = conciertoController.getConciertoById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @AfterAll
    public static void generateHtmlReport() throws IOException {
        // Generar el reporte HTML después de ejecutar las pruebas
        HTML_REPORT_GENERATOR.generateReport(evaluationContexts);
    }
}