package com.example.restapi.server.repository;

import com.example.restapi.server.jpa.CompraJPA;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompraRepository extends JpaRepository<CompraJPA, Long> {
    List<CompraJPA> findByUsuarioEmail(String email);

    @Query("SELECT COALESCE(SUM(c.cantidad), 0) FROM CompraJPA c WHERE c.concierto.id = :conciertoId AND c.tipoEntrada = :tipoEntrada")
    int sumCantidadByConciertoIdAndTipoEntrada(@Param("conciertoId") int conciertoId, @Param("tipoEntrada") String tipoEntrada);

    @Query("SELECT c FROM CompraJPA c WHERE c.concierto.id = :conciertoId")
    List<CompraJPA> findByConciertoId(@Param("conciertoId") int conciertoId);
}
