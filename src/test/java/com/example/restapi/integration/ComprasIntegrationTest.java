package com.example.restapi.integration;

import com.example.restapi.server.jpa.CompraJPA;
import com.example.restapi.server.jpa.ConciertoJPA;
import com.example.restapi.server.jpa.UsuarioJPA;
import com.example.restapi.model.TipoUsuario;
import com.example.restapi.model.TipoPago;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.sql.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ComprasIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testFlujoCompletoCompra() {
        // 1. Crear usuario
        UsuarioJPA usuario = new UsuarioJPA();
        usuario.setEmail("comprador@example.com");
        usuario.setDni(12345678L);
        usuario.setTelefono("123456789");
        usuario.setFechaNacimiento(Date.valueOf("2000-01-01"));    
        usuario.setPassword("pass");
        usuario.setNombre("Comprador");
        usuario.setApellidos("Test");
        usuario.setTipoPago(TipoPago.Visa);
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);

        ResponseEntity<UsuarioJPA> usuarioResponse = restTemplate.postForEntity("/api/usuarios", usuario, UsuarioJPA.class);
        assertEquals(HttpStatus.OK, usuarioResponse.getStatusCode());
        assertNotNull(usuarioResponse.getBody());
        String email = usuarioResponse.getBody().getEmail();

        // 2. Crear concierto
        ConciertoJPA concierto = new ConciertoJPA();
        concierto.setNombre("Concierto Test");
        concierto.setLugar("Madrid");
        concierto.setFecha(new Date(System.currentTimeMillis() + 86400000L));
        concierto.setCapacidadGeneral(100);
        concierto.setPrecioGeneral(20.0);
        concierto.setCapacidadVIP(50);
        concierto.setPrecioVIP(50.0);
        concierto.setCapacidadPremium(10);
        concierto.setPrecioPremium(100.0);

        ResponseEntity<ConciertoJPA> conciertoResponse = restTemplate.postForEntity("/api/conciertos", concierto, ConciertoJPA.class);
            assertEquals(HttpStatus.OK, conciertoResponse.getStatusCode());
            assertNotNull(conciertoResponse.getBody());
            int conciertoId = conciertoResponse.getBody().getId();

        // 3. Realizar compra
        Map<String, Object> datosCompra = new HashMap<>();
        datosCompra.put("email", email);
        datosCompra.put("conciertoId", conciertoId);
        datosCompra.put("tipoEntrada", "GENERAL");
        datosCompra.put("cantidad", 2);

        ResponseEntity<String> compraResponse = restTemplate.postForEntity("/compras", datosCompra, String.class);
        assertEquals(HttpStatus.OK, compraResponse.getStatusCode());
        assertTrue(compraResponse.getBody().contains("éxito"));

        // 4. Consultar compras por usuario
        ResponseEntity<List<Map<String, Object>>> comprasUsuarioResponse = restTemplate.exchange(
                "/compras/usuario?email=" + email,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        assertEquals(HttpStatus.OK, comprasUsuarioResponse.getStatusCode());
        List<Map<String, Object>> comprasUsuario = comprasUsuarioResponse.getBody();
        assertNotNull(comprasUsuario);
        assertFalse(comprasUsuario.isEmpty());
        assertEquals(email, comprasUsuario.get(0).get("email"));

        // 5. Consultar entradas vendidas
        ResponseEntity<Integer> entradasVendidasResponse = restTemplate.exchange(
                "/compras/concierto/" + conciertoId + "/vendidas/GENERAL",
                HttpMethod.GET,
                null,
                Integer.class
        );
        assertEquals(HttpStatus.OK, entradasVendidasResponse.getStatusCode());
        assertEquals(2, entradasVendidasResponse.getBody());

        // 6. Consultar compras por concierto
        ResponseEntity<List<Map<String, Object>>> comprasConciertoResponse = restTemplate.exchange(
                "/compras/concierto/" + conciertoId + "?adminEmail=admin@example.com",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        assertEquals(HttpStatus.OK, comprasConciertoResponse.getStatusCode());
        List<Map<String, Object>> comprasConcierto = comprasConciertoResponse.getBody();
        assertNotNull(comprasConcierto);
        assertFalse(comprasConcierto.isEmpty());
        assertEquals(email, comprasConcierto.get(0).get("email"));
    }
}
