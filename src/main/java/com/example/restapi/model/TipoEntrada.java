package com.example.restapi.model;

/**
 * @enum TipoEntrada
 * @brief Enum que representa los diferentes tipos de entradas para los conciertos.
 *
 * Define las categorías de entradas disponibles, cada una con diferentes características y precios:
 * - GENERAL: Entrada estándar.
 * - VIP: Entrada con acceso a zonas VIP y beneficios adicionales.
 * - PREMIUM: Entrada premium con los mejores accesos y servicios exclusivos.
 */
public enum TipoEntrada {
    /** Entrada estándar */
    GENERAL,

    /** Entrada con acceso a zonas VIP y beneficios adicionales */
    VIP,

    /** Entrada premium con los mejores accesos y servicios exclusivos */
    PREMIUM;
}