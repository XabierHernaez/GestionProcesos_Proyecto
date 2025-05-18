package com.example.restapi.controller;

import com.example.restapi.server.jpa.ConciertoJPA;
import com.example.restapi.server.repository.ConciertoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @class ConciertoController
 * @brief Controlador REST para gestionar los conciertos.
 *
 * Proporciona endpoints para listar, crear, actualizar y eliminar conciertos.
 */
@RestController
@RequestMapping("/api/conciertos")
@CrossOrigin(origins = "*") ///< Permite peticiones desde cualquier origen (útil para pruebas con cliente)
public class ConciertoController {

    @Autowired
    private ConciertoRepository conciertoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @brief Obtiene todos los conciertos registrados.
     * @return Lista de objetos ConciertoJPA.
     */
    @GetMapping
    public List<ConciertoJPA> getAllConciertos() {
        return conciertoRepository.findAll();
    }

    /**
     * @brief Obtiene un concierto por su ID.
     * @param id Identificador del concierto.
     * @return Objeto ConciertoJPA si existe, o 404 si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConciertoJPA> getConciertoById(@PathVariable int id) {
        Optional<ConciertoJPA> concierto = conciertoRepository.findById(id);
        return concierto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * @brief Registra un nuevo concierto.
     * @param nuevoConcierto Objeto ConciertoJPA con los datos del nuevo concierto.
     * @return ConciertoJPA guardado en la base de datos.
     */
    @PostMapping
    public ResponseEntity<ConciertoJPA> addConcierto(@RequestBody ConciertoJPA nuevoConcierto) {
        ConciertoJPA guardado = conciertoRepository.save(nuevoConcierto);
        return ResponseEntity.ok(guardado);
    }

    /**
     * @brief Actualiza los datos de un concierto existente.
     * @param id ID del concierto a modificar.
     * @param datosActualizados Objeto ConciertoJPA con los nuevos valores.
     * @return Concierto actualizado o 404 si no se encuentra.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConciertoJPA> updateConcierto(@PathVariable int id, @RequestBody ConciertoJPA datosActualizados) {
        Optional<ConciertoJPA> existente = conciertoRepository.findById(id);
        if (existente.isPresent()) {
            ConciertoJPA concierto = existente.get();

            // Actualizar campos
            concierto.setNombre(datosActualizados.getNombre());
            concierto.setLugar(datosActualizados.getLugar());
            concierto.setFecha(datosActualizados.getFecha());
            concierto.setCapacidadGeneral(datosActualizados.getCapacidadGeneral());
            concierto.setCapacidadVIP(datosActualizados.getCapacidadVIP());
            concierto.setCapacidadPremium(datosActualizados.getCapacidadPremium());
            concierto.setPrecioGeneral(datosActualizados.getPrecioGeneral());
            concierto.setPrecioVIP(datosActualizados.getPrecioVIP());
            concierto.setPrecioPremium(datosActualizados.getPrecioPremium());

            conciertoRepository.save(concierto);
            return ResponseEntity.ok(concierto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * @brief Elimina un concierto por su ID.
     *
     * También reinicia el contador de autoincremento (AUTO_INCREMENT) de la tabla.
     *
     * @param id ID del concierto a eliminar.
     * @return 204 si se elimina correctamente, 404 si no existe.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConcierto(@PathVariable int id) {
        if (conciertoRepository.existsById(id)) {
            conciertoRepository.deleteById(id);

            // Reiniciar el contador de autoincremento para MySQL
            jdbcTemplate.execute("ALTER TABLE conciertos AUTO_INCREMENT = 1");

            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}