package com.example.restapi.model;

public class Compra {
    private String email;  // Cambiar de id a email
    private int conciertoId;
    private int cantidad;
    private String tipoEntrada;
    private double precioTotal;

    // Getters y setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getConciertoId() {
        return conciertoId;
    }

    public void setConciertoId(int conciertoId) {
        this.conciertoId = conciertoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipoEntrada() {
        return tipoEntrada;
    }

    public void setTipoEntrada(String tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
    
}
