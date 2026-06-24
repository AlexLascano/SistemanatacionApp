package com.example.sistemanatacionapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static Conexion instancia;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/natacion";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "161604";

    private Conexion() {
        try {
            connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("Conexión exitosa a la base de datos");
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }

    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection getConnection() {
        return connection;
    }
}