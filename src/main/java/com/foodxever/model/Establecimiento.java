package com.foodxever.model;

public class Establecimiento {

    private Long id;

    private String nombre;

    private String direccion;

    private String categoria;

    private String telefono;

    private String correo;


    public Establecimiento() {
    }


    public Establecimiento(
            Long id,
            String nombre,
            String direccion,
            String categoria,
            String telefono,
            String correo) {

        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.categoria = categoria;
        this.telefono = telefono;
        this.correo = correo;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getDireccion() {
        return direccion;
    }


    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


    public String getCategoria() {
        return categoria;
    }


    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }


    public String getTelefono() {
        return telefono;
    }


    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    public String getCorreo() {
        return correo;
    }


    public void setCorreo(String correo) {
        this.correo = correo;
    }
}