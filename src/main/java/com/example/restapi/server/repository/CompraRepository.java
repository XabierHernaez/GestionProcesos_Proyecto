package com.example.restapi.server.repository;

import com.example.restapi.server.jpa.CompraJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CompraRepository extends JpaRepository<CompraJPA, Long>{
    
}
