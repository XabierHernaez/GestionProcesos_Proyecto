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
 * @class VentanaMisCompras
 * @brief Ventana gráfica que muestra las compras realizadas por un usuario autenticado.
 *
 * Esta clase construye una interfaz con Swing que se conecta a una API REST para obtener
 * las compras realizadas por un usuario y las muestra en una tabla. Realiza múltiples
 * solicitudes GET para recuperar tanto los datos de las compras como los detalles de los conciertos.
 */
public class VentanaMisCompras extends JFrame {

    /**< Usuario autenticado cuyas compras se mostrarán */
    private Usuario usuario;

    /**
     * @brief Constructor que inicializa la ventana y carga las compras del usuario.
     * 
     * Configura la apariencia de la ventana, realiza llamadas REST para obtener las
     * compras y muestra los datos en una tabla.
     *
     * @param usuario El usuario que ha iniciado sesión y cuyas compras se mostrarán.
     */
    public VentanaMisCompras(Usuario usuario) {
        this.usuario = usuario;

        // Establecer el look and feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Configuración de la ventana
        setTitle("Mis Compras");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(230, 230, 255));

        // Etiqueta de título
        JLabel titleLabel = new JLabel("Mis Compras", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 70, 150));
        titleLabel.setPreferredSize(new Dimension(800, 40));

        // Columnas de la tabla
        String[] columnas = {"Concierto", "Lugar", "Fecha", "Tipo Entrada", "Cantidad"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(25);
        tabla.setSelectionBackground(new Color(100, 149, 237));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(750, 350));

        // Formato para las fechas
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

        // Cliente REST para llamadas HTTP
        Client client = ClientBuilder.newClient();
        String apiUrl = "http://localhost:8080/compras/usuario?email=" + usuario.getEmail();

        try {
            // Obtener compras del usuario
            Response response = client
                    .target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == 200) {
                List<Map<String, Object>> compras = response.readEntity(new GenericType<List<Map<String, Object>>>() {});

                // Para cada compra, obtener los detalles del concierto asociado
                for (Map<String, Object> compra : compras) {
                    Integer conciertoId = (Integer) compra.get("conciertoId");

                    // Solicitud para obtener detalles del concierto
                    String conciertoUrl = "http://localhost:8080/api/conciertos/" + conciertoId;
                    Map<String, Object> concierto = client
                            .target(conciertoUrl)
                            .request(MediaType.APPLICATION_JSON)
                            .get(new GenericType<Map<String, Object>>() {});

                    // Extraer los datos necesarios
                    String nombre = String.valueOf(concierto.get("nombre"));
                    String lugar = String.valueOf(concierto.get("lugar"));
                    String fechaStr = String.valueOf(concierto.get("fecha"));
                    String tipoEntrada = String.valueOf(compra.get("tipoEntrada"));
                    int cantidad = Integer.parseInt(compra.get("cantidad").toString());

                    // Parseo de fecha y añadido de fila a la tabla
                    Date fecha = formatoFecha.parse(fechaStr);
                    modelo.addRow(new Object[]{nombre, lugar, formatoFecha.format(fecha), tipoEntrada, cantidad});
                }
            } else {
                // Mostrar mensaje de error si falla la solicitud
                JOptionPane.showMessageDialog(this, "Error al obtener compras: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            // Captura de errores durante el proceso de solicitud y parseo
            JOptionPane.showMessageDialog(this, "Error al obtener compras: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Cierre del cliente REST
            client.close();
        }

        // Construcción del panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
        setVisible(true);
    }
}
