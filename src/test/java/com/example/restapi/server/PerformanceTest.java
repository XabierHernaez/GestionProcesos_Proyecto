package com.example.restapi.server;

import com.example.restapi.controller.ConciertoController;
import com.example.restapi.server.jpa.ConciertoJPA;
import com.example.restapi.server.repository.ConciertoRepository;
import org.springframework.http.ResponseEntity;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PerformanceTest {

    @Mock
    private ConciertoRepository conciertoRepository;

    @InjectMocks
    private ConciertoController conciertoController;

    private static final HtmlReportGenerator HTML_REPORT_GENERATOR = new HtmlReportGenerator("target/junitperf-reports/performance-report.html");

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 5000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 50)
    public void testGetConciertoByIdPerformance() {
        // Simular datos para la prueba
        ConciertoJPA concierto = new ConciertoJPA();
        when(conciertoRepository.findById(1)).thenReturn(Optional.of(concierto));

        ResponseEntity<ConciertoJPA> response = conciertoController.getConciertoById(1);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @AfterAll
    public static void generateHtmlReport() throws IOException {
        // La generación manual de reportes NO funcionará automáticamente aquí
        // Necesitarías agregarlo manualmente si quieres, pero por defecto no puedes hacer mucho sin contexto
        // Solo
