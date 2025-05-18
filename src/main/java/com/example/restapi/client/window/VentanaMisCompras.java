package com.example.restapi.client.window;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.example.restapi.model.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.awt.*;

/**
 * Ventana que muestra las compras realizadas por un usuario.
 * Recupera los datos desde una API REST y los presenta en una tabla.
 */
public class VentanaMisCompras extends JFrame {
    private Usuario usuario;

    /**
     * Constructor que inicializa la ventana y carga las compras del usuario.
     * @param usuario El usuario que ha iniciado sesión y cuyas compras se mostrarán.
     */
    public VentanaMisCompras(Usuario usuario) {
        this.usuario = usuario;

        // Establecer un look and feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Configuración básica de la ventana
        setTitle("Mis Compras");
        setSize(800, 500); // Tamaño de la ventana
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Establecer el color de fondo
        getContentPane().setBackground(new Color(230, 230, 255));

        // Etiqueta de título
        JLabel titleLabel = new JLabel("Mis Compras", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 70, 150));
        titleLabel.setPreferredSize(new Dimension(800, 40));

        // Definición de columnas de la tabla
        String[] columnas = {"Concierto", "Lugar", "Fecha", "Tipo Entrada", "Cantidad"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(25);
        tabla.setSelectionBackground(new Color(100, 149, 237)); // Color al seleccionar una fila
        tabla.setSelectionForeground(Color.WHITE);

        // Añadir borde a la tabla
        tabla.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Scroll para la tabla
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(750, 350));

        // Formato esperado de las fechas
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

        // Crear cliente REST
        Client client = ClientBuilder.newClient();
        String apiUrl = "http://localhost:8080/compras/usuario?email=" + usuario.getEmail();

        try {
            // Solicitud GET para obtener las compras del usuario
            Response response = client
                    .target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            // Si la respuesta es exitosa, procesar la lista de compras
            if (response.getStatus() == 200) {
                List<Map<String, Object>> compras = response.readEntity(new GenericType<List<Map<String, Object>>>() {});

                // Para cada compra, obtener la información del concierto y mostrarla en la tabla
                for (Map<String, Object> compra : compras) {
                    Integer conciertoId = (Integer) compra.get("conciertoId");

                    // Nueva solicitud GET para obtener detalles del concierto
                    String conciertoUrl = "http://localhost:8080/api/conciertos/" + conciertoId;
                    Map<String, Object> concierto = client
                            .target(conciertoUrl)
                            .request(MediaType.APPLICATION_JSON)
                            .get(new GenericType<Map<String, Object>>() {});

                    // Extraer y formatear los datos del concierto y la compra
                    String nombre = String.valueOf(concierto.get("nombre"));
                    String lugar = String.valueOf(concierto.get("lugar"));
                    String fechaStr = String.valueOf(concierto.get("fecha"));
                    String tipoEntrada = String.valueOf(compra.get("tipoEntrada"));
                    int cantidad = Integer.parseInt(compra.get("cantidad").toString());

                    // Parsear la fecha
                    Date fecha = formatoFecha.parse(fechaStr);

                    // Añadir la fila a la tabla
                    modelo.addRow(new Object[]{nombre, lugar, formatoFecha.format(fecha), tipoEntrada, cantidad});
                }
            } else {
                // Mostrar error si la API devuelve un estado distinto a 200
                JOptionPane.showMessageDialog(this, "Error al obtener compras: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            // Mostrar error en caso de excepción
            JOptionPane.showMessageDialog(this, "Error al obtener compras: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Cerrar el cliente REST
            client.close();
        }

        // Crear panel y establecer layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Añadir el panel a la ventana
        add(panel);

        // Hacer visible la ventana
        setVisible(true);
    }
}
