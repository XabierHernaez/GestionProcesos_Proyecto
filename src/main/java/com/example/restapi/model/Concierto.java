package com.example.restapi.model;

import java.util.Date;

/**
 * @class Concierto
 * @brief Representa un concierto con información básica, capacidades y precios según tipo de entrada.
 * 
 * Esta clase contiene detalles como nombre, lugar, fecha, capacidades por tipo de entrada y precios.
 */
public class Concierto {
    /**< Identificador único del concierto */
    private int id;
    
    /**< Nombre del concierto */
    private String nombre;
    
    /**< Lugar donde se realiza el concierto */
    private String lugar;
    
    /**< Fecha del concierto */
    private Date fecha;
    
    /**< Capacidad de entradas generales disponibles */
    private int capacidadGeneral;
    
    /**< Capacidad de entradas VIP disponibles */
    private int capacidadVIP;
    
    /**< Capacidad de entradas Premium disponibles */
    private int capacidadPremium;
    
    /**< Precio de la entrada general */
    private double precioGeneral;
    
    /**< Precio de la entrada VIP */
    private double precioVIP;
    
    /**< Precio de la entrada Premium */
    private double precioPremium;

    /**
     * Constructor con todos los parámetros.
     * 
     * @param id [in] Identificador del concierto.
     * @param nombre [in] Nombre del concierto.
     * @param lugar [in] Lugar del concierto.
     * @param fecha [in] Fecha del concierto.
     * @param capacidadGeneral [in] Capacidad entradas generales.
     * @param capacidadVIP [in] Capacidad entradas VIP.
     * @param capacidadPremium [in] Capacidad entradas Premium.
     * @param precioGeneral [in] Precio entrada general.
     * @param precioVIP [in] Precio entrada VIP.
     * @param precioPremium [in] Precio entrada Premium.
     */
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

    /**
     * Constructor vacío que inicializa capacidades por defecto y precios a 0.
     */
    public Concierto() {
        this.capacidadGeneral = 100;
        this.capacidadVIP = 50;
        this.capacidadPremium = 20;
        this.precioGeneral = 0.0;
        this.precioVIP = 0.0;
        this.precioPremium = 0.0;
    }

    /// Getters y Setters

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

    /**
     * Obtiene la capacidad para un tipo de entrada dado.
     * 
     * @param tipo [in] Tipo de entrada (GENERAL, VIP, PREMIUM).
     * @return Capacidad disponible para ese tipo.
     */
    public int getCapacidad(TipoEntrada tipo) {
        switch (tipo) {
            case GENERAL: return capacidadGeneral;
            case VIP: return capacidadVIP;
            case PREMIUM: return capacidadPremium;
            default: return 0;
        }
    }

    /**
     * Obtiene el precio para un tipo de entrada dado.
     * 
     * @param tipo [in] Tipo de entrada (GENERAL, VIP, PREMIUM).
     * @return Precio para ese tipo de entrada.
     */
    public double getPrecio(TipoEntrada tipo) {
        switch (tipo) {
            case GENERAL: return precioGeneral;
            case VIP: return precioVIP;
            case PREMIUM: return precioPremium;
            default: return 0.0;
        }
    }

    /**
     * Representación en cadena del concierto.
     * 
     * @return Cadena con nombre, lugar y fecha.
     */
    @Override
    public String toString() {
        return nombre + " - " + lugar + " (" + fecha + ")";
    }
}
