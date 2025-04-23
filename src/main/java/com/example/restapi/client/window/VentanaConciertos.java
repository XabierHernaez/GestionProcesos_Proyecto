package com.example.restapi.client.window;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import com.example.restapi.model.Concierto;
import com.example.restapi.model.Usuario;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class VentanaConciertos extends JFrame {
    private static final long serialVersionUID = 1L;
    private JList<Concierto> listaConciertos;
    private DefaultListModel<Concierto> modeloLista;
    private JButton btnComprar;
    @SuppressWarnings("unused")
	private Usuario usuario;

    public VentanaConciertos(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Eventos Disponibles");
        setSize(700, 500); // Aumentamos el tamaño para mostrar más detalles
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        

        setLayout(new BorderLayout());

        modeloLista = new DefaultListModel<>();
        listaConciertos = new JList<>(modeloLista);
        listaConciertos.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Concierto) {
                    setText(((Concierto) value).getNombre()); // Assuming Concierto has a getNombre() method
                }
                return renderer;
            }
        }); // Usar el renderer personalizado
        actualizarLista();
        add(new JScrollPane(listaConciertos), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        btnComprar = new JButton("Comprar Entrada");
        panelBotones.add(btnComprar);
        add(panelBotones, BorderLayout.SOUTH);

        btnComprar.addActionListener(e -> {
            Concierto conciertoSeleccionado = listaConciertos.getSelectedValue();
            if (conciertoSeleccionado != null) {
                new VentanaCompra(conciertoSeleccionado, usuario);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un evento.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private void actualizarLista() {
        modeloLista.clear();
        try {
            // Crear cliente REST
            Client client = ClientBuilder.newClient();
            String apiUrl = "http://localhost:8080/api/conciertos";
            
            // Realizar la solicitud GET al servidor
            Response response = client.target(apiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            // Manejar la respuesta del servidor
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                // Leer la lista de conciertos desde la respuesta
                List<Concierto> conciertos = response.readEntity(
                        new GenericType<List<Concierto>>() {}
                );
                
                for (Concierto concierto : conciertos) {
                    modeloLista.addElement(concierto);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Error al cargar conciertos. Código: " + response.getStatus(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            client.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error al conectar con el servidor: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}