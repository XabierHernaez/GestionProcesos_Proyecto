package com.example.restapi.server.repository;

import com.example.restapi.model.TipoPago;
import com.example.restapi.model.TipoUsuario;
import com.example.restapi.server.jpa.UsuarioJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioJPA, String> {

    // Encuentra un usuario por su email (ya proporcionado por JpaRepository)
    Optional<UsuarioJPA> findByEmail(String email);

    // Encuentra usuarios por su nombre
    List<UsuarioJPA> findByNombre(String nombre);

    // Encuentra usuarios cuyo nombre contiene una cadena específica (consulta personalizada)
    @Query("SELECT u FROM UsuarioJPA u WHERE u.nombre LIKE %:nombre%")
    List<UsuarioJPA> findByNombreContaining(@Param("nombre") String nombre);

    // Encuentra usuarios por tipo de usuario
    List<UsuarioJPA> findByTipoUsuario(TipoUsuario tipoUsuario);

    // Encuentra usuarios por tipo de pago
    List<UsuarioJPA> findByTipoPago(TipoPago tipoPago);

    // Cuenta el número de usuarios con un tipo de usuario específico
    @Query("SELECT COUNT(u) FROM UsuarioJPA u WHERE u.tipoUsuario = :tipoUsuario")
    long countByTipoUsuario(@Param("tipoUsuario") String tipoUsuario);
}