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

public class VentanaMisCompras extends JFrame {
    private Usuario usuario;

    public VentanaMisCompras(Usuario usuario) {
        this.usuario = usuario;

        // Establecer un look and feel moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Mis Compras");
        setSize(800, 500); // Aumentamos el tamaño para que se vea mejor
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Cambiar color de fondo
        getContentPane().setBackground(new Color(230, 230, 255));

        // Titulo de la ventana
        JLabel titleLabel = new JLabel("Mis Compras", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 70, 150));
        titleLabel.setPreferredSize(new Dimension(800, 40));

        // Tabla con un modelo
        String[] columnas = {"Concierto", "Lugar", "Fecha", "Tipo Entrada", "Cantidad"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(25);
        tabla.setSelectionBackground(new Color(100, 149, 237)); // Color para cuando se selecciona una fila
        tabla.setSelectionForeground(Color.WHITE);
        
        // Agregar un border y bordes redondeados
        tabla.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(750, 350));

        // Formato de fecha esperado en el JSON
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

        // Llamada a la API
        Client client = ClientBuilder.newClient();
        String apiUrl = "http://localhost:8080/compras/usuario?email=" + usuario.getEmail();

        try {
            Response response = client
                    .target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == 200) {
                List<Map<String, Object>> compras = response.readEntity(new GenericType<List<Map<String, Object>>>() {});

                for (Map<String, Object> compra : compras) {
                    Map<String, Object> concierto = (Map<String, Object>) compra.get("concierto");

                    String nombre = String.valueOf(concierto.get("nombre"));
                    String lugar = String.valueOf(concierto.get("lugar"));
                    String fechaStr = String.valueOf(concierto.get("fecha"));
                    String tipoEntrada = String.valueOf(compra.get("tipoEntrada"));
                    int cantidad = Integer.parseInt(compra.get("cantidad").toString());

                    // Parsear fecha
                    Date fecha = formatoFecha.parse(fechaStr);

                    modelo.addRow(new Object[]{nombre, lugar, formatoFecha.format(fecha), tipoEntrada, cantidad});
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al obtener compras: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener compras: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            client.close();
        }

        // Layout del panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Añadir el panel al JFrame
        add(panel);

        // Hacer visible la ventana
        setVisible(true);
    }
}
