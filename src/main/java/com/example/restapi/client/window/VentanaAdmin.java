package com.example.restapi.client.window;
import java.awt.*;

import javax.swing.*;
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

public class VentanaAdmin extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTable tablaEventos;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtLugar, txtPrecioGeneral, txtPrecioVIP, txtPrecioPremium;
    private JSpinner spinnerFecha, spinnerCapacidadGeneral, spinnerCapacidadVIP, spinnerCapacidadPremium;
    private JButton btnAgregar, btnEditar, btnEliminar;
    @SuppressWarnings("unused")
    private Usuario usuario;

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

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Lugar", "Fecha", "Cap. General", "Cap. VIP", "Cap. Premium", "Precio General", "Precio VIP", "Precio Premium"}, 0) {

			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaEventos = new JTable(modeloTabla);
        
        add(new JScrollPane(tablaEventos), BorderLayout.CENTER);

        JPanel panelEntrada = new JPanel(new GridLayout(11, 2, 5, 5));
        panelEntrada.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");

        panelEntrada.add(btnAgregar);
        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnEditar);
        botones.add(btnEliminar);
        panelEntrada.add(botones);

        add(panelEntrada, BorderLayout.SOUTH);
        btnAgregar.addActionListener(e -> agregarConcierto());
        btnEliminar.addActionListener(e -> eliminarConcierto());
        btnEditar.addActionListener(e -> editarConcierto());
        cargarConciertos();
    
        setVisible(true);
    }

    private void cargarConciertos() {
        try {
            // Crear cliente REST
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/api/conciertos"; // La URL de tu endpoint GET
            
            // Realizar la solicitud GET al servidor para obtener los conciertos
            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                // Leer la lista de conciertos desde la respuesta
                List<Concierto> conciertos = response.readEntity(new GenericType<List<Concierto>>() {});
    
                // Limpiar la tabla antes de añadir los nuevos datos
                modeloTabla.setRowCount(0);
    
                // Añadir los conciertos al modelo de la tabla
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
   
    private void agregarConcierto() {
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
    
            // Crear el concierto
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
    
            // Enviar al backend
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

    private void eliminarConcierto() {
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

    private void editarConcierto() {
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
                // Actualizar datos en la tabla
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
    
    

    // Métodos existentes sin cambios
    /* 
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<Concierto> eventos = Bbdd.getAllEventos();
            for (Concierto evento : eventos) {
                modeloTabla.addRow(new Object[]{
                        evento.getId(),
                        evento.getNombre(),
                        evento.getLugar(),
                        evento.getFecha(),
                        evento.getCapacidadGeneral(),
                        evento.getCapacidadVIP(),
                        evento.getCapacidadPremium(),
                        evento.getPrecioGeneral(),
                        evento.getPrecioVIP(),
                        evento.getPrecioPremium()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar eventos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void limpiarCampos() {
        txtNombre.setText("");
        txtLugar.setText("");
        spinnerFecha.setValue(new Date());
        spinnerCapacidadGeneral.setValue(100);
        spinnerCapacidadVIP.setValue(50);
        spinnerCapacidadPremium.setValue(20);
        txtPrecioGeneral.setText("0.0");
        txtPrecioVIP.setText("0.0");
        txtPrecioPremium.setText("0.0");
    }

    private void agregarConcierto() {
        String nombre = txtNombre.getText().trim();
        String lugar = txtLugar.getText().trim();
        Date fecha = (Date) spinnerFecha.getValue();
        int capacidadGeneral = (int) spinnerCapacidadGeneral.getValue();
        int capacidadVIP = (int) spinnerCapacidadVIP.getValue();
        int capacidadPremium = (int) spinnerCapacidadPremium.getValue();
        double precioGeneral, precioVIP, precioPremium;
        try {
            precioGeneral = Double.parseDouble(txtPrecioGeneral.getText().trim());
            precioVIP = Double.parseDouble(txtPrecioVIP.getText().trim());
            precioPremium = Double.parseDouble(txtPrecioPremium.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Los precios deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nombre.isEmpty() || lugar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
        	Bbdd.insertConcierto(nombre, lugar, fecha, capacidadGeneral, capacidadVIP, capacidadPremium, precioGeneral, precioVIP, precioPremium);
            actualizarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Evento agregado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar evento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarConcierto() {
        int fila = tablaEventos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un evento para editar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = txtNombre.getText().trim();
        String lugar = txtLugar.getText().trim();
        Date fecha = (Date) spinnerFecha.getValue();
        int capacidadGeneral = (int) spinnerCapacidadGeneral.getValue();
        int capacidadVIP = (int) spinnerCapacidadVIP.getValue();
        int capacidadPremium = (int) spinnerCapacidadPremium.getValue();
        double precioGeneral, precioVIP, precioPremium;
        try {
            precioGeneral = Double.parseDouble(txtPrecioGeneral.getText().trim());
            precioVIP = Double.parseDouble(txtPrecioVIP.getText().trim());
            precioPremium = Double.parseDouble(txtPrecioPremium.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Los precios deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nombre.isEmpty() || lugar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
        	Bbdd.updateEvento(id, nombre, lugar, fecha, capacidadGeneral, capacidadVIP, capacidadPremium, precioGeneral, precioVIP, precioPremium);
            actualizarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Evento actualizado correctamente.");
            } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar evento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarConcierto() {
        int fila = tablaEventos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un evento para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar este evento?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
            	Bbdd.deleteEvento(id);
                actualizarTabla();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Evento eliminado correctamente.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar evento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    */

}

