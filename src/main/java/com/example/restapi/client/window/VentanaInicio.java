package com.example.restapi.client.window;

import java.awt.*;
import javax.swing.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.restapi.model.TipoUsuario;
import com.example.restapi.model.Usuario;

public class VentanaInicio extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegistro;

    public VentanaInicio() {
        setTitle("Inicio de Sesión");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Cambiar icono de la ventana
        ImageIcon imagen = new ImageIcon("resources/images/bravo.png");
        setIconImage(imagen.getImage());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Campo Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);

        txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        // Campo Contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Contraseña:"), gbc);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        // Panel para los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setPreferredSize(new Dimension(140, 40));

        btnRegistro = new JButton("Registrarse");
        btnRegistro.setPreferredSize(new Dimension(140, 40));

        panelBotones.add(btnLogin);
        panelBotones.add(btnRegistro);

        // Agregar el panel de botones
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);

        add(panel);

        btnLogin.addActionListener(e -> login());
        btnRegistro.addActionListener(e -> abrirRegistro());

        setVisible(true);
    }
    
    private void login() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
    
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            // Crear cliente REST
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/api/usuarios/login"; // Asegúrate de que esta URL sea correcta
    
            // Crear objeto Usuario para enviar al servidor
            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setPassword(password);
    
            // Realizar la solicitud POST al servidor
            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(usuario, MediaType.APPLICATION_JSON));
    
            // Manejar la respuesta del servidor
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                // Leer el usuario completo desde la respuesta
                Usuario usuarioLogueado = response.readEntity(Usuario.class);
    
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    
                // Redirigir según el tipo de usuario
                if (usuarioLogueado.getTipoUsuario() == TipoUsuario.ADMIN) {
                    dispose();
                    new VentanaAdmin(usuarioLogueado);
                } else {
                    dispose();
                    /*new VentanaUsuario(usuarioLogueado);*/
                }
            } else if (response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al iniciar sesión. Código: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
            }
    
            client.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   
    /* 
    private void login() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Crear cliente REST
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/usuarios/login";

            // Crear objeto Usuario para enviar al servidor
            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setPassword(password);

            // Realizar la solicitud POST al servidor
            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(usuario, MediaType.APPLICATION_JSON));

            // Manejar la respuesta del servidor
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String mensaje = response.readEntity(String.class);
                JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Redirigir según el tipo de usuario
                if (usuario.getTipoUsuario() == TipoUsuario.ADMIN) {
                    dispose();
                    new VentanaAdmin(usuario);
                } else {
                    dispose();
                   // new VentanaUsuario(usuario);
                }
            } else if (response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al iniciar sesión. Código: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            client.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    */
    private void abrirRegistro() {
        new VentanaRegistro(this);
    }
    
}

