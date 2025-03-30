package com.example.restapi.client.window;

import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;

import com.example.restapi.client.db.Bbdd;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.restapi.model.TipoPago;
import com.example.restapi.model.TipoUsuario;
import com.example.restapi.model.Usuario;

public class VentanaRegistro extends JDialog {
    private static final long serialVersionUID = 1L;
    private JTextField txtNombre, txtApellido, txtEmail, txtFechaNacimiento, txtTelefono, txtDni, txtCodigoSecreto;
    private JPasswordField txtPassword;
    private JComboBox<TipoUsuario> comboTipoUsuario;
    private JComboBox<TipoPago> comboTipoPago;
    private JButton btnRegistrar, btnModificar;
    private JLabel lblCodigoSecreto;
    @SuppressWarnings("unused")
    private VentanaInicio parent;
    private static final String CODIGO_SECRETO_ADMIN = "Admin123";

    public VentanaRegistro(VentanaInicio parent) {
        super(parent, "Registro de Usuario", true);
        this.parent = parent;
        setSize(400, 400);
        setLocationRelativeTo(parent);

        // Cambiar icono de la ventana
        ImageIcon imagen1 = new ImageIcon("resources/images/bravo.png");
        setIconImage(imagen1.getImage());

        // Crear el panel del formulario
        JPanel panel = new JPanel(new GridLayout(12, 2, 5, 5));
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
        txtCodigoSecreto.setEnabled(false);
        panel.add(lblCodigoSecreto);
        panel.add(txtCodigoSecreto);

        panel.add(new JLabel("Método de Pago:"));
        comboTipoPago = new JComboBox<>(TipoPago.values());
        panel.add(comboTipoPago);

        // Botones
        btnRegistrar = new JButton("Registrar");
        btnModificar = new JButton("Modificar datos");
        panel.add(btnRegistrar);
        panel.add(btnModificar);

        add(panel);

        // Lógica para activar/desactivar campo código secreto
        comboTipoUsuario.addActionListener(e -> {
            if (comboTipoUsuario.getSelectedItem().equals(TipoUsuario.ADMIN)) {
                txtCodigoSecreto.setEnabled(true);
            } else {
                txtCodigoSecreto.setEnabled(false);
                txtCodigoSecreto.setText("");
            }
        });

        // Acción del botón Registrar
        btnRegistrar.addActionListener(e -> registrar());

        // Acción del botón Modificar
        btnModificar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String telefonoStr = txtTelefono.getText().trim();
            String fechaNacimientoStr = txtFechaNacimiento.getText().trim();
            String dniStr = txtDni.getText().trim();
            String tipoUsuario = ((TipoUsuario) comboTipoUsuario.getSelectedItem()).name(); // Obtener el tipo de usuario como string
        
            // Validar que los campos no estén vacíos
            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() ||
                telefonoStr.isEmpty() || dniStr.isEmpty() || fechaNacimientoStr.isEmpty()) {
                JOptionPane.showMessageDialog(VentanaRegistro.this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
            try {
                // Validar que el teléfono y el DNI sean números válidos
                int telefono = Integer.parseInt(telefonoStr);
        
                // Validar que la fecha esté en el formato correcto (yyyy-MM-dd)
                java.sql.Date fechaNacimiento = null;
                try {
                    fechaNacimiento = java.sql.Date.valueOf(fechaNacimientoStr);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(VentanaRegistro.this, "La fecha de nacimiento debe estar en el formato yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                // Si la contraseña está vacía, no la actualizamos (puedes agregar una lógica de hashing si es necesario)
                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaRegistro.this, "La contraseña no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                // Llamar al método para actualizar los datos en la base de datos
                Bbdd.actualizarUsuarioDatos(dniStr, nombre, apellido, email, password, telefono, fechaNacimientoStr, tipoUsuario);
        
                // Mostrar un mensaje de éxito
                JOptionPane.showMessageDialog(VentanaRegistro.this, "Datos del usuario actualizados correctamente.");
        
                // Cerrar la ventana después de la actualización
                dispose();  // Cerrar la ventana de modificación
            } catch (NumberFormatException ex) {
                // Si el teléfono o el DNI no son números válidos
                JOptionPane.showMessageDialog(VentanaRegistro.this, "El teléfono y el DNI deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                // Si ocurre un error con la base de datos
                JOptionPane.showMessageDialog(VentanaRegistro.this, "Error al modificar los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        

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
        TipoPago tipoPago = (TipoPago) comboTipoPago.getSelectedItem();

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() || telefonoStr.isEmpty() || dniStr.isEmpty() || fechaNacimientoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tipoUsuario.equals(TipoUsuario.ADMIN) && !codigoSecreto.equals(CODIGO_SECRETO_ADMIN)) {
            JOptionPane.showMessageDialog(this, "Código secreto incorrecto. No puedes registrarte como ADMIN.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (Bbdd.emailExists(email)) {
                JOptionPane.showMessageDialog(this, "El email ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Bbdd.insertUsuario(nombre, apellido, email, password, Integer.parseInt(telefonoStr), dniStr, tipoUsuario);
                JOptionPane.showMessageDialog(this, "Usuario registrado correctamente. Ahora puedes iniciar sesión.");
                dispose();
            }

            long dni = Long.parseLong(dniStr);
            long telefono = Long.parseLong(telefonoStr);
            java.sql.Date fechaNacimiento = java.sql.Date.valueOf(fechaNacimientoStr);

            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellidos(apellido);
            usuario.setEmail(email);
            usuario.setPassword(password);
            usuario.setFechaNacimiento(fechaNacimiento);
            usuario.setTelefono(String.valueOf(telefono));
            usuario.setDni(dni);
            usuario.setTipoUsuario(tipoUsuario);
            usuario.setTipoPago(tipoPago);

            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/resource/register";
            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(usuario, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                JOptionPane.showMessageDialog(this, "Usuario registrado con éxito.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el usuario. Código: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            client.close();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "DNI y Teléfono deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "La fecha de nacimiento debe estar en el formato yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}