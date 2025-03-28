package com.example.restapi.model;

public class Concierto {
    private static long idCounter = 0;
    private final long id;
    private String nombre;
    private String fecha;
    private String lugar;
    private String artista;
    private String descripcion;
    private String precio;
    private String aforo;
    private String imagen;
    private String categoria;
    private String duracion;
    private String edadMinima;
    private TipoAsiento tipoAsiento;

    public Concierto() {
        this.id = ++idCounter;
    }

    public Concierto(String nombre, String fecha, String lugar, String artista) {
        this.id = ++idCounter;
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
        this.artista = artista;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getAforo() {
        return aforo;
    }

    public void setAforo(String aforo) {
        this.aforo = aforo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(String edadMinima) {
        this.edadMinima = edadMinima;
    }

    public TipoAsiento getTipoAsiento() {
        return tipoAsiento;
    }

    public void setTipoAsiento(TipoAsiento tipoAsiento) {
        this.tipoAsiento = tipoAsiento;
    }

    @Override
    public String toString() {
        return "Concierto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fecha='" + fecha + '\'' +
                ", lugar='" + lugar + '\'' +
                ", artista='" + artista + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio='" + precio + '\'' +
                ", aforo='" + aforo + '\'' +
                ", imagen='" + imagen + '\'' +
                ", categoria='" + categoria + '\'' +
                ", duracion='" + duracion + '\'' +
                ", edadMinima='" + edadMinima + '\'' +
                ", tipoAsiento=" + tipoAsiento +
                '}';
    }
}