package com.example.restapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class ConciertoTest {

    private Concierto concierto;
    private Date fecha;

    @BeforeEach
    public void setUp() {
        fecha = new Date();
        concierto = new Concierto();
        concierto.setId(1);
        concierto.setNombre("Concierto de Rock");
        concierto.setLugar("Madrid Arena");
        concierto.setFecha(fecha);
        concierto.setCapacidadGeneral(200);
        concierto.setCapacidadVIP(100);
        concierto.setCapacidadPremium(50);
        concierto.setPrecioGeneral(30.0);
        concierto.setPrecioVIP(60.0);
        concierto.setPrecioPremium(120.0);
    }

    // Pruebas para los getters
    @Test
    public void testGetId() {
        assertEquals(1, concierto.getId());
    }

    @Test
    public void testGetNombre() {
        assertEquals("Concierto de Rock", concierto.getNombre());
    }

    @Test
    public void testGetLugar() {
        assertEquals("Madrid Arena", concierto.getLugar());
    }

    @Test
    public void testGetFecha() {
        assertEquals(fecha, concierto.getFecha());
    }

    @Test
    public void testGetCapacidadGeneral() {
        assertEquals(200, concierto.getCapacidadGeneral());
    }

    @Test
    public void testGetCapacidadVIP() {
        assertEquals(100, concierto.getCapacidadVIP());
    }

    @Test
    public void testGetCapacidadPremium() {
        assertEquals(50, concierto.getCapacidadPremium());
    }

    @Test
    public void testGetPrecioGeneral() {
        assertEquals(30.0, concierto.getPrecioGeneral());
    }

    @Test
    public void testGetPrecioVIP() {
        assertEquals(60.0, concierto.getPrecioVIP());
    }

    @Test
    public void testGetPrecioPremium() {
        assertEquals(120.0, concierto.getPrecioPremium());
    }

    // Pruebas para los setters
    @Test
    public void testSetId() {
        concierto.setId(2);
        assertEquals(2, concierto.getId());
    }

    @Test
    public void testSetNombre() {
        concierto.setNombre("Festival de Jazz");
        assertEquals("Festival de Jazz", concierto.getNombre());
    }

    @Test
    public void testSetLugar() {
        concierto.setLugar("Palau Sant Jordi");
        assertEquals("Palau Sant Jordi", concierto.getLugar());
    }

    @Test
    public void testSetFecha() {
        Date nuevaFecha = new Date();
        concierto.setFecha(nuevaFecha);
        assertEquals(nuevaFecha, concierto.getFecha());
    }

    @Test
    public void testSetCapacidadGeneral() {
        concierto.setCapacidadGeneral(300);
        assertEquals(300, concierto.getCapacidadGeneral());
    }

    @Test
    public void testSetCapacidadVIP() {
        concierto.setCapacidadVIP(150);
        assertEquals(150, concierto.getCapacidadVIP());
    }

    @Test
    public void testSetCapacidadPremium() {
        concierto.setCapacidadPremium(70);
        assertEquals(70, concierto.getCapacidadPremium());
    }

    @Test
    public void testSetPrecioGeneral() {
        concierto.setPrecioGeneral(40.0);
        assertEquals(40.0, concierto.getPrecioGeneral());
    }

    @Test
    public void testSetPrecioVIP() {
        concierto.setPrecioVIP(80.0);
        assertEquals(80.0, concierto.getPrecioVIP());
    }

    @Test
    public void testSetPrecioPremium() {
        concierto.setPrecioPremium(150.0);
        assertEquals(150.0, concierto.getPrecioPremium());
    }

    // Pruebas de métodos getCapacidad y getPrecio según TipoEntrada
    @Test
    public void testGetCapacidadPorTipoEntrada() {
        assertEquals(200, concierto.getCapacidad(TipoEntrada.GENERAL));
        assertEquals(100, concierto.getCapacidad(TipoEntrada.VIP));
        assertEquals(50, concierto.getCapacidad(TipoEntrada.PREMIUM));
    }

    @Test
    public void testGetPrecioPorTipoEntrada() {
        assertEquals(30.0, concierto.getPrecio(TipoEntrada.GENERAL));
        assertEquals(60.0, concierto.getPrecio(TipoEntrada.VIP));
        assertEquals(120.0, concierto.getPrecio(TipoEntrada.PREMIUM));
    }

    // Test del constructor con parámetros
    @Test
    public void testConstructorConParametros() {
        Date fechaConcierto = new Date();
        Concierto conciertoParam = new Concierto(
            10,
            "Concierto Pop",
            "Wizink Center",
            fechaConcierto,
            300,
            150,
            80,
            25.0,
            55.0,
            110.0
        );

        assertNotNull(conciertoParam);
        assertEquals(10, conciertoParam.getId());
        assertEquals("Concierto Pop", conciertoParam.getNombre());
        assertEquals("Wizink Center", conciertoParam.getLugar());
        assertEquals(fechaConcierto, conciertoParam.getFecha());
        assertEquals(300, conciertoParam.getCapacidadGeneral());
        assertEquals(150, conciertoParam.getCapacidadVIP());
        assertEquals(80, conciertoParam.getCapacidadPremium());
        assertEquals(25.0, conciertoParam.getPrecioGeneral());
        assertEquals(55.0, conciertoParam.getPrecioVIP());
        assertEquals(110.0, conciertoParam.getPrecioPremium());
    }

    // Test del toString
    @Test
    public void testToString() {
        String expected = concierto.getNombre() + " - " + concierto.getLugar() + " (" + concierto.getFecha() + ")";
        assertEquals(expected, concierto.toString());
    }
}