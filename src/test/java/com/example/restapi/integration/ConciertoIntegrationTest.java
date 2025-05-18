package com.example.restapi.integration;

import com.example.restapi.server.jpa.ConciertoJPA;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConciertoIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testFlujoCompletoConcierto() {
        // 1. Crear concierto
        ConciertoJPA concierto = new ConciertoJPA();
        concierto.setNombre("Concierto Integration");
        concierto.setLugar("Barcelona");
        concierto.setFecha(new Date(System.currentTimeMillis() + 86400000L));
        concierto.setCapacidadGeneral(200);
        concierto.setPrecioGeneral(30.0);
        concierto.setCapacidadVIP(80);
        concierto.setPrecioVIP(70.0);
        concierto.setCapacidadPremium(20);
        concierto.setPrecioPremium(150.0);

        ResponseEntity<ConciertoJPA> createResponse = restTemplate.postForEntity("/api/conciertos", concierto, ConciertoJPA.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        int conciertoId = createResponse.getBody().getId();

        // 2. Obtener concierto por ID
        ResponseEntity<ConciertoJPA> getResponse = restTemplate.getForEntity("/api/conciertos/" + conciertoId, ConciertoJPA.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("Concierto Integration", getResponse.getBody().getNombre());

        // 3. Actualizar concierto
        ConciertoJPA actualizado = getResponse.getBody();
        actualizado.setNombre("Concierto Modificado");
        actualizado.setLugar("Valencia");
        actualizado.setPrecioGeneral(35.0);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ConciertoJPA> updateRequest = new HttpEntity<>(actualizado, headers);

        ResponseEntity<ConciertoJPA> updateResponse = restTemplate.exchange(
                "/api/conciertos/" + conciertoId, HttpMethod.PUT, updateRequest, ConciertoJPA.class);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals("Concierto Modificado", updateResponse.getBody().getNombre());
        assertEquals("Valencia", updateResponse.getBody().getLugar());
        assertEquals(35.0, updateResponse.getBody().getPrecioGeneral());

        // 4. Eliminar concierto
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/conciertos/" + conciertoId, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // 5. Comprobar que ya no existe
        ResponseEntity<ConciertoJPA> getAfterDelete = restTemplate.getForEntity("/api/conciertos/" + conciertoId, ConciertoJPA.class);
        assertEquals(HttpStatus.NOT_FOUND, getAfterDelete.getStatusCode());
    }
}
