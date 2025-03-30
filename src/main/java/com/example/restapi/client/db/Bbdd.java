package com.example.restapi.client.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.restapi.model.TipoUsuario;

import com.example.restapi.model.Concierto;
import com.example.restapi.model.TipoPago;
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
    public static int insertUsuario(String nombre, String apellido, String email, String password, int telefono, String dni, TipoUsuario tipoUsuario) throws SQLException {

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

        // Actualizar un evento
    public static void updateEvento(int id, String nombre, String lugar, java.util.Date fecha, int capacidadGeneral, int capacidadVIP, int capacidadPremium, 
    double precioGeneral, double precioVIP, double precioPremium) throws SQLException {
        getConnection();
        String sql = "UPDATE Conciertos SET nombre = ?, lugar = ?, fecha = ?, capacidad_general = ?, capacidad_vip = ?, capacidad_premium = ?, " +
        "precio_general = ?, precio_vip = ?, precio_premium = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, lugar);
            stmt.setTimestamp(3, new Timestamp(fecha.getTime()));
            stmt.setInt(4, capacidadGeneral);
            stmt.setInt(5, capacidadVIP);
            stmt.setInt(6, capacidadPremium);
            stmt.setDouble(7, precioGeneral);
            stmt.setDouble(8, precioVIP);
            stmt.setDouble(9, precioPremium);
            stmt.setInt(10, id);
        stmt.executeUpdate();
        }
    }
    
    // Eliminar un evento
    public static void deleteEvento(int id) throws SQLException {
    	getConnection();
        String sql = "DELETE FROM Conciertos WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public static boolean emailExists(String email) throws SQLException {
    	getConnection();
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        }

    }

    public static Usuario login(String email, String password) throws SQLException {
    getConnection(); // Asegúrate de que esta función esté correctamente implementada para obtener la conexión.
    String sql = "SELECT * FROM Usuarios WHERE email = ?";
    Usuario usuario = null;

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, email);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                // Obtener los valores de la base de datos
                String storedHash = rs.getString("password_hash");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String telefono = rs.getString("telefono");
                Long dni = rs.getLong("dni");
                Date fechaNacimiento = rs.getDate("fecha_nacimiento");
                String tipoUsuarioString = rs.getString("tipo_usuario");

                // Conversión de tipoPago y tipoUsuario
                TipoUsuario tipoUsuario = TipoUsuario.valueOf(tipoUsuarioString);

                // Crear el objeto Usuario usando el constructor
                usuario = new Usuario(nombre, apellido, email, storedHash, telefono, dni, fechaNacimiento, tipoUsuario);

            }
            return usuario;
        }
    }
    }

    public static void actualizarUsuarioDatos(String dni, String nombre, String apellido, String email, 
                                            String password, int telefono, String fechaNacimiento, 
                                            String tipoUsuario) throws SQLException {
        // Obtener la conexión a la base de datos
        Connection conn = getConnection();

        // SQL para actualizar todos los datos del usuario por DNI
        String sql = "UPDATE Usuarios SET nombre = ?, apellido = ?, email = ?, password_hash = ?, telefono = ?, " +
                    "fecha_nacimiento = ?, tipo_usuario = ? WHERE dni = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Establecer los parámetros en el PreparedStatement
            stmt.setString(1, nombre);               // Nombre del usuario
            stmt.setString(2, apellido);             // Apellido del usuario
            stmt.setString(3, email);                // Email del usuario
            stmt.setString(4, password);             // Contraseña del usuario
            stmt.setInt(5, telefono);                // Teléfono del usuario
            stmt.setString(6, fechaNacimiento);      // Fecha de nacimiento del usuario
            stmt.setString(7, tipoUsuario);          // Tipo de usuario (ADMIN/CLIENTE)
            stmt.setString(8, dni);                  // DNI del usuario a actualizar

            // Ejecutar la actualización en la base de datos
            int filasActualizadas = stmt.executeUpdate();

            // Verificar si se actualizó algún registro
            if (filasActualizadas == 0) {
                throw new SQLException("No se encontró ningún usuario con el DNI: " + dni);
            }
        }
    }

    // Obtener todos los eventos
    public static List<Concierto> getAllEventos() throws SQLException {
    	getConnection();
        List<Concierto> eventos = new ArrayList<>();
        String sql = "SELECT * FROM Conciertos";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Concierto evento = new Concierto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("lugar"),
                            rs.getTimestamp("fecha"),
                            rs.getInt("capacidad_general"),
                            rs.getInt("capacidad_vip"),
                            rs.getInt("capacidad_premium"),
                            rs.getDouble("precio_general"),
                            rs.getDouble("precio_vip"),
                            rs.getDouble("precio_premium")
                    );
                    eventos.add(evento);
                }
            }
        }
        return eventos;
    }
}
