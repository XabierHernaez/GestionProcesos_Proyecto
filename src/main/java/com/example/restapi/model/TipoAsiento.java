package com.example.restapi.model;

/**
 * @enum TipoAsiento
 * @brief Enum que representa los tipos de asientos disponibles en un concierto.
 *
 * Tipos de asientos que determinan la ubicación y posiblemente los servicios asociados:
 * - VIP: Asientos en la zona VIP, con mejores comodidades.
 * - GENERAL: Asientos estándar en la zona general.
 */
public enum TipoAsiento {
    /** Asiento en zona VIP con mejores comodidades */
    VIP,

    /** Asiento estándar en zona general */
    GENERAL
}
