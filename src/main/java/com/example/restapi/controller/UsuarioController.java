package com.example.restapi.controller;

import com.example.restapi.server.jpa.UsuarioJPA;
import com.example.restapi.server.repository.UsuarioRepository;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuario Controller", description = "API para gestionar usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todos los usuarios
    @GetMapping
    public List<UsuarioJPA> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por email
    @GetMapping("/{email}")
    public ResponseEntity<UsuarioJPA> getUsuarioByEmail(@PathVariable String email) {
        Optional<UsuarioJPA> usuario = usuarioRepository.findById(email);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioJPA> createUsuario(@RequestBody UsuarioJPA usuario) {
        try {
            // Imprimir el usuario recibido
            System.out.println("Usuario recibido: " + usuario);

            UsuarioJPA nuevoUsuario = usuarioRepository.save(usuario);

            // Imprimir el usuario guardado
            System.out.println("Usuario guardado: " + nuevoUsuario);

            return ResponseEntity.ok(nuevoUsuario);
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el error en consola
            return ResponseEntity.badRequest().build();
        }
    }

    // Actualizar un usuario existente
    @PutMapping("/{email}")
    public ResponseEntity<UsuarioJPA> updateUsuario(@PathVariable String email, @RequestBody UsuarioJPA usuarioDetalles) {
        Optional<UsuarioJPA> usuarioExistente = usuarioRepository.findById(email);
        if(usuarioExistente.isPresent()) {
            UsuarioJPA usuario = usuarioExistente.get();
            usuario.setNombre(usuarioDetalles.getNombre());
            usuario.setApellidos(usuarioDetalles.getApellidos());
            usuario.setPassword(usuarioDetalles.getPassword());
            usuario.setTelefono(usuarioDetalles.getTelefono());
            usuario.setFechaNacimiento(usuarioDetalles.getFechaNacimiento());
            usuario.setTipoPago(usuarioDetalles.getTipoPago());
            usuario.setTipoUsuario(usuarioDetalles.getTipoUsuario());
            UsuarioJPA usuarioActualizado = usuarioRepository.save(usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un usuario
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable String email) {
        Optional<UsuarioJPA> usuario = usuarioRepository.findById(email);
        if (usuario.isPresent()) {
            usuarioRepository.deleteById(email);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
