package com.example.restapi.model;

import java.util.Date;

public class Concierto {
    private int id;
    private String nombre;
    private String lugar;
    private Date fecha;
    private int capacidadGeneral;
    private int capacidadVIP;
    private int capacidadPremium;
    private double precioGeneral;
    private double precioVIP;
    private double precioPremium;

    public Concierto(int id, String nombre, String lugar, Date fecha, int capacidadGeneral, int capacidadVIP, int capacidadPremium, 
                  double precioGeneral, double precioVIP, double precioPremium) {
        this.id = id;
        this.nombre = nombre;
        this.lugar = lugar;
        this.fecha = fecha;
        this.capacidadGeneral = capacidadGeneral;
        this.capacidadVIP = capacidadVIP;
        this.capacidadPremium = capacidadPremium;
        this.precioGeneral = precioGeneral;
        this.precioVIP = precioVIP;
        this.precioPremium = precioPremium;
    }

    public Concierto() {
        this.capacidadGeneral = 100;
        this.capacidadVIP = 50;
        this.capacidadPremium = 20;
        this.precioGeneral = 0.0;
        this.precioVIP = 0.0;
        this.precioPremium = 0.0;
    }

    // Getters y setters existentes
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public int getCapacidadGeneral() { return capacidadGeneral; }
    public void setCapacidadGeneral(int capacidadGeneral) { this.capacidadGeneral = capacidadGeneral; }
    public int getCapacidadVIP() { return capacidadVIP; }
    public void setCapacidadVIP(int capacidadVIP) { this.capacidadVIP = capacidadVIP; }
    public int getCapacidadPremium() { return capacidadPremium; }
    public void setCapacidadPremium(int capacidadPremium) { this.capacidadPremium = capacidadPremium; }
    public double getPrecioGeneral() { return precioGeneral; }
    public void setPrecioGeneral(double precioGeneral) { this.precioGeneral = precioGeneral; }
    public double getPrecioVIP() { return precioVIP; }
    public void setPrecioVIP(double precioVIP) { this.precioVIP = precioVIP; }
    public double getPrecioPremium() { return precioPremium; }
    public void setPrecioPremium(double precioPremium) { this.precioPremium = precioPremium; }

    public int getCapacidad(TipoEntrada tipo) {
        switch (tipo) {
            case GENERAL: return capacidadGeneral;
            case VIP: return capacidadVIP;
            case PREMIUM: return capacidadPremium;
            default: return 0;
        }
    }

    public double getPrecio(TipoEntrada tipo) {
        switch (tipo) {
            case GENERAL: return precioGeneral;
            case VIP: return precioVIP;
            case PREMIUM: return precioPremium;
            default: return 0.0;
        }
    }

    @Override
    public String toString() {
        return nombre + " - " + lugar + " (" + fecha + ")";
    }
}