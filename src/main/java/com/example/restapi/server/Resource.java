package com.example.restapi.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.restapi.model.Usuario;
import com.example.restapi.server.jpa.UsuarioJPA;
import com.example.restapi.server.repository.UsuarioRepository;

import java.util.Optional;

@RestController
@RequestMapping("/resource")
public class Resource {

    protected static final Logger logger = LogManager.getLogger();

    @Autowired
    private UsuarioRepository usuarioRepository;

    /** Método que registra un cliente en la base de datos. */
    @PostMapping("/register")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            // Log de los datos recibidos
            logger.info("Datos recibidos: {}", usuario);

            // Validar campos obligatorios
            if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
                logger.error("El campo 'email' es obligatorio.");
                return ResponseEntity.badRequest().body("El campo 'email' es obligatorio.");
            }
            if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
                logger.error("El campo 'nombre' es obligatorio.");
                return ResponseEntity.badRequest().body("El campo 'nombre' es obligatorio.");
            }
            if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                logger.error("El campo 'password' es obligatorio.");
                return ResponseEntity.badRequest().body("El campo 'password' es obligatorio.");
            }

            logger.info("Comprobando que el usuario no exista: '{}'", usuario.getEmail());
            Optional<UsuarioJPA> usuarioExistente = usuarioRepository.findById(usuario.getEmail());

            if (usuarioExistente.isPresent()) {
                logger.info("Usuario ya existe, actualizando datos: {}", usuario.getEmail());
                UsuarioJPA usuarioJPA = usuarioExistente.get();
                usuarioJPA.setPassword(usuario.getPassword());
                usuarioJPA.setNombre(usuario.getNombre());
                usuarioJPA.setApellidos(usuario.getApellidos());
                usuarioJPA.setTelefono(usuario.getTelefono());
                usuarioJPA.setFechaNacimiento(usuario.getFechaNacimiento());
                usuarioJPA.setTipoUsuario(usuario.getTipoUsuario());
                usuarioJPA.setTipoPago(usuario.getTipoPago());
                usuarioRepository.save(usuarioJPA);
                logger.info("Usuario actualizado: {}", usuarioJPA);
            } else {
                logger.info("Creando nuevo usuario: {}", usuario.getEmail());
                UsuarioJPA nuevoUsuario = new UsuarioJPA(
                        usuario.getDni(),
                        usuario.getNombre(),
                        usuario.getApellidos(),
                        usuario.getEmail(),
                        usuario.getPassword(),
                        usuario.getTelefono(),
                        usuario.getFechaNacimiento(),
                        usuario.getTipoPago(),
                        usuario.getTipoUsuario()
                );
                usuarioRepository.save(nuevoUsuario);
                logger.info("Usuario creado: {}", nuevoUsuario);
            }
            return ResponseEntity.ok("Usuario registrado o actualizado con éxito.");
        } catch (Exception e) {
            // Registrar la excepción completa
            logger.error("Error al registrar el usuario: ", e);
            return ResponseEntity.status(500).body("Error al registrar el usuario.");
        }
    }
}