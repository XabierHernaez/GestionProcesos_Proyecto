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

public class VentanaCompra extends JFrame{
    private static final long serialVersionUID = 1L;
    private Concierto concierto;
    private Usuario usuario;
    private JComboBox<String> comboTipoEntrada;
    private JTextField campoCantidad;
    private JLabel lblPrecioTotal;

    public VentanaCompra(Concierto concierto, Usuario usuario) {
        this.concierto = concierto;
        this.usuario = usuario;

        setTitle("Compra de Entrada");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior con título
        JLabel lblTitulo = new JLabel("Compra de Entrada para " + concierto.getNombre(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel de información del concierto
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new GridLayout(4, 2, 10, 10));
        panelInfo.add(new JLabel("Lugar:"));
        panelInfo.add(new JLabel(concierto.getLugar()));
        panelInfo.add(new JLabel("Fecha:"));
        panelInfo.add(new JLabel(concierto.getFecha().toString()));
        panelInfo.add(new JLabel("Precio General:"));
        panelInfo.add(new JLabel(String.format("€ %.2f", concierto.getPrecioGeneral())));
        panelInfo.add(new JLabel("Precio VIP:"));
        panelInfo.add(new JLabel(String.format("€ %.2f", concierto.getPrecioVIP())));
        
        add(panelInfo, BorderLayout.CENTER);
        
        // Panel de selección de tipo de entrada y cantidad
        JPanel panelEntrada = new JPanel();
        panelEntrada.setLayout(new GridLayout(3, 2, 10, 10));
        
        panelEntrada.add(new JLabel("Tipo de Entrada:"));
        
        comboTipoEntrada = new JComboBox<>(new String[] { "General", "VIP", "Premium" });
        panelEntrada.add(comboTipoEntrada);
        
        panelEntrada.add(new JLabel("Cantidad:"));
        campoCantidad = new JTextField("1");
        panelEntrada.add(campoCantidad);
        
        panelEntrada.add(new JLabel("Precio Total:"));
        lblPrecioTotal = new JLabel("€ " + concierto.getPrecioGeneral());
        panelEntrada.add(lblPrecioTotal);
        
        add(panelEntrada, BorderLayout.CENTER);

        // Botón para realizar la compra
        JPanel panelBoton = new JPanel();
        JButton btnComprar = new JButton("Comprar");
        btnComprar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnComprar.setBackground(new Color(0, 123, 255));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setFocusPainted(false);
        btnComprar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnComprar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnComprar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarCompra();
            }
        });
        panelBoton.add(btnComprar);
        add(panelBoton, BorderLayout.SOUTH);
        
        // Actualizar precio total cuando se cambia el tipo de entrada o la cantidad
        comboTipoEntrada.addActionListener(e -> actualizarPrecioTotal());
        campoCantidad.addActionListener(e -> actualizarPrecioTotal());

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
            lblPrecioTotal.setText("€ " + total);
        } catch (NumberFormatException e) {
            lblPrecioTotal.setText("€ 0.00");
        }
        return total;
    }

    private void realizarCompra() {
        Client client = null;
        Response response = null;
        
        try {
            // Crear un cliente HTTP
            client = ClientBuilder.newClient();
            
            // Crear el objeto de compra con el email del usuario
            Compra compra = new Compra();
            compra.setEmail(usuario.getEmail());  // Usar el email del usuario
            compra.setConciertoId(concierto.getId());  // ID del concierto
            compra.setCantidad(Integer.parseInt(campoCantidad.getText()));  // Obtener la cantidad de entradas
            compra.setTipoEntrada((String) comboTipoEntrada.getSelectedItem());  // Obtener el tipo de entrada
            compra.setPrecioTotal(actualizarPrecioTotal());  // Calcular el precio total
    
            // Crear la URL de la API para realizar la compra
            String apiUrl = "http://localhost:8080/compras";  // Ajusta la URL si es necesario
            
            // Enviar la solicitud POST con los detalles de la compra
            response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(compra, MediaType.APPLICATION_JSON));
    
            // Verificar la respuesta de la API
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                JOptionPane.showMessageDialog(this, "Compra realizada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();  // Cerrar la ventana de compra
            } else {
                JOptionPane.showMessageDialog(this, "Error al realizar la compra. Código: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            // Mostrar el mensaje de error si ocurre alguna excepción
            JOptionPane.showMessageDialog(this, "Error al realizar la compra: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Cerrar la respuesta y el cliente
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }
}
