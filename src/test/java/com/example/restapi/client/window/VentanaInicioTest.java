package com.example.restapi.client.window;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VentanaInicioTest {

    private VentanaInicio ventana;

    @BeforeEach
    public void setUp() {
        // Importante: en tests GUI, mejor lanzar en EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> ventana = new VentanaInicio());
    }

    @Test
    public void testComponentesNoNulos() {
        assertNotNull(ventana);
        assertNotNull(getTextFieldEmail());
        assertNotNull(getPasswordField());
        assertNotNull(getBtnLogin());
        assertNotNull(getBtnRegistro());
    }

    @Test
    public void testTituloVentana() {
        assertEquals("Inicio de Sesión", ventana.getTitle());
    }

    @Test
    public void testTamanioVentana() {
        assertEquals(500, ventana.getWidth());
        assertEquals(300, ventana.getHeight());
    }

    @Test
    public void testAccionLoginCamposVacios() {
        SwingUtilities.invokeLater(() -> {
            getTextFieldEmail().setText("");
            getPasswordField().setText("");

            getBtnLogin().doClick();

            // No podemos capturar JOptionPane directamente, pero podemos verificar que no se cierra
            assertTrue(ventana.isDisplayable());
        });
    }

    @Test
    public void testAccionRegistro() {
        SwingUtilities.invokeLater(() -> {
            getBtnRegistro().doClick();
            assertTrue(ventana.isDisplayable()); // Verifica que no se cierre la ventana actual
        });
    }

    // Métodos privados auxiliares para acceder a los componentes
    private JTextField getTextFieldEmail() {
        try {
            var field = VentanaInicio.class.getDeclaredField("txtEmail");
            field.setAccessible(true);
            return (JTextField) field.get(ventana);
        } catch (Exception e) {
            fail("No se pudo acceder al campo txtEmail: " + e.getMessage());
            return null;
        }
    }

    private JPasswordField getPasswordField() {
        try {
            var field = VentanaInicio.class.getDeclaredField("txtPassword");
            field.setAccessible(true);
            return (JPasswordField) field.get(ventana);
        } catch (Exception e) {
            fail("No se pudo acceder al campo txtPassword: " + e.getMessage());
            return null;
        }
    }

    private JButton getBtnLogin() {
        try {
            var field = VentanaInicio.class.getDeclaredField("btnLogin");
            field.setAccessible(true);
            return (JButton) field.get(ventana);
        } catch (Exception e) {
            fail("No se pudo acceder al botón btnLogin: " + e.getMessage());
            return null;
        }
    }

    private JButton getBtnRegistro() {
        try {
            var field = VentanaInicio.class.getDeclaredField("btnRegistro");
            field.setAccessible(true);
            return (JButton) field.get(ventana);
        } catch (Exception e) {
            fail("No se pudo acceder al botón btnRegistro: " + e.getMessage());
            return null;
        }
    }
}