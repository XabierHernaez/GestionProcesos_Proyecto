package com.example.restapi.client.window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.example.restapi.model.Usuario;
import com.example.restapi.model.Concierto;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.example.restapi.model.Compra;

public class VentanaCompra extends JFrame {
    private static final long serialVersionUID = 1L;
    private Concierto concierto;
    private Usuario usuario;
    private JComboBox<String> comboTipoEntrada;
    private JTextField campoCantidad;
    private JLabel lblPrecioTotal, lblDispGeneral, lblDispVIP, lblDispPremium;
    private boolean compraEnProgreso = false; // Evitar clics múltiples

    public VentanaCompra(Concierto concierto, Usuario usuario) {
        this.concierto = concierto;
        this.usuario = usuario;

        setTitle("Compra de Entrada");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        // Panel superior con título
        JLabel lblTitulo = new JLabel("Compra de Entrada para " + concierto.getNombre(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel central con información y selección
        JPanel panelCentral = new JPanel(new GridLayout(7, 2, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelCentral.add(new JLabel("Lugar:"));
        panelCentral.add(new JLabel(concierto.getLugar()));

        panelCentral.add(new JLabel("Fecha:"));
        panelCentral.add(new JLabel(concierto.getFecha().toString()));

        // Mostrar disponibilidad directamente desde el concierto
        int dispGeneral = concierto.getCapacidadGeneral();
        int dispVIP = concierto.getCapacidadVIP();
        int dispPremium = concierto.getCapacidadPremium();

        panelCentral.add(new JLabel("Disponibilidad General:"));
        lblDispGeneral = new JLabel(dispGeneral + " entradas");
        panelCentral.add(lblDispGeneral);

        panelCentral.add(new JLabel("Disponibilidad VIP:"));
        lblDispVIP = new JLabel(dispVIP + " entradas");
        panelCentral.add(lblDispVIP);

        panelCentral.add(new JLabel("Disponibilidad Premium:"));
        lblDispPremium = new JLabel(dispPremium + " entradas");
        panelCentral.add(lblDispPremium);

        panelCentral.add(new JLabel("Tipo de Entrada:"));
        comboTipoEntrada = new JComboBox<>(new String[]{"General", "VIP", "Premium"});
        panelCentral.add(comboTipoEntrada);

        panelCentral.add(new JLabel("Cantidad:"));
        campoCantidad = new JTextField("1");
        panelCentral.add(campoCantidad);

        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con precio total y botón
        JPanel panelInferior = new JPanel(new GridLayout(2, 1, 5, 5));
        lblPrecioTotal = new JLabel("Precio Total: € " + String.format("%.2f", concierto.getPrecioGeneral()), SwingConstants.CENTER);
        panelInferior.add(lblPrecioTotal);

        JButton btnComprar = new JButton("Comprar");
        btnComprar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnComprar.setBackground(new Color(0, 123, 255));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setFocusPainted(false);
        btnComprar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnComprar.addActionListener(e -> realizarCompra());
        panelInferior.add(btnComprar);

        add(panelInferior, BorderLayout.SOUTH);

        // Actualizar precio total cuando se cambia el tipo de entrada o la cantidad
        comboTipoEntrada.addActionListener(e -> actualizarPrecioTotal());
        campoCantidad.addActionListener(e -> actualizarPrecioTotal());
        actualizarPrecioTotal();

        setVisible(true);
    }

    private double actualizarPrecioTotal() {
        double total = 0.0;
        try {
            int cantidad = Integer.parseInt(campoCantidad.getText());
            double precio = 0;

            switch (comboTipoEntrada.getSelectedItem().toString()) {
                case "General":
                    precio = concierto.getPrecioGeneral();
                    break;
                case "VIP":
                    precio = concierto.getPrecioVIP();
                    break;
                case "Premium":
                    precio = concierto.getPrecioPremium();
                    break;
            }

            total = precio * cantidad;
            lblPrecioTotal.setText("Precio Total: € " + String.format("%.2f", total));
        } catch (NumberFormatException e) {
            lblPrecioTotal.setText("Precio Total: € 0.00");
        }
        return total;
    }

    private void realizarCompra() {
        if (compraEnProgreso) {
            return; // Evitar múltiples clics
        }
        compraEnProgreso = true;

        Client client = null;
        Response response = null;

        try {
            int cantidad = Integer.parseInt(campoCantidad.getText());
            String tipoEntrada = (String) comboTipoEntrada.getSelectedItem();

            // Validar disponibilidad
            int dispGeneral = concierto.getCapacidadGeneral();
            int dispVIP = concierto.getCapacidadVIP();
            int dispPremium = concierto.getCapacidadPremium();

            int disponibles = 0;
            String tipoEntradaUpper = tipoEntrada.toUpperCase();
            switch (tipoEntradaUpper) {
                case "GENERAL":
                    disponibles = dispGeneral;
                    break;
                case "VIP":
                    disponibles = dispVIP;
                    break;
                case "PREMIUM":
                    disponibles = dispPremium;
                    break;
            }

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (cantidad > disponibles) {
                JOptionPane.showMessageDialog(this, "No hay suficientes entradas disponibles. Solo hay " + disponibles + " entradas de tipo " + tipoEntrada + ".", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear el objeto de compra
            Compra compra = new Compra();
            compra.setEmail(usuario.getEmail());
            compra.setConciertoId(concierto.getId());
            compra.setCantidad(cantidad);
            compra.setTipoEntrada(tipoEntradaUpper);
            compra.setPrecioTotal(actualizarPrecioTotal());

            // Log para depuración
            System.out.println("Enviando compra: conciertoId=" + concierto.getId() + ", tipoEntrada=" + tipoEntradaUpper + ", cantidad=" + cantidad);

            // Enviar al backend
            client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/compras";

            response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(compra, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                JOptionPane.showMessageDialog(this, "Compra realizada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                String mensajeError = response.readEntity(String.class);
                JOptionPane.showMessageDialog(this, "Error al realizar la compra: " + mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al realizar la compra: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            compraEnProgreso = false;
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }
}
