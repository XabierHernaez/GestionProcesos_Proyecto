package com.example.restapi.integration;

import com.example.restapi.server.jpa.UsuarioJPA;
import com.example.restapi.model.TipoUsuario;
import com.example.restapi.model.TipoPago;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioIntegrationTes {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testFlujoCompletoUsuario() {
        // 1. Crear usuario
        UsuarioJPA usuario = new UsuarioJPA();
        usuario.setEmail("testuser@example.com");
        usuario.setDni(12345678L);
        usuario.setTelefono("123456789");
        usuario.setFechaNacimiento(Date.valueOf("1990-01-01"));
        usuario.setPassword("pass123");
        usuario.setNombre("Test");
        usuario.setApellidos("User");
        usuario.setTipoPago(TipoPago.Visa);
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);

        ResponseEntity<UsuarioJPA> createResponse = restTemplate.postForEntity("/api/usuarios", usuario, UsuarioJPA.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        String email = createResponse.getBody().getEmail();

        // 2. Obtener usuario por email
        ResponseEntity<UsuarioJPA> getResponse = restTemplate.getForEntity("/api/usuarios/" + email, UsuarioJPA.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(email, getResponse.getBody().getEmail());

        // 3. Actualizar usuario
        UsuarioJPA usuarioActualizado = getResponse.getBody();
        usuarioActualizado.setNombre("TestMod");
        usuarioActualizado.setApellidos("UserMod");
        usuarioActualizado.setTelefono("987654321");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UsuarioJPA> updateRequest = new HttpEntity<>(usuarioActualizado, headers);

        ResponseEntity<UsuarioJPA> updateResponse = restTemplate.exchange(
                "/api/usuarios/" + email, HttpMethod.PUT, updateRequest, UsuarioJPA.class);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals("TestMod", updateResponse.getBody().getNombre());
        assertEquals("UserMod", updateResponse.getBody().getApellidos());
        assertEquals("987654321", updateResponse.getBody().getTelefono());

        // 4. Login correcto
        UsuarioJPA loginRequest = new UsuarioJPA();
        loginRequest.setEmail(email);
        loginRequest.setPassword("pass123");
        ResponseEntity<UsuarioJPA> loginResponse = restTemplate.postForEntity("/api/usuarios/login", loginRequest, UsuarioJPA.class);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertEquals(email, loginResponse.getBody().getEmail());

        // 5. Login incorrecto
        loginRequest.setPassword("wrongpass");
        ResponseEntity<UsuarioJPA> loginFailResponse = restTemplate.postForEntity("/api/usuarios/login", loginRequest, UsuarioJPA.class);
        assertEquals(HttpStatus.UNAUTHORIZED, loginFailResponse.getStatusCode());

        // 6. Eliminar usuario
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/usuarios/" + email, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // 7. Comprobar que ya no existe
        ResponseEntity<UsuarioJPA> getAfterDelete = restTemplate.getForEntity("/api/usuarios/" + email, UsuarioJPA.class);
        assertEquals(HttpStatus.NOT_FOUND, getAfterDelete.getStatusCode());
    }
}
