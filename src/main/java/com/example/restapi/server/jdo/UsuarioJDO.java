package com.example.restapi.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import com.example.restapi.model.TipoPago;
import com.example.restapi.model.TipoUsuario;
import java.sql.Date;



@PersistenceCapable

public class UsuarioJDO {
    Long dni = null;
    String nombre = null;
    String apellidos = null;
    @PrimaryKey
    String email= null;
    String password = null;
    String telefono = null;
    Date fechaNacimiento = null;
    TipoPago tipoPago = null;
    TipoUsuario tipoUsuario = null;

   


    public UsuarioJDO(long dni, String nombre, String apellidos, String email, String password, String telefono, 
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

    public UsuarioJDO() {
    }
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
    
    public String toString() {
        return "UsuarioJDO [dni=" + dni + ", nombre=" + nombre + ", apellidos=" + apellidos + ", email=" + email
                + ", password=" + password + ", telefono=" + telefono + ", fechaNacimiento=" + fechaNacimiento
                + ", tipoPago=" + tipoPago + ", tipoUsuario=" + tipoUsuario + "]";
    }


    
}
