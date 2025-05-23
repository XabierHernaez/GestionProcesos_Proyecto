package com.example.restapi.client.window;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.example.restapi.model.Usuario;
import java.util.Date;
import java.util.List;
import com.example.restapi.model.TipoUsuario;
import com.example.restapi.model.Concierto;

/**
 * @class VentanaAdmin
 * @brief Ventana de administración para gestionar eventos de la plataforma de conciertos.
 * 
 * Esta clase proporciona una interfaz gráfica para administradores, permitiendo agregar, editar, eliminar y visualizar conciertos, 
 * así como acceder a listas de usuarios y compras. Solo usuarios con tipo ADMIN pueden acceder a esta ventana. 
 * Utiliza componentes Swing para la interfaz y se conecta a una API REST para la gestión de datos.
 */
public class VentanaAdmin extends JFrame {
    /**< Identificador de serialización para la clase */
    private static final long serialVersionUID = 1L;

    /**< Tabla que muestra la lista de conciertos */
    protected JTable tablaEventos;

    /**< Modelo de datos para la tabla de conciertos */
    protected DefaultTableModel modeloTabla;

    /**< Campo de texto para el nombre del concierto */
    protected JTextField txtNombre;

    /**< Campo de texto para el lugar del concierto */
    protected JTextField txtLugar;

    /**< Campo de texto para el precio de entradas generales */
    protected JTextField txtPrecioGeneral;

    /**< Campo de texto para el precio de entradas VIP */
    protected JTextField txtPrecioVIP;

    /**< Campo de texto para el precio de entradas Premium */
    protected JTextField txtPrecioPremium;

    /**< Campo de texto para buscar conciertos por nombre */
    protected JTextField txtBuscar;

    /**< Selector de fecha para el concierto */
    protected JSpinner spinnerFecha;

    /**< Selector de capacidad para entradas generales */
    protected JSpinner spinnerCapacidadGeneral;

    /**< Selector de capacidad para entradas VIP */
    protected JSpinner spinnerCapacidadVIP;

    /**< Selector de capacidad para entradas Premium */
    protected JSpinner spinnerCapacidadPremium;

    /**< Botón para agregar un nuevo concierto */
    protected JButton btnAgregar;

    /**< Botón para editar un concierto existente */
    protected JButton btnEditar;

    /**< Botón para eliminar un concierto seleccionado */
    protected JButton btnEliminar;

    /**< Botón para buscar conciertos por nombre */
    protected JButton btnBuscar;

    /**< Botón para ver la lista de usuarios */
    protected JButton btnVerUsuarios;

    /**< Botón para volver al menú principal */
    protected JButton btnVolverVentanaPrincipal;

    /**< Botón para ver las compras de un concierto */
    protected JButton btnVerCompras;

    /**< Usuario autenticado que accede a la ventana */
    private Usuario usuario;

    /**
     * @brief Constructor de la ventana de administración.
     * 
     * Inicializa la interfaz gráfica con una tabla de conciertos, campos de entrada para detalles del concierto,
     * y botones para gestionar eventos, usuarios y compras. Verifica que el usuario sea administrador.
     * Carga los conciertos desde la API al iniciar.
     * @param usuario [in] Usuario autenticado que intenta acceder a la ventana.
     */
    public VentanaAdmin(Usuario usuario) {
        this.usuario = usuario;
        if (!usuario.getTipoUsuario().equals(TipoUsuario.ADMIN)) {
            JOptionPane.showMessageDialog(this, "Acceso denegado. Solo administradores pueden gestionar eventos.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Gestión de Eventos");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel(new String[]{
                "ID", "Nombre", "Lugar", "Fecha", "Cap. General", "Cap. VIP", "Cap. Premium", "Precio General", "Precio VIP", "Precio Premium"
        }, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEventos = new JTable(modeloTabla);
        tablaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaEventos), BorderLayout.CENTER);

        JPanel panelEntrada = new JPanel(new GridLayout(13, 2, 5, 5));
        panelEntrada.setBorder(new EmptyBorder(10, 10, 10, 10));

        panelEntrada.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelEntrada.add(txtNombre);

        panelEntrada.add(new JLabel("Lugar:"));
        txtLugar = new JTextField();
        panelEntrada.add(txtLugar);

        panelEntrada.add(new JLabel("Fecha:"));
        spinnerFecha = new JSpinner(new SpinnerDateModel());
        spinnerFecha.setEditor(new JSpinner.DateEditor(spinnerFecha, "dd/MM/yyyy HH:mm"));
        panelEntrada.add(spinnerFecha);

        panelEntrada.add(new JLabel("Capacidad General:"));
        spinnerCapacidadGeneral = new JSpinner(new SpinnerNumberModel(100, 0, 10000, 1));
        panelEntrada.add(spinnerCapacidadGeneral);

        panelEntrada.add(new JLabel("Capacidad VIP:"));
        spinnerCapacidadVIP = new JSpinner(new SpinnerNumberModel(50, 0, 10000, 1));
        panelEntrada.add(spinnerCapacidadVIP);

        panelEntrada.add(new JLabel("Capacidad Premium:"));
        spinnerCapacidadPremium = new JSpinner(new SpinnerNumberModel(20, 0, 10000, 1));
        panelEntrada.add(spinnerCapacidadPremium);

        panelEntrada.add(new JLabel("Precio General:"));
        txtPrecioGeneral = new JTextField("0.0");
        panelEntrada.add(txtPrecioGeneral);

        panelEntrada.add(new JLabel("Precio VIP:"));
        txtPrecioVIP = new JTextField("0.0");
        panelEntrada.add(txtPrecioVIP);

        panelEntrada.add(new JLabel("Precio Premium:"));
        txtPrecioPremium = new JTextField("0.0");
        panelEntrada.add(txtPrecioPremium);

        panelEntrada.add(new JLabel("Buscar por nombre:"));
        txtBuscar = new JTextField();
        panelEntrada.add(txtBuscar);

        btnBuscar = new JButton("Buscar");
        panelEntrada.add(btnBuscar);

        // Panel para botones de gestión
        JPanel botonesGestion = new JPanel(new FlowLayout());
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        botonesGestion.add(btnAgregar);
        botonesGestion.add(btnEditar);
        botonesGestion.add(btnEliminar);
        panelEntrada.add(botonesGestion);

        // Panel para botones de vista
        JPanel botonesVista = new JPanel(new FlowLayout());
        btnVerUsuarios = new JButton("Ver Usuarios");
        btnVerCompras = new JButton("Ver Compras");
        btnVolverVentanaPrincipal = new JButton("Volver");
        botonesVista.add(btnVerUsuarios);
        botonesVista.add(btnVerCompras);
        botonesVista.add(btnVolverVentanaPrincipal);
        panelEntrada.add(botonesVista);

        add(panelEntrada, BorderLayout.SOUTH);

        // Acciones de los botones
        btnAgregar.addActionListener(e -> agregarConcierto());
        btnEliminar.addActionListener(e -> eliminarConcierto());
        btnEditar.addActionListener(e -> editarConcierto());
        btnVerUsuarios.addActionListener(e -> new VentanaUsuarios());
        btnVerCompras.addActionListener(e -> verCompras());
        btnVolverVentanaPrincipal.addActionListener(e -> {
            dispose();
            new MenuPrincipal();
        });

        // Selección en la tabla de eventos
        tablaEventos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            /**
             * @brief Maneja la selección de una fila en la tabla de eventos.
             * 
             * Carga los datos del concierto seleccionado en el formulario para su edición.
             * @param e [in] Evento de selección de la tabla.
             */
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tablaEventos.getSelectedRow() != -1) {
                    int fila = tablaEventos.getSelectedRow();
                    cargarDatosEnFormulario(fila);
                }
            }
        });

        // Cargar conciertos al iniciar
        cargarConciertos();
        setVisible(true);
    }

    /**
     * @brief Muestra las compras asociadas a un concierto seleccionado.
     * 
     * Abre una ventana con las compras del concierto seleccionado en la tabla. 
     * Requiere que haya un concierto seleccionado.
     */
    private void verCompras() {
        int filaSeleccionada = tablaEventos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un concierto para ver las compras.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idConcierto = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        Concierto concierto = new Concierto();
        concierto.setId(idConcierto);
        concierto.setNombre(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
        new VentanaComprasConcierto(concierto, usuario);
    }

    /**
     * @brief Carga los datos de un concierto en el formulario.
     * 
     * Rellena los campos del formulario con los datos del concierto seleccionado en la tabla.
     * @param fila [in] Índice de la fila seleccionada en la tabla.
     */
    private void cargarDatosEnFormulario(int fila) {
        try {
            txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtLugar.setText(modeloTabla.getValueAt(fila, 2).toString());
            
            // Fecha
            if (modeloTabla.getValueAt(fila, 3) instanceof Date) {
                spinnerFecha.setValue(modeloTabla.getValueAt(fila, 3));
            } else if (modeloTabla.getValueAt(fila, 3) instanceof java.sql.Date) {
                spinnerFecha.setValue(new Date(((java.sql.Date)modeloTabla.getValueAt(fila, 3)).getTime()));
            }
            
            // Capacidades
            spinnerCapacidadGeneral.setValue(modeloTabla.getValueAt(fila, 4));
            spinnerCapacidadVIP.setValue(modeloTabla.getValueAt(fila, 5));
            spinnerCapacidadPremium.setValue(modeloTabla.getValueAt(fila, 6));
            
            // Precios
            txtPrecioGeneral.setText(modeloTabla.getValueAt(fila, 7).toString());
            txtPrecioVIP.setText(modeloTabla.getValueAt(fila, 8).toString());
            txtPrecioPremium.setText(modeloTabla.getValueAt(fila, 9).toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @brief Carga la lista de conciertos desde la API.
     * 
     * Realiza una solicitud GET a la API para obtener la lista de conciertos y actualiza la tabla.
     */
    protected void cargarConciertos() {
        try {
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/api/conciertos";
            
            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                List<Concierto> conciertos = response.readEntity(new GenericType<List<Concierto>>() {});
                modeloTabla.setRowCount(0);
                for (Concierto concierto : conciertos) {
                    modeloTabla.addRow(new Object[]{
                            concierto.getId(),
                            concierto.getNombre(),
                            concierto.getLugar(),
                            concierto.getFecha(),
                            concierto.getCapacidadGeneral(),
                            concierto.getCapacidadVIP(),
                            concierto.getCapacidadPremium(),
                            concierto.getPrecioGeneral(),
                            concierto.getPrecioVIP(),
                            concierto.getPrecioPremium()
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al cargar los conciertos. Código: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            client.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @brief Agrega un nuevo concierto a la base de datos.
     * 
     * Valida los datos del formulario, crea un objeto Concierto y envía una solicitud POST a la API.
     * Actualiza la tabla si la operación es exitosa.
     */
    protected void agregarConcierto() {
        try {
            String nombre = txtNombre.getText().trim();
            String lugar = txtLugar.getText().trim();
            Date fecha = (Date) spinnerFecha.getValue();
    
            int capGeneral = (int) spinnerCapacidadGeneral.getValue();
            int capVIP = (int) spinnerCapacidadVIP.getValue();
            int capPremium = (int) spinnerCapacidadPremium.getValue();
    
            double precioGeneral = Double.parseDouble(txtPrecioGeneral.getText());
            double precioVIP = Double.parseDouble(txtPrecioVIP.getText());
            double precioPremium = Double.parseDouble(txtPrecioPremium.getText());
    
            if (nombre.isEmpty() || lugar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y Lugar son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            Concierto concierto = new Concierto();
            concierto.setNombre(nombre);
            concierto.setLugar(lugar);
            concierto.setFecha(new java.sql.Date(fecha.getTime()));
            concierto.setCapacidadGeneral(capGeneral);
            concierto.setCapacidadVIP(capVIP);
            concierto.setCapacidadPremium(capPremium);
            concierto.setPrecioGeneral(precioGeneral);
            concierto.setPrecioVIP(precioVIP);
            concierto.setPrecioPremium(precioPremium);
    
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/api/conciertos";
    
            Response response = client
                    .target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(concierto, MediaType.APPLICATION_JSON));
    
            if (response.getStatus() == Response.Status.OK.getStatusCode() ||
                response.getStatus() == Response.Status.CREATED.getStatusCode()) {
    
                Concierto conciertoCreado = response.readEntity(Concierto.class);
                modeloTabla.addRow(new Object[]{
                    conciertoCreado.getId(), conciertoCreado.getNombre(), conciertoCreado.getLugar(),
                    conciertoCreado.getFecha(), conciertoCreado.getCapacidadGeneral(), conciertoCreado.getCapacidadVIP(),
                    conciertoCreado.getCapacidadPremium(), conciertoCreado.getPrecioGeneral(), conciertoCreado.getPrecioVIP(),
                    conciertoCreado.getPrecioPremium()
                });
    
                JOptionPane.showMessageDialog(this, "Concierto agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar el concierto. Código: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
            }
    
            response.close();
            client.close();
    
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * @brief Elimina un concierto seleccionado de la base de datos.
     * 
     * Envía una solicitud DELETE a la API para eliminar el concierto seleccionado en la tabla, 
     * tras confirmar la acción con el usuario.
     */
    protected void eliminarConcierto() {
        int filaSeleccionada = tablaEventos.getSelectedRow();
    
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un concierto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar este concierto?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
    
        try {
            int idConcierto = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
    
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/api/conciertos/" + idConcierto;
    
            Response response = client
                    .target(apiUrl)
                    .request()
                    .delete();
    
            if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
                modeloTabla.removeRow(filaSeleccionada);
                JOptionPane.showMessageDialog(this, "Concierto eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                JOptionPane.showMessageDialog(this, "Concierto no encontrado en el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar concierto. Código: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
            }
    
            response.close();
            client.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * @brief Edita un concierto seleccionado en la base de datos.
     * 
     * Valida los datos del formulario, crea un objeto Concierto actualizado y envía una solicitud PUT a la API.
     * Actualiza la tabla si la operación es exitosa.
     */
    protected void editarConcierto() {
        int filaSeleccionada = tablaEventos.getSelectedRow();
    
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un concierto para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        try {
            int idConcierto = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            String nombre = txtNombre.getText().trim();
            String lugar = txtLugar.getText().trim();
            Date fecha = (Date) spinnerFecha.getValue();
    
            int capGeneral = (int) spinnerCapacidadGeneral.getValue();
            int capVIP = (int) spinnerCapacidadVIP.getValue();
            int capPremium = (int) spinnerCapacidadPremium.getValue();
    
            double precioGeneral = Double.parseDouble(txtPrecioGeneral.getText());
            double precioVIP = Double.parseDouble(txtPrecioVIP.getText());
            double precioPremium = Double.parseDouble(txtPrecioPremium.getText());
    
            if (nombre.isEmpty() || lugar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y Lugar son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            Concierto conciertoEditado = new Concierto();
            conciertoEditado.setId(idConcierto);
            conciertoEditado.setNombre(nombre);
            conciertoEditado.setLugar(lugar);
            conciertoEditado.setFecha(new java.sql.Date(fecha.getTime()));
            conciertoEditado.setCapacidadGeneral(capGeneral);
            conciertoEditado.setCapacidadVIP(capVIP);
            conciertoEditado.setCapacidadPremium(capPremium);
            conciertoEditado.setPrecioGeneral(precioGeneral);
            conciertoEditado.setPrecioVIP(precioVIP);
            conciertoEditado.setPrecioPremium(precioPremium);
    
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/api/conciertos/" + idConcierto;
    
            Response response = client
                    .target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(conciertoEditado, MediaType.APPLICATION_JSON));
    
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                modeloTabla.setValueAt(conciertoEditado.getNombre(), filaSeleccionada, 1);
                modeloTabla.setValueAt(conciertoEditado.getLugar(), filaSeleccionada, 2);
                modeloTabla.setValueAt(conciertoEditado.getFecha(), filaSeleccionada, 3);
                modeloTabla.setValueAt(conciertoEditado.getCapacidadGeneral(), filaSeleccionada, 4);
                modeloTabla.setValueAt(conciertoEditado.getCapacidadVIP(), filaSeleccionada, 5);
                modeloTabla.setValueAt(conciertoEditado.getCapacidadPremium(), filaSeleccionada, 6);
                modeloTabla.setValueAt(conciertoEditado.getPrecioGeneral(), filaSeleccionada, 7);
                modeloTabla.setValueAt(conciertoEditado.getPrecioVIP(), filaSeleccionada, 8);
                modeloTabla.setValueAt(conciertoEditado.getPrecioPremium(), filaSeleccionada, 9);
    
                JOptionPane.showMessageDialog(this, "Concierto actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                JOptionPane.showMessageDialog(this, "Concierto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar concierto. Código: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
            }
    
            response.close();
            client.close();
    
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * @brief Limpia los campos del formulario.
     * 
     * Restablece los valores del formulario a sus valores predeterminados y deselecciona cualquier fila en la tabla.
     */
    private void limpiarFormulario() {
        txtNombre.setText("");
        txtLugar.setText("");
        spinnerFecha.setValue(new Date());
        spinnerCapacidadGeneral.setValue(100);
        spinnerCapacidadVIP.setValue(50);
        spinnerCapacidadPremium.setValue(20);
        txtPrecioGeneral.setText("0.0");
        txtPrecioVIP.setText("0.0");
        txtPrecioPremium.setText("0.0");
        tablaEventos.clearSelection();
    }
}


