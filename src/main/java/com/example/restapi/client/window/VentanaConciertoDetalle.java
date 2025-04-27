package com.example.restapi.client.window;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.example.restapi.model.Concierto;

public class VentanaConciertoDetalle extends JFrame {
    private static final long serialVersionUID = 1L;

    public VentanaConciertoDetalle(Concierto concierto) {
        setTitle("Detalles del Concierto");
        setSize(400, 400);
        setLocationRelativeTo(null); // Centra la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Layout y panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Etiquetas para mostrar los detalles del concierto
        JLabel lblId = new JLabel("ID: " + concierto.getId());
        JLabel lblNombre = new JLabel("Nombre: " + concierto.getNombre());
        JLabel lblLugar = new JLabel("Lugar: " + concierto.getLugar());
        JLabel lblFecha = new JLabel("Fecha: " + concierto.getFecha());
        JLabel lblPrecioGeneral = new JLabel("Precio General: " + concierto.getPrecioGeneral());
        JLabel lblPrecioVIP = new JLabel("Precio VIP: " + concierto.getPrecioVIP());
        JLabel lblPrecioPremium = new JLabel("Precio Premium: " + concierto.getPrecioPremium());

        // Añadir las etiquetas al panel
        panel.add(lblId);
        panel.add(lblNombre);
        panel.add(lblLugar);
        panel.add(lblFecha);
        panel.add(lblPrecioGeneral);
        panel.add(lblPrecioVIP);
        panel.add(lblPrecioPremium);

        // Botón para cerrar la ventana
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose()); // Cierra la ventana
        panel.add(btnCerrar);

        add(panel);
        setVisible(true);
    }
}
