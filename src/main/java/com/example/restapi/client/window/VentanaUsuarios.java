package com.example.restapi.client.window;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.example.restapi.model.Usuario;
import java.util.List;

public class VentanaUsuarios extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private Client client;

    // Constructor original (para producción)
    public VentanaUsuarios() {
        this(ClientBuilder.newClient());
    }

    // Constructor extra (para testing)
    public VentanaUsuarios(Client client) {
        this.client = client;
        setTitle("Listado de Usuarios");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Establecer una apariencia moderna para el fondo
        getContentPane().setBackground(new Color(245, 245, 245));

        // Crear el modelo de la tabla con cabeceras estilizadas
        modelo = new DefaultTableModel(new String[]{
                "Nombre", "Apellidos", "Email", "Password", "Teléfono", "DNI", "Fecha Nacimiento", "Tipo Pago", "Tipo Usuario"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Crear la tabla con un estilo más moderno
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(30);
        tabla.setGridColor(new Color(204, 204, 204));
        tabla.setSelectionBackground(new Color(42, 87, 141)); // Color de selección en azul
        tabla.setSelectionForeground(Color.WHITE); // Texto blanco al seleccionar

        // Usar un panel de desplazamiento con un borde suave
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));
        add(scrollPane, BorderLayout.CENTER);

        // Panel para el botón de cerrar
        JPanel panelBoton = new JPanel();
        panelBoton.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setBackground(new Color(245, 245, 245)); // Fondo claro del panel
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.setBackground(new Color(242, 85, 96));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.setPreferredSize(new Dimension(100, 35));

        // Añadir acción al botón de cerrar
        btnCerrar.addActionListener(e -> dispose());

        panelBoton.add(btnCerrar);
        add(panelBoton, BorderLayout.SOUTH);

        // Cargar los usuarios desde la API
        cargarUsuarios();
        setVisible(true);
    }

    private void cargarUsuarios() {
        try {
            String apiUrl = "http://localhost:8080/api/usuarios";

            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                List<Usuario> usuarios = response.readEntity(new GenericType<List<Usuario>>() {});
                modelo.setRowCount(0);

                for (Usuario u : usuarios) {
                    modelo.addRow(new Object[]{
                            u.getNombre(),
                            u.getApellidos(),
                            u.getEmail(),
                            u.getPassword(),
                            u.getTelefono(),
                            u.getDni(),
                            u.getFechaNacimiento(),
                            u.getTipoPago(),
                            u.getTipoUsuario()
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al cargar usuarios. Código: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JTable getTabla() {
        return tabla;
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }
}