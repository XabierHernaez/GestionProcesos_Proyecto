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
    private Client client; // <-- nuevo atributo

    // Constructor original (para producción)
    public VentanaUsuarios() {
        this(ClientBuilder.newClient());
    }

    // Constructor extra (para testing)
    public VentanaUsuarios(Client client) {
        this.client = client;
        setTitle("Listado de Usuarios");
        setSize(1000, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new String[]{
                "Nombre", "Apellidos", "Email", "Password", "Teléfono", "DNI", "Fecha Nacimiento", "Tipo Pago", "Tipo Usuario"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);
        add(panelBoton, BorderLayout.SOUTH);

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