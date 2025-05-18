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
 * Clase que representa la ventana de inicio de sesión para los usuarios.
 * Permite ingresar correo electrónico y contraseña, y autentica al usuario
 * contra el servidor REST.
 */
public class VentanaInicio extends JFrame {
    private static final long serialVersionUID = 1L;

    // Campos de entrada de texto
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    // Botones de acciones
    private JButton btnLogin, btnRegistro;

    /**
     * Constructor que inicializa y muestra la ventana de inicio de sesión.
     */
    public VentanaInicio() {
        // Configurar la ventana principal
        setTitle("Inicio de Sesión");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el panel principal con layout y bordes
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Etiqueta y campo de texto para el email
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);

        txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(250, 30));
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        // Etiqueta y campo de texto para la contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Contraseña:"), gbc);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(250, 30));
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        // Panel para contener los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(240, 248, 255));

        // Botón para iniciar sesión
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setPreferredSize(new Dimension(160, 40));
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Botón para registrarse
        btnRegistro = new JButton("Registrarse");
        btnRegistro.setPreferredSize(new Dimension(160, 40));
        btnRegistro.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistro.setBackground(new Color(70, 130, 180));
        btnRegistro.setForeground(Color.WHITE);
        btnRegistro.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        btnRegistro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Añadir botones al panel
        panelBotones.add(btnLogin);
        panelBotones.add(btnRegistro);

        // Añadir panel de botones al panel principal
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);

        // Añadir el panel principal a la ventana
        add(panel);

        // Asignar acciones a los botones
        btnLogin.addActionListener(e -> login());
        btnRegistro.addActionListener(e -> abrirRegistro());

        // Hacer visible la ventana
        setVisible(true);
    }

    /**
     * Método que se ejecuta al hacer clic en "Iniciar Sesión".
     * Realiza una petición al servidor REST para autenticar al usuario.
     */
    private void login() {
        // Obtener los datos introducidos por el usuario
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        // Validar que los campos no estén vacíos
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Crear cliente REST para enviar la solicitud
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/api/usuarios/login"; // URL del endpoint de login

            // Crear objeto Usuario con los datos introducidos
            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setPassword(password);

            // Enviar solicitud POST con el usuario como JSON
            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(usuario, MediaType.APPLICATION_JSON));

            // Evaluar la respuesta del servidor
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                // Autenticación exitosa: leer el objeto Usuario de la respuesta
                Usuario usuarioLogueado = response.readEntity(Usuario.class);

                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Redirigir al usuario según su tipo
                if (usuarioLogueado.getTipoUsuario() == TipoUsuario.ADMIN) {
                    dispose(); // Cerrar ventana actual
                    new VentanaAdmin(usuarioLogueado); // Abrir ventana de admin
                } else {
                    dispose(); // Cerrar ventana actual
                    new VentanaConciertos(usuarioLogueado); // Abrir ventana de conciertos
                }
            } else if (response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
                // Contraseña incorrecta
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                // Usuario no encontrado
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Otro error
                JOptionPane.showMessageDialog(this, "Error al iniciar sesión. Código: " + response.getStatus(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            client.close(); // Cerrar cliente REST
        } catch (Exception ex) {
            // Error de conexión con el servidor
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método que se ejecuta al hacer clic en "Registrarse".
     * Abre la ventana de registro de usuario.
     */
    private void abrirRegistro() {
        new VentanaRegistro(this);
    }
}