package com.example.restapi.client.window;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import java.awt.*;
import javax.swing.*;

import com.example.restapi.model.Usuario;
import com.example.restapi.model.Concierto;
import com.example.restapi.model.TipoEntrada;
import com.example.restapi.model.TipoPago;

public class VentanaCompra extends JFrame{
        private static final long serialVersionUID = 1L;

    private JSpinner spinnerCantidad;
    private JComboBox<TipoEntrada> comboTipoEntrada;
    private JComboBox<String> comboUbicacion;
    private JComboBox<TipoPago> comboMetodoPago;
    private JLabel lblPrecio, lblPrecioTotal, lblImagenArtista;
    private JTextField txtDni, txtTelefono, txtCodigoPromo;
    private JButton btnConfirmar, btnCancelar, btnAplicarPromo;
    private Concierto concierto;
    private Usuario usuario;
    private double descuentoAplicado = 0.0;

    public VentanaCompra(Concierto concierto, Usuario usuario) {
        this.concierto = concierto;
        this.usuario = usuario;
        setTitle("Comprar Entrada - " + concierto.getNombre());
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configurar la ventana
    }
}
