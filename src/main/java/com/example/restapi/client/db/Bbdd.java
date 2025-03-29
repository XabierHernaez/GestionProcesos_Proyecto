package com.example.restapi.client.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


import com.example.restapi.model.Concierto;
import com.example.restapi.model.Usuario;

public class Bbdd {
    private static Connection conn;
    private static final String DRIVER = "org.sqlite.JDBC";

    /* METODO QUE REALIZA LA CONEXION A LA BASE DE DATOS */
    public static void initBD(String nombreBD) {
        if (conn != null) {
            return; // Si ya está abierta, no hacemos nada
        }
        String connectionString = String.format("jdbc:sqlite:resources/db/%s.db", nombreBD);
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(connectionString);
            System.out.println("Conexión a la base de datos establecida exitosamente.");
            crearTablas(); // Creamos las tablas al conectar
        } catch (ClassNotFoundException e) {
            System.err.println("No se encontró el driver SQLite: " + e.getMessage());
            throw new RuntimeException("Error al cargar el driver SQLite", e);
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            throw new RuntimeException("Error al conectar a la base de datos", e);
        }
    }

    /* MÉTODO QUE REALIZA EL CIERRE DE LA BASE DE DATOS */
    public static void closeBD() {
        if (conn != null) {
            try {
                conn.close();
                conn = null; // Marcamos como null después de cerrar
                System.out.println("Conexión a la base de datos cerrada.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para verificar si la conexión está abierta
    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            throw new SQLException("Conexión no inicializada. Llama a initBD primero.");
        }
        return conn;
    }
    public static void crearTablas() throws SQLException {
    	getConnection();
        try {
        	String sqlUsuario = "CREATE TABLE IF NOT EXISTS Usuarios (" +
        	        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        	        "nombre TEXT NOT NULL, " +
        	        "apellido TEXT NOT NULL, " +
        	        "email TEXT UNIQUE NOT NULL, " +
        	        "password_hash TEXT NOT NULL, " +
        	        "telefono INTEGER, " +
        	        "dni TEXT UNIQUE, " +
        	        "fecha_nacimiento DATETIME DEFAULT CURRENT_TIMESTAMP, " +
        	        "tipo_usuario TEXT CHECK(tipo_usuario IN ('ADMIN', 'CLIENTE')) DEFAULT 'CLIENTE')";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUsuario)) {
                pstmt.execute();
            }
            String sqlConciertos = "CREATE TABLE IF NOT EXISTS Conciertos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT NOT NULL, " +
                    "lugar TEXT NOT NULL, " +
                    "fecha DATETIME NOT NULL, " +
                    "capacidad_general INTEGER DEFAULT 100, " + // Capacidad para GENERAL
                    "capacidad_vip INTEGER DEFAULT 50, " +      // Capacidad para VIP
                    "capacidad_premium INTEGER DEFAULT 20, " +  // Capacidad para PREMIUM
                    "precio_general REAL NOT NULL, " +
                    "precio_vip REAL NOT NULL, " +
                    "precio_premium REAL NOT NULL)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlConciertos)) {
                pstmt.execute();
            }
        } catch (SQLException e) {
            System.err.format("\n* Error al crear las tablas: %s", e.getMessage());
            throw e;
        }
    }

    // Insertar un usuario
    public static int insertUsuario(String nombre, String apellido, String email, String password, int telefono, String dni, Usuario.TipoUsuario tipoUsuario) throws SQLException {
        getConnection();
        String sql = "INSERT INTO Usuarios (nombre, apellido, email, password_hash, telefono, dni, tipo_usuario) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setInt(5, telefono);
            stmt.setString(6, dni);
            stmt.setString(7, tipoUsuario.name());
            stmt.executeUpdate();
    
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        }
    }
    // Insertar un evento
    public static int insertConcierto(String nombre, String lugar, java.util.Date fecha, int capacidadGeneral, int capacidadVIP, int capacidadPremium, 
        double precioGeneral, double precioVIP, double precioPremium) throws SQLException {
        getConnection();
        String sql = "INSERT INTO Conciertos (nombre, lugar, fecha, capacidad_general, capacidad_vip, capacidad_premium, precio_general, precio_vip, precio_premium) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nombre);
            stmt.setString(2, lugar);
            stmt.setTimestamp(3, new Timestamp(fecha.getTime()));
            stmt.setInt(4, capacidadGeneral);
            stmt.setInt(5, capacidadVIP);
            stmt.setInt(6, capacidadPremium);
            stmt.setDouble(7, precioGeneral);
            stmt.setDouble(8, precioVIP);
            stmt.setDouble(9, precioPremium);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        }
        }
}
