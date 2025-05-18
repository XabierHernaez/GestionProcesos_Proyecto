package com.example.restapi.client.window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @class MenuPrincipal
 * @brief Ventana principal de la aplicación de gestión de conciertos.
 * 
 * Esta clase representa la interfaz gráfica del menú principal, que permite al usuario
 * acceder a las funcionalidades de registro, inicio de sesión y visualización del catálogo
 * de eventos. Utiliza componentes de Swing para crear una interfaz amigable con botones
 * estilizados y un diseño centrado.
 */
public class MenuPrincipal extends JFrame {
    /**< Identificador de serialización para la clase */
    private static final long serialVersionUID = 1L;

    /**
     * @brief Constructor de la ventana principal.
     * 
     * Configura la ventana con un título, tamaño, diseño y componentes gráficos como una
     * etiqueta de bienvenida y botones para registro, inicio de sesión y catálogo de eventos.
     * Los botones tienen acciones asociadas que abren otras ventanas o muestran mensajes.
     */
    public MenuPrincipal() {
        // Configurar la ventana
        setTitle("Menú Principal");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(240, 248, 255)); // Color de fondo suave

        // Crear etiqueta de bienvenida
        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>"
                + "🎵 Bienvenido a la plataforma de conciertos <br>"
                + "Encuentra eventos de tus artistas favoritos"
                + "</div></html>", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        welcomeLabel.setForeground(new Color(60, 90, 153));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));
        add(welcomeLabel, BorderLayout.NORTH);

        // Crear panel para los botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 20, 20));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Crear botones
        JButton registerButton = new JButton("Registro");
        JButton loginButton = new JButton("Inicio de Sesión");
        JButton catalogButton = new JButton("Catálogo de Eventos");

        // Estilizar botones
        JButton[] buttons = {registerButton, loginButton, catalogButton};
        for (JButton button : buttons) {
            button.setFont(new Font("SansSerif", Font.BOLD, 16));
            button.setFocusPainted(false);
            button.setBackground(new Color(70, 130, 180));
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        // Añadir botones al panel
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(catalogButton);

        // Añadir el panel de botones a la ventana
        add(buttonPanel, BorderLayout.CENTER);

        // Acciones de los botones
        registerButton.addActionListener(new ActionListener() {
            /**
             * @brief Maneja el evento de clic en el botón de registro.
             * 
             * Muestra un mensaje y abre la ventana de registro.
             * @param e [in] Evento de acción generado por el botón.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Registro seleccionado");
                VentanaRegistro ventanaRegistro = new VentanaRegistro(null);
                ventanaRegistro.setVisible(true);
            }
        });

        loginButton.addActionListener(new ActionListener() {
            /**
             * @brief Maneja el evento de clic en el botón de inicio de sesión.
             * 
             * Muestra un mensaje y abre la ventana de inicio de sesión.
             * @param e [in] Evento de acción generado por el botón.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Inicio de Sesión seleccionado");
                new VentanaInicio();
            }
        });

        catalogButton.addActionListener(new ActionListener() {
            /**
             * @brief Maneja el evento de clic en el botón de catálogo de eventos.
             * 
             * Abre la ventana de catálogo de eventos y muestra un mensaje.
             * @param e [in] Evento de acción generado por el botón.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaConciertos(null);
                JOptionPane.showMessageDialog(null, "Catálogo de Eventos seleccionado");
            }
        });

        // Mostrar la ventana
        setVisible(true);
    }
}
