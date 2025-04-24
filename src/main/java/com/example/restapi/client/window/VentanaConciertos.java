package com.example.restapi.client.window;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.example.restapi.model.Concierto;
import com.example.restapi.model.Usuario;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class VentanaConciertos extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable tablaConciertos;
    private DefaultTableModel modeloTabla;
    private JButton btnComprar, btnActualizar;
    private Usuario usuario;

    public VentanaConciertos(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Eventos Disponibles");
        setSize(900, 600); // Tamaño ampliado para la tabla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());

        // Crear modelo de tabla con columnas
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Lugar", "Fecha", "Precio General", "Precio VIP", "Precio Premium"}, 
                0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición directa
            }
        };
        
        tablaConciertos = new JTable(modeloTabla);
        tablaConciertos.getColumnModel().getColumn(0).setPreferredWidth(30); // ID más pequeño
        tablaConciertos.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre más ancho
        tablaConciertos.getColumnModel().getColumn(2).setPreferredWidth(150); // Lugar
        tablaConciertos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tablaConciertos);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de información del concierto seleccionado
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitulo = new JLabel("Seleccione un evento para ver detalles");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelInfo.add(lblTitulo);
        panelInfo.add(Box.createVerticalStrut(20));
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnComprar = new JButton("Comprar Entrada");
        btnActualizar = new JButton("Actualizar Lista");
        
        panelBotones.add(btnComprar);
        panelBotones.add(btnActualizar);
        
        add(panelBotones, BorderLayout.SOUTH);

        // Configurar acciones de botones
        btnComprar.addActionListener(e -> {
            int filaSeleccionada = tablaConciertos.getSelectedRow();
            if (filaSeleccionada != -1) {
                // Obtener el concierto completo por ID
                int idConcierto = (int) tablaConciertos.getValueAt(filaSeleccionada, 0);
                Concierto conciertoSeleccionado = obtenerConciertoPorId(idConcierto);
                
                if (conciertoSeleccionado != null) {
                    new VentanaCompra(conciertoSeleccionado, usuario);
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Por favor, selecciona un evento.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnActualizar.addActionListener(e -> actualizarTabla());

        // Cargar datos iniciales
        actualizarTabla();
        
        setVisible(true);
    }

    private void actualizarTabla() {
        // Limpiar la tabla antes de actualizar
        modeloTabla.setRowCount(0);
        
        try {
            // Crear cliente REST
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/api/conciertos";
            
            // Realizar la solicitud GET al servidor
            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            // Manejar la respuesta del servidor
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                // Leer la lista de conciertos desde la respuesta
                List<Concierto> conciertos = response.readEntity(
                        new GenericType<List<Concierto>>() {}
                );
                
                for (Concierto concierto : conciertos) {
                    modeloTabla.addRow(new Object[]{
                            concierto.getId(),
                            concierto.getNombre(),
                            concierto.getLugar(),
                            concierto.getFecha(),
                            concierto.getPrecioGeneral(),
                            concierto.getPrecioVIP(),
                            concierto.getPrecioPremium()
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Error al cargar conciertos. Código: " + response.getStatus(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            client.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error al conectar con el servidor: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Concierto obtenerConciertoPorId(int id) {
        try {
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/api/conciertos/" + id;
            
            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                Concierto concierto = response.readEntity(Concierto.class);
                return concierto;
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Error al obtener detalles del concierto. Código: " + response.getStatus(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error al conectar con el servidor: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}