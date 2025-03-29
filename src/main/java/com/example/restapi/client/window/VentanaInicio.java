package com.example.restapi.client.window;

import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;

import com.example.restapi.client.db.Bbdd;
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
        txtEmail.setPreferredSize(new Dimension(200, 25)); // Reducir altura
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        // Campo Contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Contraseña:"), gbc);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(200, 25)); // Reducir altura
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        // Panel para los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setPreferredSize(new Dimension(140, 40)); // Botón más grande

        btnRegistro = new JButton("Registrarse");
        btnRegistro.setPreferredSize(new Dimension(140, 40)); // Botón más grande

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
        try {
            Usuario usuario = Bbdd.login(email, password);
            if (usuario != null) {
                dispose();
                if (usuario.getTipoUsuario().equals(TipoUsuario.ADMIN)) {
                    new VentanaAdmin(usuario);
                } else {
                    
                }
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al iniciar sesión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void abrirRegistro() {
        new VentanaRegistro(this);
    }
}

