package com.example.restapi.client.window;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import javax.swing.*;
import com.example.restapi.model.Usuario;
import com.example.restapi.model.TipoPago;

public class VentanaRegistro extends JDialog {
    private static final long serialVersionUID = 1L;

    // Campos del formulario
    private JTextField txtNombre, txtApellidos, txtEmail, txtTelefono, txtDni;
    private JPasswordField txtPassword;
    private JComboBox<TipoPago> comboTipoPago;
    private JSpinner spinnerFechaNacimiento;
    private JButton btnRegistrar, btnCancelar;

    public VentanaRegistro(Frame parent) {
        super(parent, "Registro de Usuario", true);
        setSize(400, 450);  // Tamaño de la ventana
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Crear panel principal con GridLayout para el formulario
        JPanel panelFormulario = new JPanel(new GridLayout(8, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Añadir campos del formulario
        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Apellidos:"));
        txtApellidos = new JTextField();
        panelFormulario.add(txtApellidos);

        panelFormulario.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelFormulario.add(txtEmail);

        panelFormulario.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panelFormulario.add(txtPassword);

        panelFormulario.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelFormulario.add(txtTelefono);

        panelFormulario.add(new JLabel("DNI:"));
        txtDni = new JTextField();
        panelFormulario.add(txtDni);

        panelFormulario.add(new JLabel("Fecha de Nacimiento:"));
        spinnerFechaNacimiento = new JSpinner(new SpinnerDateModel());
        spinnerFechaNacimiento.setEditor(new JSpinner.DateEditor(spinnerFechaNacimiento, "dd/MM/yyyy"));
        panelFormulario.add(spinnerFechaNacimiento);

        panelFormulario.add(new JLabel("Tipo de Pago:"));
        comboTipoPago = new JComboBox<>(TipoPago.values()); 
        panelFormulario.add(comboTipoPago);

        add(panelFormulario, BorderLayout.CENTER);

        // Botones de registrar y cancelar
        JPanel panelBotones = new JPanel();
        btnRegistrar = new JButton("Registrar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        // Listener del botón Registrar
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });

        // Listener del botón Cancelar para cerrar la ventana
        btnCancelar.addActionListener(e -> dispose());

        // Mostrar la ventana
        setVisible(true);
    }

    private void registrarUsuario() {
        // Obtener valores de los campos
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String telefono = txtTelefono.getText().trim();
        String dniStr = txtDni.getText().trim();
        Date fechaNacimiento = new Date(((java.util.Date) spinnerFechaNacimiento.getValue()).getTime());
        TipoPago tipoPago = (TipoPago) comboTipoPago.getSelectedItem();

        // Validación básica
        if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || password.isEmpty()
                || telefono.isEmpty() || dniStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Introduce un email válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Long dni = Long.parseLong(dniStr);  // Validación de DNI como número
            Usuario nuevoUsuario = new Usuario(nombre, apellidos, email, password, telefono, dni, fechaNacimiento, tipoPago);

            // Aquí se llamaría a una base de datos o lógica para guardar el usuario
            JOptionPane.showMessageDialog(this, "Usuario registrado con éxito:\n" + nuevoUsuario.toString());
            dispose();  // Cerrar la ventana tras registro exitoso
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El DNI debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

