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

/**
 * @class VentanaConciertos
 * @brief Ventana principal donde se listan los conciertos disponibles.
 *
 * Permite al usuario filtrar conciertos, ver detalles, comprar entradas y acceder a sus compras.
 */
public class VentanaConciertos extends JFrame {
    private static final long serialVersionUID = 1L;
    
    /**< Tabla para mostrar la lista de conciertos */
    private JTable tablaConciertos;
    /**< Modelo de datos para la tabla */
    private DefaultTableModel modeloTabla;
    /**< Botones para las diferentes acciones de la interfaz */
    private JButton btnComprar, btnActualizar, btnDetalle, btnVolverVentanaPrincipal, btnFiltrarFecha, btnFiltrarAmbos, btnMisCopras;
    /**< Usuario autenticado en la aplicación */
    private Usuario usuario;
    /**< Campos de texto para los filtros de búsqueda */
    private JTextField campoFiltroLugar, campoFiltroFecha;

    /**
     * @brief Constructor de la clase. Recibe el usuario autenticado (si lo hay).
     *
     * @param usuario Usuario autenticado o null.
     */
    public VentanaConciertos(Usuario usuario) {
        this.usuario = usuario;
        setTitle("🎵 Eventos Disponibles 🎵");
        setSize(1200, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // Panel superior con el título
        JLabel lblTitulo = new JLabel("Lista de Conciertos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel de filtros de búsqueda
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelFiltro.setBackground(Color.WHITE);

        // Campo para filtrar por lugar
        campoFiltroLugar = new JTextField(20);
        campoFiltroLugar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoFiltroLugar.setToolTipText("Filtrar por lugar");
        campoFiltroLugar.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Botón para filtrar por lugar
        JButton btnFiltrar = new JButton("🔍 Filtrar");
        configurarBoton(btnFiltrar);
        btnFiltrar.addActionListener(e -> filtrarPorLugar());

        // Campo para filtrar por fecha
        campoFiltroFecha = new JTextField(20);
        campoFiltroFecha.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoFiltroFecha.setToolTipText("Filtrar por fecha (formato: yyyy-mm-dd)");
        campoFiltroFecha.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Botón para filtrar por fecha
        btnFiltrarFecha = new JButton("🔍 Filtrar Fecha");
        configurarBoton(btnFiltrarFecha);
        btnFiltrarFecha.addActionListener(e -> filtrarPorFecha());

        // Botón para filtrar por lugar y fecha
        btnFiltrarAmbos = new JButton("🎯 Filtrar Ambos");
        configurarBoton(btnFiltrarAmbos);
        btnFiltrarAmbos.addActionListener(e -> filtrarPorLugarYFecha());

        // Agregar campos y botones de filtro al panel
        panelFiltro.add(new JLabel("Lugar:"));
        panelFiltro.add(campoFiltroLugar);
        panelFiltro.add(btnFiltrar);
        panelFiltro.add(new JLabel("Fecha:"));
        panelFiltro.add(campoFiltroFecha);
        panelFiltro.add(btnFiltrarFecha);
        panelFiltro.add(btnFiltrarAmbos);

        add(panelFiltro, BorderLayout.NORTH);

        // Modelo de la tabla con las columnas definidas
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Lugar", "Fecha", "Precio General", "Disp. General",
                             "Precio VIP", "Disp. VIP", "Precio Premium", "Disp. Premium"}, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Tabla de conciertos
        tablaConciertos = new JTable(modeloTabla);
        tablaConciertos.setRowHeight(30);
        tablaConciertos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tablaConciertos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        tablaConciertos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tablaConciertos);
        scrollPane.setBorder(new EmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones inferiores
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        panelBotones.setBackground(Color.WHITE);

        // Inicialización de botones
        btnComprar = new JButton("Comprar Entrada");
        btnActualizar = new JButton("Actualizar Lista");
        btnDetalle = new JButton("Ver Detalle");
        btnVolverVentanaPrincipal = new JButton("Volver");
        btnMisCopras = new JButton("Mis Compras");

        // Configuración y añadido de botones
        configurarBoton(btnComprar);
        configurarBoton(btnActualizar);
        configurarBoton(btnDetalle);
        configurarBoton(btnVolverVentanaPrincipal);
        configurarBoton(btnMisCopras);

        panelBotones.add(btnComprar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnDetalle);
        panelBotones.add(btnVolverVentanaPrincipal);
        panelBotones.add(btnMisCopras);

        add(panelBotones, BorderLayout.SOUTH);

        // Eventos asociados a los botones
        btnComprar.addActionListener(e -> comprarEntrada());
        btnActualizar.addActionListener(e -> actualizarTabla());
        btnDetalle.addActionListener(e -> verDetalle());
        btnVolverVentanaPrincipal.addActionListener(e -> {
            dispose();
            new MenuPrincipal();
        });
        btnMisCopras.addActionListener(e -> verMisCompras());

        // Carga inicial de conciertos
        actualizarTabla();

        setVisible(true);
    }

    /**
     * @brief Configura la apariencia de los botones.
     *
     * @param boton Botón a configurar.
     */
    private void configurarBoton(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setBackground(new Color(0, 123, 255));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * @brief Muestra la ventana con las compras del usuario autenticado.
     */
    private void verMisCompras() {
        if (usuario == null) {
            int respuesta = JOptionPane.showConfirmDialog(this,
                    "Debes iniciar sesión para ver tus compras. ¿Quieres iniciar sesión?",
                    "Iniciar Sesión", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                new VentanaInicio();
                dispose();
            }
        } else {
            new VentanaMisCompras(usuario);
        }
    }

    /**
     * @brief Abre la ventana de compra si hay una fila seleccionada.
     */
    private void comprarEntrada() {
        int filaSeleccionada = tablaConciertos.getSelectedRow();
        if (filaSeleccionada != -1) {
            int idConcierto = (int) tablaConciertos.getValueAt(filaSeleccionada, 0);
            Concierto conciertoSeleccionado = obtenerConciertoPorId(idConcierto);
            if (conciertoSeleccionado != null) {
                if (usuario == null) {
                    int respuesta = JOptionPane.showConfirmDialog(this,
                            "Debes iniciar sesión para realizar la compra. ¿Quieres iniciar sesión?",
                            "Iniciar Sesión", JOptionPane.YES_NO_OPTION);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        new VentanaInicio();
                        dispose();
                    }
                } else {
                    new VentanaCompra(conciertoSeleccionado, usuario);
                }
            }
        } else {
            mostrarMensajeError("Por favor, selecciona un evento.");
        }
    }

    /**
     * @brief Muestra la ventana de detalle del concierto seleccionado.
     */
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

    /**
     * @brief Muestra un mensaje de error mediante un cuadro de diálogo.
     *
     * @param mensaje Texto del mensaje.
     */
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * @brief Carga los conciertos desde la API y actualiza la tabla.
     */
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);  // Limpiar la tabla
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
                            String.format("€ %.2f", concierto.getPrecioGeneral()),
                            concierto.getCapacidadGeneral(),
                            String.format("€ %.2f", concierto.getPrecioVIP()),
                            concierto.getCapacidadVIP(),
                            String.format("€ %.2f", concierto.getPrecioPremium()),
                            concierto.getCapacidadPremium()
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

    /**
     * @brief Consulta los detalles de un concierto por su ID.
     *
     * @param id ID del concierto.
     * @return Objeto Concierto o null si falla.
     */
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

    /**
     * @brief Filtra los conciertos por lugar usando el valor del campo de texto.
     */
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
                                String.format("€ %.2f", concierto.getPrecioGeneral()),
                                concierto.getCapacidadGeneral(),
                                String.format("€ %.2f", concierto.getPrecioVIP()),
                                concierto.getCapacidadVIP(),
                                String.format("€ %.2f", concierto.getPrecioPremium()),
                                concierto.getCapacidadPremium()
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

    /**
     * @brief Filtra los conciertos por fecha.
     */
    private void filtrarPorFecha() {
        String fechaStr = campoFiltroFecha.getText().trim();
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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                for (Concierto concierto : conciertos) {
                    String fechaFormateada = sdf.format(concierto.getFecha());
                    if (fechaFormateada.equals(fechaStr)) {
                        modeloTabla.addRow(new Object[]{
                                concierto.getId(),
                                concierto.getNombre(),
                                concierto.getLugar(),
                                concierto.getFecha(),
                                String.format("€ %.2f", concierto.getPrecioGeneral()),
                                concierto.getCapacidadGeneral(),
                                String.format("€ %.2f", concierto.getPrecioVIP()),
                                concierto.getCapacidadVIP(),
                                String.format("€ %.2f", concierto.getPrecioPremium()),
                                concierto.getCapacidadPremium()
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

    /**
     * @brief Filtra los conciertos por lugar y fecha simultáneamente.
     */
    private void filtrarPorLugarYFecha() {
        String lugar = campoFiltroLugar.getText().trim().toLowerCase();
        String fechaStr = campoFiltroFecha.getText().trim();

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

                    boolean coincideLugar = lugar.isEmpty() || lugarConcierto.contains(lugar);
                    boolean coincideFecha = fechaStr.isEmpty() || fechaConcierto.equals(fechaStr);

                    if (coincideLugar && coincideFecha) {
                        modeloTabla.addRow(new Object[]{
                                concierto.getId(),
                                concierto.getNombre(),
                                concierto.getLugar(),
                                concierto.getFecha(),
                                String.format("€ %.2f", concierto.getPrecioGeneral()),
                                concierto.getCapacidadGeneral(),
                                String.format("€ %.2f", concierto.getPrecioVIP()),
                                concierto.getCapacidadVIP(),
                                String.format("€ %.2f", concierto.getPrecioPremium()),
                                concierto.getCapacidadPremium()
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