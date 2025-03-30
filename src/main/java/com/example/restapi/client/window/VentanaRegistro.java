package com.example.restapi.client.window;

import java.awt.*;
import java.sql.SQLException;

import javax.swing.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import com.example.restapi.client.db.Bbdd;
import com.example.restapi.model.TipoPago;
import com.example.restapi.model.TipoUsuario;
import com.example.restapi.model.Usuario;

public class VentanaRegistro extends JDialog {
    private static final long serialVersionUID = 1L;
    private JTextField txtNombre, txtApellido, txtEmail, txtFechaNacimiento, txtTelefono, txtDni, txtCodigoSecreto;
    private JPasswordField txtPassword;
    private JComboBox<TipoUsuario> comboTipoUsuario;
    private JComboBox<TipoPago> comboTipoPago; // Cambiado a JComboBox
    private JButton btnRegistrar;
    private JLabel lblCodigoSecreto;
    @SuppressWarnings("unused")
    private VentanaInicio parent;
    private static final String CODIGO_SECRETO_ADMIN = "Admin123"; // Código secreto fijo (puedes cambiarlo)

    public VentanaRegistro(VentanaInicio parent) {
        super(parent, "Registro de Usuario", true);
        this.parent = parent;
        setSize(400, 400); // Ajustamos el tamaño
        setLocationRelativeTo(parent);


		// Cambiar icono de la ventana
		
        JPanel panel = new JPanel(new GridLayout(12, 2, 5, 5)); // Aumentamos a 12 filas

        // Cambiar icono de la ventana
        ImageIcon imagen1 = new ImageIcon("resources/images/bravo.png");
        setIconImage(imagen1.getImage());

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campos del formulario
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

        panel.add(new JLabel("Fecha de Nacimiento (yyyy-MM-dd):"));
        txtFechaNacimiento = new JTextField();
        panel.add(txtFechaNacimiento);

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

        // ComboBox para TipoPago
        panel.add(new JLabel("Método de Pago:"));
        comboTipoPago = new JComboBox<>(TipoPago.values()); // ComboBox para TipoPago
        panel.add(comboTipoPago);

        btnRegistrar = new JButton("Registrar");
        panel.add(new JLabel("")); // Espacio vacío
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

        // Acción del botón Registrar
        btnRegistrar.addActionListener(e -> registrar());

        setVisible(true);
    }

    private void registrar() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String telefonoStr = txtTelefono.getText().trim();
        String fechaNacimientoStr = txtFechaNacimiento.getText().trim();
        String dniStr = txtDni.getText().trim();
        String codigoSecreto = txtCodigoSecreto.getText().trim();
        TipoUsuario tipoUsuario = (TipoUsuario) comboTipoUsuario.getSelectedItem();
        TipoPago tipoPago = (TipoPago) comboTipoPago.getSelectedItem(); // Obtener el método de pago seleccionado

        // Validar campos vacíos
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() || telefonoStr.isEmpty() || dniStr.isEmpty() || fechaNacimientoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar código secreto para ADMIN
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
                Bbdd.insertUsuario(nombre, apellido, email, password, telefono, dniStr, tipoUsuario);
                JOptionPane.showMessageDialog(this, "Usuario registrado correctamente. Ahora puedes iniciar sesión.");
                dispose();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return; // Ensure the flow exits on SQLException
        }

        try {
            long dni = Long.parseLong(dniStr);
            

            // Convertir la fecha de nacimiento a java.sql.Date
            java.sql.Date fechaNacimiento = java.sql.Date.valueOf(fechaNacimientoStr);

            // Crear objeto Usuario
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellidos(apellido);
            usuario.setEmail(email);
            usuario.setPassword(password);
            usuario.setFechaNacimiento(fechaNacimiento);
            usuario.setTelefono(String.valueOf(telefono));
            usuario.setDni(dni);
            usuario.setTipoUsuario(tipoUsuario);
            usuario.setTipoPago(tipoPago); // Asignar el método de pago seleccionado

            // Enviar datos al servidor
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/resource/register";
            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(usuario, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                JOptionPane.showMessageDialog(this, "Usuario registrado con éxito.");
                dispose(); // Cerrar la ventana
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el usuario. Código: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            client.close();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "DNI y Teléfono deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "La fecha de nacimiento debe estar en el formato yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}



