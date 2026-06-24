package com.example.sistemanatacionapp.model;

public class Participante {

    private String cedula;
    private String nombre;
    private String apellido;
    private int edad;
    private String correo;
    private String categoria;
    private String genero;
    private String observaciones;

    public Participante(String cedula, String nombre, String apellido, int edad,
                        String correo, String categoria, String genero, String observaciones) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.correo = correo;
        this.categoria = categoria;
        this.genero = genero;
        this.observaciones = observaciones;
    }

    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public int getEdad() { return edad; }
    public String getCorreo() { return correo; }
    public String getCategoria() { return categoria; }
    public String getGenero() { return genero; }
    public String getObservaciones() { return observaciones; }

    public void setCedula(String cedula) { this.cedula = cedula; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setEdad(int edad) { this.edad = edad; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}