package com.example.restapi.client.window;

import java.awt.*;
import javax.swing.*;


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
        // Si el botón está en la fase de "Buscar Usuario"
        if (btnModificar.getText().equals("Modificar datos")) {
            // Mostrar un cuadro de diálogo para que el usuario introduzca su email
            String email = JOptionPane.showInputDialog(VentanaRegistro.this, "Introduce el email del usuario:", "Buscar Usuario", JOptionPane.QUESTION_MESSAGE);
    
            if (email == null || email.trim().isEmpty()) {
                JOptionPane.showMessageDialog(VentanaRegistro.this, "El email no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            try {
                // Crear un cliente HTTP para buscar el usuario
                Client client = ClientBuilder.newClient();
                try {
                    String apiUrl = "http://localhost:8080/api/usuarios/" + email.trim();
                    Response response = client.target(apiUrl)
                            .request(MediaType.APPLICATION_JSON)
                            .get();
    
                    if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                        // Obtener el usuario desde la respuesta
                        Usuario usuario = response.readEntity(Usuario.class);
    
                        // Mostrar la información del usuario en los campos correspondientes
                        txtNombre.setText(usuario.getNombre());
                        txtApellido.setText(usuario.getApellidos());
                        txtEmail.setText(usuario.getEmail());
                        txtEmail.setEnabled(false); // Deshabilitar el campo de email para que no pueda ser modificado
                        txtPassword.setText(usuario.getPassword());
                        txtTelefono.setText(usuario.getTelefono());
                        txtFechaNacimiento.setText(usuario.getFechaNacimiento().toString());
                        txtDni.setText(String.valueOf(usuario.getDni()));
                        comboTipoUsuario.setSelectedItem(usuario.getTipoUsuario());
                        comboTipoPago.setSelectedItem(usuario.getTipoPago());
    
                        // Cambiar el texto del botón a "Guardar cambios"
                        btnModificar.setText("Guardar cambios");
    
                        // Guardar el email del usuario en un atributo oculto para usarlo en la actualización
                        txtEmail.setName(email.trim());
                    } else {
                        JOptionPane.showMessageDialog(VentanaRegistro.this, "No se encontró ningún usuario con el email proporcionado.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } finally {
                    client.close(); // Cerrar el cliente HTTP de búsqueda
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(VentanaRegistro.this, "Error al buscar el usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        // Si el botón está en la fase de "Guardar cambios"
        else if (btnModificar.getText().equals("Guardar cambios")) {
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String telefonoStr = txtTelefono.getText().trim();
            String fechaNacimientoStr = txtFechaNacimiento.getText().trim();
            String dniStr = txtDni.getText().trim();
            TipoUsuario tipoUsuario = (TipoUsuario) comboTipoUsuario.getSelectedItem();
            TipoPago tipoPago = (TipoPago) comboTipoPago.getSelectedItem();
    
            // Validar que los campos no estén vacíos
            if (nombre.isEmpty() || apellido.isEmpty() || password.isEmpty() || telefonoStr.isEmpty() || dniStr.isEmpty() || fechaNacimientoStr.isEmpty()) {
                JOptionPane.showMessageDialog(VentanaRegistro.this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            try {
                // Validar que el teléfono y el DNI sean números válidos
                long telefono = Long.parseLong(telefonoStr);
                long dni = Long.parseLong(dniStr);
    
                // Validar que la fecha esté en el formato correcto (yyyy-MM-dd)
                java.sql.Date fechaNacimiento = java.sql.Date.valueOf(fechaNacimientoStr);
    
                // Crear un cliente HTTP para actualizar el usuario
                Client client = ClientBuilder.newClient();
                try {
                    String apiUrl = "http://localhost:8080/api/usuarios/" + txtEmail.getName(); // Usar el email guardado
                    Usuario usuario = new Usuario();
                    usuario.setNombre(nombre);
                    usuario.setApellidos(apellido);
                    usuario.setPassword(password);
                    usuario.setTelefono(String.valueOf(telefono));
                    usuario.setDni(dni);
                    usuario.setFechaNacimiento(fechaNacimiento);
                    usuario.setTipoUsuario(tipoUsuario);
                    usuario.setTipoPago(tipoPago);
    
                    Response response = client.target(apiUrl)
                            .request(MediaType.APPLICATION_JSON)
                            .put(Entity.entity(usuario, MediaType.APPLICATION_JSON));
    
                    if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                        JOptionPane.showMessageDialog(VentanaRegistro.this, "Datos del usuario actualizados correctamente.");
                        dispose(); // Cerrar la ventana después de la actualización
                    } else {
                        JOptionPane.showMessageDialog(VentanaRegistro.this, "Error al actualizar los datos: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } finally {
                    client.close(); // Cerrar el cliente HTTP de actualización
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(VentanaRegistro.this, "El teléfono y el DNI deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(VentanaRegistro.this, "La fecha de nacimiento debe estar en el formato yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    
    


        //setVisible(true);
    }

    protected void registrar() {
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
    
        // Validación de campos obligatorios
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() || telefonoStr.isEmpty() || dniStr.isEmpty() || fechaNacimientoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Validación del código secreto para ADMIN
        if (tipoUsuario.equals(TipoUsuario.ADMIN) && !codigoSecreto.equals(CODIGO_SECRETO_ADMIN)) {
            JOptionPane.showMessageDialog(this, "Código secreto incorrecto. No puedes registrarte como ADMIN.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            long dni = Long.parseLong(dniStr);
            long telefono = Long.parseLong(telefonoStr);
    
            java.sql.Date fechaNacimiento = java.sql.Date.valueOf(fechaNacimientoStr);
    
            // Crear el objeto Usuario
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
    
            // Configurar el cliente HTTP
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/resource/register";
            
            // Realizar la solicitud POST al backend
            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(usuario, MediaType.APPLICATION_JSON));
    
            // Manejo de la respuesta
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                JOptionPane.showMessageDialog(this, "Usuario registrado con éxito.");
                dispose();
            } else if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                // Si el email ya está registrado, mostrar mensaje de conflicto
                JOptionPane.showMessageDialog(this, "El email ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // En caso de cualquier otro error
                JOptionPane.showMessageDialog(this, "Error al registrar el usuario. Código: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
            }
    
            client.close();
        } catch (NumberFormatException ex) {
            // Manejo de excepciones para el DNI y teléfono
            JOptionPane.showMessageDialog(this, "DNI y Teléfono deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            // Manejo de excepciones para la fecha de nacimiento
            JOptionPane.showMessageDialog(this, "La fecha de nacimiento debe estar en el formato yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

     // Getters para los campos privados
     public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JTextField getTxtApellido() {
        return txtApellido;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JTextField getTxtFechaNacimiento() {
        return txtFechaNacimiento;
    }

    public JTextField getTxtTelefono() {
        return txtTelefono;
    }

    public JTextField getTxtDni() {
        return txtDni;
    }

    public JTextField getTxtCodigoSecreto() {
        return txtCodigoSecreto;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public JComboBox<TipoUsuario> getComboTipoUsuario() {
        return comboTipoUsuario;
    }

    public JComboBox<TipoPago> getComboTipoPago() {
        return comboTipoPago;
    }

    public JButton getBtnRegistrar() {
        return btnRegistrar;
    }

    public JButton getBtnModificar() {
        return btnModificar;
    }
    
}