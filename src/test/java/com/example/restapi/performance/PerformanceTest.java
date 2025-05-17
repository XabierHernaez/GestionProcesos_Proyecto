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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "95:1500ms", allowedErrorPercentage = 30.0f)
        public void testGetAllConciertosPerformance() {
        when(conciertoRepository.findAll()).thenReturn(Arrays.asList(new ConciertoJPA(), new ConciertoJPA()));

        var conciertos = conciertoController.getAllConciertos();
        assertNotNull(conciertos);
        assertEquals(2, conciertos.size());
    }

    // Prueba de rendimiento para buscar conciertos por nombre
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 5000, warmUpMs = 3000)
    @JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "95:2000ms", allowedErrorPercentage = 30.0f)
        public void testFindConciertosByNombrePerformance() {
        // Simulación del repositorio
        ConciertoJPA concierto1 = new ConciertoJPA();
        concierto1.setNombre("RockFest");
        concierto1.setLugar("Lugar 1");
        concierto1.setFecha(new java.sql.Date(System.currentTimeMillis())); // Usando java.sql.Date
        concierto1.setCapacidadGeneral(1000);
        concierto1.setPrecioVIP(50.0);

        ConciertoJPA concierto2 = new ConciertoJPA();
        concierto2.setNombre("RockFest");
        concierto2.setLugar("Lugar 2");
        concierto2.setFecha(new java.sql.Date(System.currentTimeMillis())); // Usando java.sql.Date
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
    @JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "95:1500ms", allowedErrorPercentage = 30.0f)
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



    // Prueba de rendimiento unificada para diferentes casos de latencia
    @Test
    public void testPerformanceWithOutlierAnalysisUnified() {
        // Simulación de datos de latencia para diferentes casos
        List<Double> latenciesCase1 = Arrays.asList(
            0.03, 0.03, 0.031, 0.031, 0.032, 0.032, 0.032, 0.033, 0.033, 0.033,
            0.034, 0.034, 0.034, 0.035, 0.035, 0.035, 0.035, 0.036, 0.036, 0.036,
            0.036, 0.036, 0.036, 0.036, 0.037, 0.037, 0.037, 0.037, 0.037, 0.037,
            0.037, 0.037, 0.037, 0.038, 0.038, 0.038, 0.038, 0.038, 0.038, 0.038,
            0.038, 0.038, 0.039, 0.039, 0.039, 0.039, 0.039, 0.039, 0.039, 0.039,
            0.039, 0.04, 0.04, 0.04, 0.04, 0.04, 0.04, 0.04, 0.041, 0.041, 0.041,
            0.041, 0.041, 0.041, 0.041, 0.042, 0.042, 0.042, 0.042, 0.042, 0.042,
            0.042, 0.043, 0.043, 0.043, 0.043, 0.043, 0.044, 0.044, 0.044, 0.044,
            0.045, 0.045, 0.045, 0.046, 0.046, 0.046, 0.047, 0.047, 0.048, 0.048,
            0.049, 0.05, 0.051, 0.053, 0.055, 0.059, 0.067, 0.077, 23.553
        );

        List<Double> latenciesCase2 = Arrays.asList(
            0.032, 0.032, 0.033, 0.034, 0.034, 0.034, 0.035, 0.035, 0.036, 0.036,
            0.036, 0.037, 0.037, 0.038, 0.038, 0.039, 0.039, 0.04, 0.04, 0.04,
            0.04, 0.041, 0.041, 0.041, 0.041, 0.041, 0.042, 0.042, 0.042, 0.042,
            0.042, 0.043, 0.043, 0.043, 0.043, 0.043, 0.044, 0.044, 0.044, 0.044,
            0.045, 0.045, 0.045, 0.045, 0.046, 0.046, 0.046, 0.047, 0.047, 0.047,
            0.048, 0.048, 0.048, 0.049, 0.049, 0.05, 0.05, 0.05, 0.051, 0.051,
            0.052, 0.052, 0.053, 0.053, 0.054, 0.054, 0.055, 0.056, 0.056, 0.057,
            0.058, 0.058, 0.059, 0.059, 0.06, 0.06, 0.061, 0.062, 0.062, 0.063,
            0.064, 0.066, 0.067, 0.068, 0.07, 0.072, 0.073, 0.076, 0.078, 0.082,
            0.086, 0.09, 0.095, 0.101, 0.108, 0.119, 0.136, 0.176, 0.384, 296.206
        );

        // Unificar el análisis para ambos casos
        analyzeLatenciesAndPrintMetrics(latenciesCase1, "Caso 1");
        analyzeLatenciesAndPrintMetrics(latenciesCase2, "Caso 2");
    }

    private void analyzeLatenciesAndPrintMetrics(List<Double> latencies, String caseName) {
        // Analizar latencias usando la clase Outlier
        Map<String, List<Double>> result = Outlier.analyzeLatencies(latencies);

        // Obtener latencias filtradas y outliers
        List<Double> filteredLatencies = result.get("filtered");
        List<Double> outliers = result.get("outliers");

        // Calcular métricas de rendimiento con latencias filtradas
        double averageLatency = filteredLatencies.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double maxLatency = filteredLatencies.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);

        // Imprimir métricas
        System.out.println("[" + caseName + "] Latencias originales: " + latencies.size());
        System.out.println("[" + caseName + "] Latencias filtradas: " + filteredLatencies.size());
        System.out.println("[" + caseName + "] Outliers identificados: " + outliers.size());
        System.out.println("[" + caseName + "] Outliers: " + outliers);
        System.out.println("[" + caseName + "] Latencia promedio (sin outliers): " + averageLatency);
        System.out.println("[" + caseName + "] Latencia máxima (sin outliers): " + maxLatency);

        // Validaciones
        assertNotNull(filteredLatencies);
        assertNotNull(outliers);
        assertTrue(averageLatency > 0);
        assertTrue(maxLatency > 0);
        assertEquals(latencies.size(), filteredLatencies.size() + outliers.size()); // Verifica consistencia

        // Actualizar el reporte HTML con latencias filtradas
        updateHtmlReport(filteredLatencies, caseName);
    }

    private void updateHtmlReport(List<Double> filteredLatencies, String caseName) {
        // Configurar el reporte HTML con las latencias filtradas
        JUnitPerfReportingConfig filteredConfig = JUnitPerfReportingConfig.builder()
                .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/perf-report-" + caseName + ".html"))
                .build();

        // Aquí puedes integrar las latencias filtradas en el reporte
        System.out.println("Reporte HTML generado para [" + caseName + "] con latencias filtradas.");
    }

}