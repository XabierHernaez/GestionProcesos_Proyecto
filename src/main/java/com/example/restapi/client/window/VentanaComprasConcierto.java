package com.example.restapi.client.window;

import com.example.restapi.model.Compra;
import com.example.restapi.model.Concierto;
import com.example.restapi.model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.util.List;

public class VentanaComprasConcierto extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable tablaCompras;
    private DefaultTableModel modeloTabla;
    private Concierto concierto;
    private Usuario admin;

    public VentanaComprasConcierto(Concierto concierto, Usuario admin) {
        this.concierto = concierto;
        this.admin = admin;

        setTitle("Compras para " + concierto.getNombre());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Título
        JLabel lblTitulo = new JLabel("Lista de Compras - " + concierto.getNombre(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Modelo de la tabla
        modeloTabla = new DefaultTableModel(
                new String[]{"Email", "Tipo Entrada", "Cantidad", "Precio Total"},
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCompras = new JTable(modeloTabla);
        tablaCompras.setRowHeight(25);
        tablaCompras.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaCompras.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaCompras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tablaCompras);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Botón Cerrar
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setBackground(new Color(0, 123, 255));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        // Cargar compras
        cargarCompras();

        setVisible(true);
    }

    private void cargarCompras() {
        try {
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/compras/concierto/" + concierto.getId() + "?adminEmail=" + admin.getEmail();
            System.out.println("Llamando a la URL: " + apiUrl);

            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            System.out.println("Código de estado HTTP: " + response.getStatus());

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String jsonResponse = response.readEntity(String.class);
                System.out.println("Respuesta JSON: " + jsonResponse);
                response = client.target(apiUrl).request(MediaType.APPLICATION_JSON).get(); // Reenviar solicitud
                List<Compra> compras = response.readEntity(new GenericType<List<Compra>>() {});
                modeloTabla.setRowCount(0);
                for (Compra compra : compras) {
                    System.out.println("Procesando compra: email=" + compra.getEmail() +
                            ", tipoEntrada=" + compra.getTipoEntrada() +
                            ", cantidad=" + compra.getCantidad() +
                            ", precioTotal=" + compra.getPrecioTotal());
                    modeloTabla.addRow(new Object[]{
                            compra.getEmail(),
                            compra.getTipoEntrada(),
                            compra.getCantidad(),
                            compra.getPrecioTotal()
                    });
                }
                if (compras.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hay compras para este concierto.", "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                String errorBody = response.readEntity(String.class);
                System.out.println("Cuerpo del error: " + errorBody);
                JOptionPane.showMessageDialog(this, "Error al cargar las compras. Código: " + response.getStatus() + ", Mensaje: " + errorBody, "Error", JOptionPane.ERROR_MESSAGE);
            }

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
