package com.example.restapi.client.window;

import java.sql.SQLException;

import com.example.restapi.client.db.Bbdd;

public class WindowLaucher {
    public static void main(String[] args) {
        Bbdd.initBD("BaseDatosConciertos");
		try {
			Bbdd.crearTablas();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        new MenuPrincipal();
    }
}
