package com.example.restapi.client.window;
import java.awt.*;
import java.sql.SQLException;

import com.example.restapi.client.db.Bbdd;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.example.restapi.model.Usuario;
import com.example.restapi.client.db.Bbdd;
import java.util.Date;
import com.example.restapi.model.TipoUsuario;

public class VentanaAdmin extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTable tablaEventos;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtLugar, txtPrecioGeneral, txtPrecioVIP, txtPrecioPremium;
    private JSpinner spinnerFecha, spinnerCapacidadGeneral, spinnerCapacidadVIP, spinnerCapacidadPremium;
    private JButton btnAgregar, btnEditar, btnEliminar, btnValidarEntradas, btnVerEntradas; // Nuevo botón
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
        btnValidarEntradas = new JButton("Validar Entradas");
        btnVerEntradas = new JButton("Ver Entradas Compradas");

        panelEntrada.add(btnAgregar);
        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnValidarEntradas);
        botones.add(btnVerEntradas);
        panelEntrada.add(botones);

        add(panelEntrada, BorderLayout.SOUTH);


        setVisible(true);
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

    public class Main {
        public static void main(String[] args) {
            // Crear un usuario de tipo ADMIN
            Usuario usuarioAdmin = new Usuario();
    
            // Crear y mostrar la ventana de administración con el usuario ADMIN
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new VentanaAdmin(usuarioAdmin);
                }
            });
        }
    }

}

