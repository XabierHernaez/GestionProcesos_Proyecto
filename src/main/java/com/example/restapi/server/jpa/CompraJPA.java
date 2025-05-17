package com.example.restapi.server.jpa;
import jakarta.persistence.*;

@Entity
@Table(name = "compras")
public class CompraJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_email")
    private UsuarioJPA usuario;

    @ManyToOne
    @JoinColumn(name = "concierto_id")
    private ConciertoJPA concierto;

    @Column(name = "tipo_entrada")
    private String tipoEntrada;

    private int cantidad;

    public CompraJPA() {}

    public CompraJPA(UsuarioJPA usuario, ConciertoJPA concierto, String tipoEntrada, int cantidad) {
        this.usuario = usuario;
        this.concierto = concierto;
        this.tipoEntrada = tipoEntrada;
        this.cantidad = cantidad;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioJPA getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioJPA usuario) {
        this.usuario = usuario;
    }

    public ConciertoJPA getConcierto() {
        return concierto;
    }

    public void setConcierto(ConciertoJPA concierto) {
        this.concierto = concierto;
    }

    public String getTipoEntrada() {
        return tipoEntrada;
    }

    public void setTipoEntrada(String tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
