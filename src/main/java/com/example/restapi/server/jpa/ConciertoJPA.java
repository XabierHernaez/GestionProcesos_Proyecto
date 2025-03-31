package com.example.restapi.server.jpa;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "conciertos")
public class ConciertoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String lugar;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fecha;

    @Column(name = "capacidad_general", nullable = false)
    private int capacidadGeneral;

    @Column(name = "capacidad_vip", nullable = false)
    private int capacidadVIP;

    @Column(name = "capacidad_premium", nullable = false)
    private int capacidadPremium;

    @Column(name = "precio_general", nullable = false)
    private double precioGeneral;

    @Column(name = "precio_vip", nullable = false)
    private double precioVIP;

    @Column(name = "precio_premium", nullable = false)
    private double precioPremium;

    public ConciertoJPA() {
        this.capacidadGeneral = 100;
        this.capacidadVIP = 50;
        this.capacidadPremium = 20;
        this.precioGeneral = 0.0;
        this.precioVIP = 0.0;
        this.precioPremium = 0.0;
    }

    public ConciertoJPA(int id, String nombre, String lugar, Date fecha, int capacidadGeneral, int capacidadVIP, int capacidadPremium,
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

    // Getters y setters
    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id;
     }
    public String getNombre() {
         return nombre;
         }
    public void setNombre(String nombre) {
         this.nombre = nombre; 
        }
    public String getLugar() {
         return lugar; 
        }
    public void setLugar(String lugar) {
         this.lugar = lugar; 
        }
    public Date getFecha() {
         return fecha; 
        }
    public void setFecha(Date fecha) {
         this.fecha = fecha; 
        }
    public int getCapacidadGeneral() {
         return capacidadGeneral; 
        }
    public void setCapacidadGeneral(int capacidadGeneral) {
         this.capacidadGeneral = capacidadGeneral; 
        }
    public int getCapacidadVIP() {
         return capacidadVIP; 
        }
    public void setCapacidadVIP(int capacidadVIP) {
         this.capacidadVIP = capacidadVIP; 
        }
    public int getCapacidadPremium() {
         return capacidadPremium; 
        }
    public void setCapacidadPremium(int capacidadPremium) {
         this.capacidadPremium = capacidadPremium; 
        }
    public double getPrecioGeneral() {
         return precioGeneral; 
        }
    public void setPrecioGeneral(double precioGeneral) {
         this.precioGeneral = precioGeneral; 
        }
    public double getPrecioVIP() {
         return precioVIP; 
        }
    public void setPrecioVIP(double precioVIP) {
         this.precioVIP = precioVIP; 
        }
    public double getPrecioPremium() {
         return precioPremium; 
        }
    public void setPrecioPremium(double precioPremium) {
         this.precioPremium = precioPremium; 
        }

    @Override
    public String toString() {
        return nombre + " - " + lugar + " (" + fecha + ")";
    }
}
