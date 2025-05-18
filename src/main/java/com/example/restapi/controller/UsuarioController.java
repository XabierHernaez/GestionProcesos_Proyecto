package com.example.restapi.controller;

import com.example.restapi.server.jpa.UsuarioJPA;
import com.example.restapi.server.repository.UsuarioRepository;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @class UsuarioController
 * @brief Controlador REST para la gestión de usuarios.
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuario Controller", description = "API para gestionar usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * @brief Obtiene todos los usuarios.
     * @return Lista de usuarios en la base de datos.
     */
    @GetMapping
    public List<UsuarioJPA> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * @brief Obtiene un usuario por su email.
     * @param email Email del usuario.
     * @return Usuario encontrado o 404 si no existe.
     */
    @GetMapping("/{email}")
    public ResponseEntity<UsuarioJPA> getUsuarioByEmail(@PathVariable String email) {
        Optional<UsuarioJPA> usuario = usuarioRepository.findById(email);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * @brief Crea un nuevo usuario.
     * @param usuario Objeto UsuarioJPA con los datos del usuario.
     * @return Usuario creado o 400 en caso de error.
     */
    @PostMapping
    public ResponseEntity<UsuarioJPA> createUsuario(@RequestBody UsuarioJPA usuario) {
        try {
            System.out.println("Usuario recibido: " + usuario);

            UsuarioJPA nuevoUsuario = usuarioRepository.save(usuario);

            System.out.println("Usuario guardado: " + nuevoUsuario);

            return ResponseEntity.ok(nuevoUsuario);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * @brief Actualiza un usuario existente por su email.
     * @param email Email del usuario a actualizar.
     * @param usuarioDetalles Datos nuevos del usuario.
     * @return Usuario actualizado o 404 si no se encuentra.
     */
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

    /**
     * @brief Elimina un usuario por su email.
     * @param email Email del usuario a eliminar.
     * @return 204 si se elimina, 404 si no se encuentra.
     */
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

    /**
     * @brief Inicia sesión de usuario.
     * @param credenciales Objeto UsuarioJPA con email y contraseña.
     * @return Usuario si la autenticación es correcta, 401 si la contraseña es incorrecta, 404 si el usuario no existe.
     */
    @PostMapping("/login")
    public ResponseEntity<UsuarioJPA> loginUsuario(@RequestBody UsuarioJPA credenciales) {
        Optional<UsuarioJPA> usuarioExistente = usuarioRepository.findById(credenciales.getEmail());
        if (usuarioExistente.isPresent()) {
            UsuarioJPA usuario = usuarioExistente.get();
            if (usuario.getPassword().equals(credenciales.getPassword())) {
                return ResponseEntity.ok(usuario);
            } else {
                return ResponseEntity.status(401).build();
            }
        } else {
            return ResponseEntity.status(404).build();
        }
    }
}