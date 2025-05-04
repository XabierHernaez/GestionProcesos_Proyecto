package com.example.restapi.client.window;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.example.restapi.model.Concierto;
import com.example.restapi.model.Usuario;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;


public class VentanaConciertos extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable tablaConciertos;
    private DefaultTableModel modeloTabla;
    private JButton btnComprar, btnActualizar, btnDetalle, btnVolverVentanaPrincipal, btnFiltrarFecha, btnFiltrarAmbos;
    private Usuario usuario;
    private JTextField campoFiltroLugar, campoFiltroFecha;


    public VentanaConciertos(Usuario usuario) {
        this.usuario = usuario;
        setTitle("🎵 Eventos Disponibles 🎵");
        setSize(1200, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // Panel superior con título
        JLabel lblTitulo = new JLabel("Lista de Conciertos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel de filtro (lugar y fecha en el mismo panel)
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelFiltro.setBackground(Color.WHITE);
        
        // Panel de filtro por lugar
        campoFiltroLugar = new JTextField(20);
        campoFiltroLugar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoFiltroLugar.setToolTipText("Filtrar por lugar");
        campoFiltroLugar.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JButton btnFiltrar = new JButton("🔍 Filtrar");
        configurarBoton(btnFiltrar);
        btnFiltrar.addActionListener(e -> filtrarPorLugar());

        // Panel de filtro por fecha
        campoFiltroFecha = new JTextField(20);
        campoFiltroFecha.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoFiltroFecha.setToolTipText("Filtrar por fecha (formato: yyyy-mm-dd)");
        campoFiltroFecha.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        btnFiltrarFecha = new JButton("🔍 Filtrar Fecha");
        configurarBoton(btnFiltrarFecha);
        btnFiltrarFecha.addActionListener(e -> filtrarPorFecha());

        //Boton para filtrar ambos
        btnFiltrarAmbos = new JButton("🎯 Filtrar Ambos");
        configurarBoton(btnFiltrarAmbos);
        btnFiltrarAmbos.addActionListener(e -> filtrarPorLugarYFecha());

        // Agregar componentes al panel
        panelFiltro.add(new JLabel("Lugar:"));
        panelFiltro.add(campoFiltroLugar);
        panelFiltro.add(btnFiltrar);

        panelFiltro.add(new JLabel("Fecha:"));
        panelFiltro.add(campoFiltroFecha);
        panelFiltro.add(btnFiltrarFecha);
      
        panelFiltro.add(btnFiltrarAmbos);

        // Colocar panel de filtro en la parte superior
        add(panelFiltro, BorderLayout.NORTH);

        // Crear modelo de tabla
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Lugar", "Fecha", "Precio General", "Precio VIP", "Precio Premium"},
                0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Tabla
        tablaConciertos = new JTable(modeloTabla);
        tablaConciertos.setRowHeight(30);
        tablaConciertos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tablaConciertos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        tablaConciertos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tablaConciertos);
        scrollPane.setBorder(new EmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        panelBotones.setBackground(Color.WHITE);

        btnComprar = new JButton("Comprar Entrada");
        btnActualizar = new JButton("Actualizar Lista");
        btnDetalle = new JButton("Ver Detalle");
        btnVolverVentanaPrincipal = new JButton("Volver");

        configurarBoton(btnComprar);
        configurarBoton(btnActualizar);
        configurarBoton(btnDetalle);
        configurarBoton(btnVolverVentanaPrincipal);

        panelBotones.add(btnComprar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnDetalle);
        panelBotones.add(btnVolverVentanaPrincipal);

        add(panelBotones, BorderLayout.SOUTH);

        // Eventos de botones
        btnComprar.addActionListener(e -> comprarEntrada());
        btnActualizar.addActionListener(e -> actualizarTabla());
        btnDetalle.addActionListener(e -> verDetalle());
        btnVolverVentanaPrincipal.addActionListener(e -> {
            dispose();
            new MenuPrincipal();
        });

        // Cargar datos iniciales
        actualizarTabla();

        setVisible(true);
    }

    private void configurarBoton(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setBackground(new Color(0, 123, 255));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void comprarEntrada() {
        int filaSeleccionada = tablaConciertos.getSelectedRow();
        if (filaSeleccionada != -1) {
            int idConcierto = (int) tablaConciertos.getValueAt(filaSeleccionada, 0);
            Concierto conciertoSeleccionado = obtenerConciertoPorId(idConcierto);
            if (conciertoSeleccionado != null) {
                if (usuario == null) {
                    // Si no hay usuario logueado, redirigir a la ventana de inicio de sesión
                    int respuesta = JOptionPane.showConfirmDialog(this, 
                            "Debes iniciar sesión para realizar la compra. ¿Quieres iniciar sesión?", 
                            "Iniciar Sesión", JOptionPane.YES_NO_OPTION);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        // Abre la ventana de inicio de sesión (suponiendo que la tienes)
                        new VentanaInicio();
                        dispose();
                    }
                } else {
                    // Si el usuario está logueado, continuar con la compra
                    new VentanaCompra(conciertoSeleccionado, usuario);
                    dispose(); // Cierra la ventana actual
                }
            }
        } else {
            mostrarMensajeError("Por favor, selecciona un evento.");
        }
    }

    private void verDetalle() {
        int filaSeleccionada = tablaConciertos.getSelectedRow();
        if (filaSeleccionada != -1) {
            int idConcierto = (int) tablaConciertos.getValueAt(filaSeleccionada, 0);
            Concierto conciertoSeleccionado = obtenerConciertoPorId(idConcierto);
            if (conciertoSeleccionado != null) {
                new VentanaConciertoDetalle(conciertoSeleccionado);
            }
        } else {
            mostrarMensajeError("Por favor, selecciona un evento para ver detalles.");
        }
    }

    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        try {
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/api/conciertos";

            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                List<Concierto> conciertos = response.readEntity(new GenericType<List<Concierto>>() {});
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
                mostrarMensajeError("Error al cargar conciertos. Código: " + response.getStatus());
            }

            client.close();
        } catch (Exception e) {
            mostrarMensajeError("Error al conectar con el servidor: " + e.getMessage());
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
                return response.readEntity(Concierto.class);
            } else {
                mostrarMensajeError("Error al obtener detalles del concierto. Código: " + response.getStatus());
                return null;
            }
        } catch (Exception e) {
            mostrarMensajeError("Error al conectar con el servidor: " + e.getMessage());
            return null;
        }
    }

    private void filtrarPorLugar() {
        String lugar = campoFiltroLugar.getText().trim().toLowerCase();
        if (lugar.isEmpty()) {
            actualizarTabla();
            return;
        }
    
        modeloTabla.setRowCount(0);
        try {
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/api/conciertos";
    
            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
    
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                List<Concierto> conciertos = response.readEntity(new GenericType<List<Concierto>>() {});
                for (Concierto concierto : conciertos) {
                    if (concierto.getLugar().toLowerCase().contains(lugar)) {
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
                }
            } else {
                mostrarMensajeError("Error al cargar conciertos. Código: " + response.getStatus());
            }
    
            client.close();
        } catch (Exception e) {
            mostrarMensajeError("Error al conectar con el servidor: " + e.getMessage());
        }
    }

    private void filtrarPorFecha() {
        String fechaStr = campoFiltroFecha.getText().trim(); // Formato esperado: yyyy-MM-dd
        if (fechaStr.isEmpty()) {
            actualizarTabla();
            return;
        }
    
        modeloTabla.setRowCount(0);
        try {
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/api/conciertos";
    
            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
    
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                List<Concierto> conciertos = response.readEntity(new GenericType<List<Concierto>>() {});
                
                // Formato para comparar
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
                for (Concierto concierto : conciertos) {
                    String fechaFormateada = sdf.format(concierto.getFecha());
                    if (fechaFormateada.equals(fechaStr)) {
                        modeloTabla.addRow(new Object[]{
                                concierto.getId(),
                                concierto.getNombre(),
                                concierto.getLugar(),
                                concierto.getFecha(),  // o usa fechaFormateada si prefieres mostrarlo más limpio
                                concierto.getPrecioGeneral(),
                                concierto.getPrecioVIP(),
                                concierto.getPrecioPremium()
                        });
                    }
                }
            } else {
                mostrarMensajeError("Error al cargar conciertos. Código: " + response.getStatus());
            }
    
            client.close();
        } catch (Exception e) {
            mostrarMensajeError("Error al conectar con el servidor: " + e.getMessage());
        }
    } 

    private void filtrarPorLugarYFecha() {
        String lugar = campoFiltroLugar.getText().trim().toLowerCase();
        String fechaStr = campoFiltroFecha.getText().trim(); // formato yyyy-MM-dd
    
        if (lugar.isEmpty() && fechaStr.isEmpty()) {
            actualizarTabla();
            return;
        }
    
        modeloTabla.setRowCount(0);
        try {
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/api/conciertos";
    
            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
    
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                List<Concierto> conciertos = response.readEntity(new GenericType<List<Concierto>>() {});
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
                for (Concierto concierto : conciertos) {
                    String lugarConcierto = concierto.getLugar().toLowerCase();
                    String fechaConcierto = sdf.format(concierto.getFecha());
    
                    boolean coincideLugar = !lugar.isEmpty() && lugarConcierto.contains(lugar);
                    boolean coincideFecha = !fechaStr.isEmpty() && fechaConcierto.equals(fechaStr);
    
                    if (coincideLugar && coincideFecha) {
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
                }
            } else {
                mostrarMensajeError("Error al cargar conciertos. Código: " + response.getStatus());
            }
    
            client.close();
        } catch (Exception e) {
            mostrarMensajeError("Error al conectar con el servidor: " + e.getMessage());
        }
    }   
    
}
