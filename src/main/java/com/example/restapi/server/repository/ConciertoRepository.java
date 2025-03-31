package com.example.restapi.server.repository;

import com.example.restapi.server.jpa.ConciertoJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConciertoRepository extends JpaRepository<ConciertoJPA, Integer> {

    // Buscar conciertos por nombre
    List<ConciertoJPA> findByNombre(String nombre);

    // Buscar conciertos por lugar
    List<ConciertoJPA> findByLugar(String lugar);

    // Buscar conciertos por rango de fechas
    List<ConciertoJPA> findByFechaBetween(java.util.Date fechaInicio, java.util.Date fechaFin);

    // Buscar conciertos con capacidad general mayor a un valor
    List<ConciertoJPA> findByCapacidadGeneralGreaterThan(int capacidad);

    // Buscar conciertos con precio VIP menor a un valor
    List<ConciertoJPA> findByPrecioVIPLessThan(double precio);
}
