package com.example.restapi.client.window;

import javax.swing.*;
import java.awt.*;
import com.example.restapi.model.Concierto;

public class VentanaConciertoDetalle extends JFrame {
    private static final long serialVersionUID = 1L;

    public VentanaConciertoDetalle(Concierto concierto) {
        setTitle("Detalles del Concierto");
        setSize(500, 500);
        setLocationRelativeTo(null); // Centra la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Márgenes
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE); // Fondo blanco

        // Título
        JLabel lblTitulo = new JLabel("Detalles del Concierto");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panel.add(lblTitulo);

        // Panel de información
        panel.add(crearLabel("ID: " + concierto.getId()));
        panel.add(crearLabel("Nombre: " + concierto.getNombre()));
        panel.add(crearLabel("Lugar: " + concierto.getLugar()));
        panel.add(crearLabel("Fecha: " + concierto.getFecha()));
        panel.add(crearLabel("Precio General: " + concierto.getPrecioGeneral() + " €"));
        panel.add(crearLabel("Precio VIP: " + concierto.getPrecioVIP() + " €"));
        panel.add(crearLabel("Precio Premium: " + concierto.getPrecioPremium() + " €"));

        panel.add(Box.createVerticalStrut(30)); // Espacio antes del botón

        // Botón para cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.setBackground(new Color(59, 89, 182)); // Azul bonito
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnCerrar.addActionListener(e -> dispose());
        panel.add(btnCerrar);

        add(panel);
        setVisible(true);
    }

    // Método para crear etiquetas estilizadas
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Espacio entre labels
        return label;
    }
}

