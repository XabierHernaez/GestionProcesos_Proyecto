package com.example.restapi.controller;

import com.example.restapi.server.repository.ConciertoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

public class ConciertoControllerTest {

    @Mock
    private ConciertoRepository conciertoRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ConciertoController conciertoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
}