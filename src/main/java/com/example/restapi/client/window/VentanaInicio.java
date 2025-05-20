package com.example.restapi.client.window;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.restapi.model.TipoUsuario;
import com.example.restapi.model.Usuario;

/**
 * @class VentanaInicio
 * @brief Ventana de inicio de sesión para los usuarios del sistema.
 *
 * Permite a los usuarios ingresar su correo electrónico y contraseña
 * para autenticarse a través de una API REST. Si la autenticación es exitosa,
 * redirige a la ventana correspondiente según el tipo de usuario (ADMIN o CLIENTE).
 */
public class VentanaInicio extends JFrame {
    private static final long serialVersionUID = 1L;

    /**< Campo de texto para introducir el correo electrónico */
    private JTextField txtEmail;

    /**< Campo de texto para introducir la contraseña */
    private JPasswordField txtPassword;

    /**< Botón para iniciar sesión */
    private JButton btnLogin;

    /**< Botón para acceder al formulario de registro */
    private JButton btnRegistro;

    /**
     * @brief Constructor de la ventana de inicio de sesión.
     *
     * Configura los componentes visuales y define el comportamiento de los botones.
     */
    public VentanaInicio() {
        setTitle("Inicio de Sesión");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Campo email
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);

        txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(250, 30));
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        // Campo contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Contraseña:"), gbc);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(250, 30));
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        // Panel con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(240, 248, 255));

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setPreferredSize(new Dimension(160, 40));
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnRegistro = new JButton("Registrarse");
        btnRegistro.setPreferredSize(new Dimension(160, 40));
        btnRegistro.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistro.setBackground(new Color(70, 130, 180));
        btnRegistro.setForeground(Color.WHITE);
        btnRegistro.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        btnRegistro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panelBotones.add(btnLogin);
        panelBotones.add(btnRegistro);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);

        add(panel);

        // Asignar eventos
        btnLogin.addActionListener(e -> login());
        btnRegistro.addActionListener(e -> abrirRegistro());

        setVisible(true);
    }

    /**
     * @brief Realiza la autenticación del usuario al presionar "Iniciar Sesión".
     *
     * Envía una solicitud POST a la API REST con el email y la contraseña proporcionados.
     * Según la respuesta del servidor, notifica si la autenticación fue exitosa o si hubo errores.
     */
    private void login() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/api/usuarios/login";

            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setPassword(password);

            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(usuario, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                Usuario usuarioLogueado = response.readEntity(Usuario.class);

                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                if (usuarioLogueado.getTipoUsuario() == TipoUsuario.ADMIN) {
                    dispose();
                    new VentanaAdmin(usuarioLogueado);
                } else {
                    dispose();
                    new VentanaConciertos(usuarioLogueado);
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

    /**
     * @brief Abre la ventana de registro de usuario al hacer clic en "Registrarse".
     */
    private void abrirRegistro() {
        new VentanaRegistro(this);
    }
}