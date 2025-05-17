package com.example.restapi.performance;

import java.util.*;
import java.util.stream.Collectors;

public class Outlier {

    /**
     * Identifica y filtra valores atípicos (outliers) de una lista de latencias.
     *
     * @param latencies Lista de latencias en milisegundos.
     * @return Un mapa con las latencias filtradas y los outliers.
     */
    public static Map<String, List<Double>> analyzeLatencies(List<Double> latencies) {
        // Calcular la media
        double mean = latencies.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        // Calcular la desviación estándar
        double stdDev = Math.sqrt(latencies.stream()
            .mapToDouble(latency -> Math.pow(latency - mean, 2))
            .average()
            .orElse(0.0));

        // Definir los límites para identificar outliers (3 desviaciones estándar)
        double lowerBound = mean - 3 * stdDev;
        double upperBound = mean + 3 * stdDev;

        // Filtrar valores normales
        List<Double> filteredLatencies = latencies.stream()
            .filter(latency -> latency >= lowerBound && latency <= upperBound)
            .collect(Collectors.toList());

        // Identificar valores atípicos
        List<Double> outliers = latencies.stream()
            .filter(latency -> latency < lowerBound || latency > upperBound)
            .collect(Collectors.toList());

        // Retornar los resultados en un mapa
        Map<String, List<Double>> result = new HashMap<>();
        result.put("filtered", filteredLatencies);
        result.put("outliers", outliers);

        return result;
    }
}
