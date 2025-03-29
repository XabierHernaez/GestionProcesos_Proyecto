package com.example.restapi.client.window;

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame{
    public MenuPrincipal() {
        // Set up the frame
        setTitle("Menu Principal");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create a welcome label
        JLabel welcomeLabel = new JLabel("<html><center>BIENVENIDO A LA WEB DONDE PODRAS ENCONTRAR<br>CUALQUIER CONCIERTO DE TU ARTISTA FAVORITO</center></html>", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.BLUE);
        add(welcomeLabel, BorderLayout.NORTH);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create buttons
        JButton registerButton = new JButton("Registro");
        JButton loginButton = new JButton("Inicio de Sesión");
        JButton catalogButton = new JButton("Catálogo de Eventos");

        // Style buttons
        registerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        loginButton.setFont(new Font("Arial", Font.PLAIN, 14));
        catalogButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add buttons to the panel
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(catalogButton);

        // Add the button panel to the frame
        add(buttonPanel, BorderLayout.CENTER);

        // Add action listeners (optional)
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Registro seleccionado");
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Inicio de Sesión seleccionado");
            }
        });

        catalogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Catálogo de Eventos seleccionado");
            }
        });

        // Make the frame visible
        setVisible(true);
    }
}
