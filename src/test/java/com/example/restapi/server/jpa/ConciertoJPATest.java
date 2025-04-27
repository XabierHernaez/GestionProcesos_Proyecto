package com.example.restapi.server.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class ConciertoJPATest {

    private ConciertoJPA concierto;

    @BeforeEach
    void setUp() {
        // Setup para el test: crear un objeto ConciertoJPA con valores por defecto.
        concierto = new ConciertoJPA();
    }

    @Test
    void testConstructorSinParametros() {
        // Verificar los valores por defecto que se asignan en el constructor sin parámetros
        assertEquals(100, concierto.getCapacidadGeneral());
        assertEquals(50, concierto.getCapacidadVIP());
        assertEquals(20, concierto.getCapacidadPremium());
        assertEquals(0.0, concierto.getPrecioGeneral());
        assertEquals(0.0, concierto.getPrecioVIP());
        assertEquals(0.0, concierto.getPrecioPremium());
    }

    @Test
    void testConstructorConParametros() {
        // Crear un concierto con los valores proporcionados
        Date fecha = Date.valueOf("2025-05-01");
        ConciertoJPA conciertoConParametros = new ConciertoJPA(1, "Concierto Test", "Lugar Test", fecha, 200, 100, 50, 80.0, 150.0, 200.0);

        // Verificar que los valores se han asignado correctamente
        assertEquals(1, conciertoConParametros.getId());
        assertEquals("Concierto Test", conciertoConParametros.getNombre());
        assertEquals("Lugar Test", conciertoConParametros.getLugar());
        assertEquals(fecha, conciertoConParametros.getFecha());
        assertEquals(200, conciertoConParametros.getCapacidadGeneral());
        assertEquals(100, conciertoConParametros.getCapacidadVIP());
        assertEquals(50, conciertoConParametros.getCapacidadPremium());
        assertEquals(80.0, conciertoConParametros.getPrecioGeneral());
        assertEquals(150.0, conciertoConParametros.getPrecioVIP());
        assertEquals(200.0, conciertoConParametros.getPrecioPremium());
    }

    @Test
    void testGettersYSetters() {
        // Cambiar los valores a través de los setters
        Date fecha = Date.valueOf("2025-06-01");
        concierto.setId(2);
        concierto.setNombre("Concierto Nuevo");
        concierto.setLugar("Nuevo Lugar");
        concierto.setFecha(fecha);
        concierto.setCapacidadGeneral(300);
        concierto.setCapacidadVIP(150);
        concierto.setCapacidadPremium(75);
        concierto.setPrecioGeneral(100.0);
        concierto.setPrecioVIP(200.0);
        concierto.setPrecioPremium(300.0);

        // Verificar que los getters devuelvan los valores establecidos
        assertEquals(2, concierto.getId());
        assertEquals("Concierto Nuevo", concierto.getNombre());
        assertEquals("Nuevo Lugar", concierto.getLugar());
        assertEquals(fecha, concierto.getFecha());
        assertEquals(300, concierto.getCapacidadGeneral());
        assertEquals(150, concierto.getCapacidadVIP());
        assertEquals(75, concierto.getCapacidadPremium());
        assertEquals(100.0, concierto.getPrecioGeneral());
        assertEquals(200.0, concierto.getPrecioVIP());
        assertEquals(300.0, concierto.getPrecioPremium());
    }

    @Test
    void testToString() {
        // Crear un concierto para probar el método toString
        Date fecha = Date.valueOf("2025-07-01");
        ConciertoJPA conciertoParaToString = new ConciertoJPA(1, "Concierto de Verano", "Plaza Mayor", fecha, 500, 250, 100, 150.0, 250.0, 350.0);

        // Verificar que el método toString devuelve la cadena esperada
        String expected = "Concierto de Verano - Plaza Mayor (2025-07-01)";
        assertEquals(expected, conciertoParaToString.toString());
    }
}

