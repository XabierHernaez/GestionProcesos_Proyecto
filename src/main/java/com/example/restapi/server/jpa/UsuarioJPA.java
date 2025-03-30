package com.example.restapi.server.jpa;


import jakarta.persistence.*;
import com.example.restapi.model.TipoPago;
import com.example.restapi.model.TipoUsuario;

import java.sql.Date;

@Entity
@Table(name = "usuarios")
public class UsuarioJPA {

    @Id
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Long dni;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String telefono;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_nacimiento", nullable = false)
    private Date fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago", nullable = false)
    private TipoPago tipoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario;

    public UsuarioJPA() {
    }

    public UsuarioJPA(long dni, String nombre, String apellidos, String email, String password, String telefono,
                      Date fechaNacimiento, TipoPago tipoPago, TipoUsuario tipoUsuario) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoPago = tipoPago;
        this.tipoUsuario = tipoUsuario;
    }

    // Getters y setters
    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public TipoPago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public String toString() {
        return "UsuarioJPA{" +
                "dni=" + dni +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", telefono='" + telefono + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", tipoPago=" + tipoPago +
                ", tipoUsuario=" + tipoUsuario +
                '}';
    }
}