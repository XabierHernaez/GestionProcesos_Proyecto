package com.example.restapi.model;

import java.sql.Date;

/**
 * @class Usuario
 * @brief Clase que representa un usuario del sistema.
 * 
 * Esta clase contiene la información personal, datos de acceso y tipo de usuario.
 * También almacena el método de pago preferido.
 */
public class Usuario {
    /**< Nombre del usuario */
    private String nombre;
    
    /**< Apellidos del usuario */
    private String apellidos;
    
    /**< Email único que identifica al usuario */
    private String email;
    
    /**< Contraseña para autenticación */
    private String password;
    
    /**< Número de teléfono del usuario */
    private String telefono;
    
    /**< Documento Nacional de Identidad */
    private Long dni;
    
    /**< Fecha de nacimiento del usuario */
    private Date fechaNacimiento;
    
    /**< Método de pago preferido */
    private TipoPago tipoPago;
    
    /**< Tipo de usuario (por ejemplo, administrador, cliente) */
    private TipoUsuario tipoUsuario;

    /**
     * Constructor vacío.
     */
    public Usuario() {}

    /**
     * Constructor con email y tipo de usuario.
     * 
     * @param email [in] Email del usuario.
     * @param tipoUsuario [in] Tipo de usuario.
     */
    public Usuario(String email, TipoUsuario tipoUsuario) {
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Constructor con todos los parámetros.
     * 
     * @param nombre [in] Nombre del usuario.
     * @param apellidos [in] Apellidos del usuario.
     * @param email [in] Email del usuario.
     * @param password [in] Contraseña del usuario.
     * @param telefono [in] Teléfono del usuario.
     * @param dni [in] Documento Nacional de Identidad.
     * @param fechaNacimiento [in] Fecha de nacimiento.
     * @param tipoUsuario [in] Tipo de usuario.
     */
    public Usuario(String nombre, String apellidos, String email, String password, String telefono, Long dni, Date fechaNacimiento, TipoUsuario tipoUsuario) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoUsuario = tipoUsuario;
    }

    /// Getters y Setters

    /**
     * Obtiene el tipo de usuario.
     * @return tipoUsuario
     */
    public TipoUsuario getTipoUsuario(){
        return tipoUsuario;
    }

    /**
     * Establece el tipo de usuario.
     * @param tipoUsuario [in] Nuevo tipo de usuario.
     */
    public void setTipoUsuario(TipoUsuario tipoUsuario){
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Obtiene el nombre.
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre.
     * @param nombre [in] Nuevo nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos.
     * @return apellidos
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos.
     * @param apellidos [in] Nuevos apellidos.
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el email.
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el email.
     * @param email [in] Nuevo email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña.
     * @param password [in] Nueva contraseña.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el teléfono.
     * @return telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono.
     * @param telefono [in] Nuevo teléfono.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el DNI.
     * @return dni
     */
    public Long getDni() {
        return dni;
    }

    /**
     * Establece el DNI.
     * @param dni [in] Nuevo DNI.
     */
    public void setDni(Long dni) {
        this.dni = dni;
    }

    /**
     * Obtiene la fecha de nacimiento.
     * @return fechaNacimiento
     */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Establece la fecha de nacimiento.
     * @param fechaNacimiento [in] Nueva fecha de nacimiento.
     */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Obtiene el tipo de pago preferido.
     * @return tipoPago
     */
    public TipoPago getTipoPago() {
        return tipoPago;
    }

    /**
     * Establece el tipo de pago preferido.
     * @param tipoPago [in] Nuevo tipo de pago.
     */
    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    /**
     * Representación en String del usuario.
     * @return Cadena con datos del usuario.
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", dni=" + dni +
                ", fechaNacimiento=" + fechaNacimiento +
                ", tipoPago=" + tipoPago +
                '}';
    }
}
