package com.example.restapi.client.window;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.example.restapi.model.Usuario;
import com.example.restapi.model.TipoUsuario;

public class VentanaInicioSesion extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegistro;

    public VentanaInicioSesion() {
        setTitle("Inicio de Sesión");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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

        // Acción de Iniciar Sesión
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText();
                String password = new String(txtPassword.getPassword());

                // Simulación de validación de usuario (puedes conectarlo a la base de datos)
                Usuario usuario = validarUsuario(email, password);
                if (usuario != null) {
                    JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso.");
                    dispose(); // Cerrar la ventana actual

                    // Abrir la ventana de administración si el usuario es ADMIN
                    if (usuario.getTipoUsuario().equals(TipoUsuario.ADMIN)) {
                        new VentanaAdmin(usuario);
                    } else {
                        JOptionPane.showMessageDialog(null, "El usuario no tiene permisos de administrador.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnRegistro.addActionListener(e -> abrirRegistro());
        setVisible(true);
    }

    private void abrirRegistro() {
        new VentanaRegistro(this);
    }

    // Método simulado para validar un usuario (en una aplicación real, usarías una base de datos)
    private Usuario validarUsuario(String email, String password) {
        // Aquí podrías agregar una validación real usando base de datos
        if (email.equals("admin@example.com") && password.equals("admin123")) {
            return new Usuario("admin@example.com", TipoUsuario.ADMIN);
        }
        return null; // Usuario no válido
    }
}

