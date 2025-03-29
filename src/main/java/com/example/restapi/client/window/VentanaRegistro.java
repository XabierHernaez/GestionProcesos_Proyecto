package com.example.restapi.client.window;

import java.awt.*;
import java.sql.SQLException;

import javax.swing.*;

import com.example.restapi.client.db.Bbdd;
import com.example.restapi.model.TipoUsuario;
import com.example.restapi.model.Usuario;

public class VentanaRegistro extends JDialog {
    private static final long serialVersionUID = 1L;
    private JTextField txtNombre, txtApellido, txtEmail, txtTelefono, txtDni, txtCodigoSecreto;
    private JPasswordField txtPassword;
    private JComboBox<TipoUsuario> comboTipoUsuario;
    private JButton btnRegistrar;
    private JLabel lblCodigoSecreto;
    @SuppressWarnings("unused")
    private VentanaInicioSesion parent;
    private static final String CODIGO_SECRETO_ADMIN = "Admin123"; // Código secreto fijo (puedes cambiarlo)

    public VentanaRegistro(VentanaInicioSesion parent) {
        super(parent, "Registro de Usuario", true);
        this.parent = parent;
        setSize(300, 350); // Aumentamos tamaño para el nuevo campo
        setLocationRelativeTo(parent);

		// Cambiar icono de la ventana
		
        JPanel panel = new JPanel(new GridLayout(9, 2, 5, 5)); // Aumentamos a 9 filas
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);

        panel.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        panel.add(txtApellido);

        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);

        panel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        panel.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panel.add(txtTelefono);

        panel.add(new JLabel("DNI:"));
        txtDni = new JTextField();
        panel.add(txtDni);

        panel.add(new JLabel("Tipo de usuario:"));
        comboTipoUsuario = new JComboBox<>(TipoUsuario.values());
        comboTipoUsuario.setSelectedItem(TipoUsuario.CLIENTE);
        panel.add(comboTipoUsuario);

        lblCodigoSecreto = new JLabel("Código Secreto (solo ADMIN):");
        txtCodigoSecreto = new JTextField();
        txtCodigoSecreto.setEnabled(false); // Deshabilitado por defecto
        panel.add(lblCodigoSecreto);
        panel.add(txtCodigoSecreto);

        btnRegistrar = new JButton("Registrar");
        panel.add(new JLabel(""));
        panel.add(btnRegistrar);

        add(panel);

        // Habilitar/deshabilitar el campo de código secreto según el tipo seleccionado
        comboTipoUsuario.addActionListener(e -> {
            if (comboTipoUsuario.getSelectedItem().equals(TipoUsuario.ADMIN)) {
                txtCodigoSecreto.setEnabled(true);
            } else {
                txtCodigoSecreto.setEnabled(false);
                txtCodigoSecreto.setText(""); // Limpiar el campo si no es ADMIN
            }
        });

        btnRegistrar.addActionListener(e -> registrar());

        setVisible(true);
    }

    private void registrar() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String telefonoStr = txtTelefono.getText().trim();
        String dni = txtDni.getText().trim();
        String codigoSecreto = txtCodigoSecreto.getText().trim();
        TipoUsuario tipoUsuario = (TipoUsuario) comboTipoUsuario.getSelectedItem();

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() || telefonoStr.isEmpty() || dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tipoUsuario.equals(TipoUsuario.ADMIN) && !codigoSecreto.equals(CODIGO_SECRETO_ADMIN)) {
            JOptionPane.showMessageDialog(this, "Código secreto incorrecto. No puedes registrarte como ADMIN.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int telefono;
        try {
            telefono = Integer.parseInt(telefonoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El teléfono debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (Bbdd.emailExists(email)) {
                JOptionPane.showMessageDialog(this, "El email ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Bbdd.insertUsuario(nombre, apellido, email, password, telefono, dni, tipoUsuario);
                JOptionPane.showMessageDialog(this, "Usuario registrado correctamente. Ahora puedes iniciar sesión.");
                dispose();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

