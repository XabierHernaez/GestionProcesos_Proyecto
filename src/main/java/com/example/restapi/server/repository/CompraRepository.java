package com.example.restapi.server.repository;

import com.example.restapi.server.jpa.CompraJPA;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompraRepository extends JpaRepository<CompraJPA, Long>{
    List<CompraJPA> findByUsuarioEmail(String email);
}
