package com.example.restapi.model;

/**
 * @enum TipoPago
 * @brief Enum que representa los métodos de pago disponibles para las compras.
 *
 * Opciones disponibles para realizar pagos en la plataforma:
 * - Visa: Pago mediante tarjeta Visa.
 * - Paypal: Pago mediante la plataforma PayPal.
 * - Mastercard: Pago mediante tarjeta Mastercard.
 * - Transferencia: Pago mediante transferencia bancaria.
 * - Criptomonedas: Pago mediante monedas digitales (criptomonedas).
 */
public enum TipoPago {
    /** Pago mediante tarjeta Visa */
    Visa,

    /** Pago mediante la plataforma PayPal */
    Paypal,

    /** Pago mediante tarjeta Mastercard */
    Mastercard,

    /** Pago mediante transferencia bancaria */
    Transferencia,

    /** Pago mediante monedas digitales (criptomonedas) */
    Criptomonedas
}
