package com.example.restapi.client.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.example.restapi.model.Concierto;

/**
 * @class VentanaConciertoDetalle
 * @brief Ventana que muestra los detalles de un concierto específico.
 *
 * Esta interfaz gráfica permite visualizar información detallada de un concierto, como su nombre,
 * lugar, fecha y precios de los distintos tipos de entradas.
 */
public class VentanaConciertoDetalle extends JFrame {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor que crea e inicializa la ventana con la información del concierto.
     * 
     * @param concierto [in] Objeto Concierto cuyos datos se mostrarán.
     */
    public VentanaConciertoDetalle(Concierto concierto) {
        setTitle("Detalles del Concierto");
        setSize(500, 500);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel principal que contiene todos los elementos
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Márgenes internos
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE); // Fondo blanco para una apariencia limpia

        // Título de la ventana
        JLabel lblTitulo = new JLabel("Detalles del Concierto");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panel.add(lblTitulo);

        // Agregar información del concierto
        panel.add(crearLabel("ID: " + concierto.getId()));
        panel.add(crearLabel("Nombre: " + concierto.getNombre()));
        panel.add(crearLabel("Lugar: " + concierto.getLugar()));
        panel.add(crearLabel("Fecha: " + concierto.getFecha()));
        panel.add(crearLabel("Precio General: " + concierto.getPrecioGeneral() + " €"));
        panel.add(crearLabel("Precio VIP: " + concierto.getPrecioVIP() + " €"));
        panel.add(crearLabel("Precio Premium: " + concierto.getPrecioPremium() + " €"));

        panel.add(Box.createVerticalStrut(30)); // Espacio antes del botón

        // Botón para cerrar la ventana
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.setBackground(new Color(59, 89, 182)); // Color azul personalizado
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnCerrar.addActionListener(e -> dispose());
        panel.add(btnCerrar);

        add(panel);
        setVisible(true);
    }

    /**
     * Crea una etiqueta (JLabel) estilizada con el texto proporcionado.
     *
     * @param texto [in] Texto que se mostrará en la etiqueta.
     * @return JLabel con el estilo aplicado.
     */
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Espacio vertical
        return label;
    }
}