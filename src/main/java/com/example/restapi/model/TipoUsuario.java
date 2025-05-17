package com.example.restapi.model;

/**
 * @enum TipoUsuario
 * @brief Enum que representa los diferentes tipos de usuario en el sistema.
 *
 * Este enum define los roles posibles que un usuario puede tener:
 * - ADMIN: Usuario con permisos administrativos.
 * - CLIENTE: Usuario registrado que puede comprar entradas.
 * - ANONIMO: Usuario no registrado o visitante sin privilegios.
 */
public enum TipoUsuario {
    /** Usuario con permisos administrativos */
    ADMIN,

    /** Usuario registrado que puede comprar entradas */
    CLIENTE,

    /** Usuario no registrado o visitante sin privilegios */
    ANONIMO
}
