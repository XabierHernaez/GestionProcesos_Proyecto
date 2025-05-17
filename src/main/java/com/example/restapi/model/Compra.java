package com.example.restapi.model;

/**
 * @class Compra
 * @brief Representa una compra de entradas para un concierto realizada por un usuario.
 * 
 * Contiene información del comprador, concierto, cantidad de entradas, tipo de entrada y precio total.
 */
public class Compra {
    /**< Email del usuario que realiza la compra */
    private String email;
    
    /**< Identificador del concierto */
    private int conciertoId;
    
    /**< Cantidad de entradas compradas */
    private int cantidad;
    
    /**< Tipo de entrada adquirida (e.g. GENERAL, VIP, PREMIUM) */
    private String tipoEntrada;
    
    /**< Precio total de la compra */
    private double precioTotal;

    /** 
     * Obtiene el email del comprador.
     * @return Email del usuario.
     */
    public String getEmail() {
        return email;
    }

    /** 
     * Establece el email del comprador.
     * @param email [in] Email del usuario.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /** 
     * Obtiene el ID del concierto.
     * @return Identificador del concierto.
     */
    public int getConciertoId() {
        return conciertoId;
    }

    /** 
     * Establece el ID del concierto.
     * @param conciertoId [in] Identificador del concierto.
     */
    public void setConciertoId(int conciertoId) {
        this.conciertoId = conciertoId;
    }

    /** 
     * Obtiene la cantidad de entradas compradas.
     * @return Cantidad de entradas.
     */
    public int getCantidad() {
        return cantidad;
    }

    /** 
     * Establece la cantidad de entradas compradas.
     * @param cantidad [in] Cantidad de entradas.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /** 
     * Obtiene el tipo de entrada.
     * @return Tipo de entrada (e.g., GENERAL, VIP, PREMIUM).
     */
    public String getTipoEntrada() {
        return tipoEntrada;
    }

    /** 
     * Establece el tipo de entrada.
     * @param tipoEntrada [in] Tipo de entrada.
     */
    public void setTipoEntrada(String tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

    /** 
     * Obtiene el precio total de la compra.
     * @return Precio total.
     */
    public double getPrecioTotal() {
        return precioTotal;
    }

    /** 
     * Establece el precio total de la compra.
     * @param precioTotal [in] Precio total.
     */
    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
}
