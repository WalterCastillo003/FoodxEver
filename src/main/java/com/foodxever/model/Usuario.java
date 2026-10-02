package com.foodxever.model;

public class Usuario {

    private Long id;
    private String nombre;
    private String correo;
    private String contrasena;
    private String rol;
    private String nombreEstablecimiento;


    public Usuario() {
    }


    public Usuario(
            Long id,
            String nombre,
            String correo,
            String contrasena,
            String rol,
            String nombreEstablecimiento) {

        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
        this.nombreEstablecimiento =
                nombreEstablecimiento;
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


    public String getCorreo() {
        return correo;
    }


    public void setCorreo(String correo) {
        this.correo = correo;
    }


    public String getContrasena() {
        return contrasena;
    }


    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }


    public String getRol() {
        return rol;
    }


    public void setRol(String rol) {
        this.rol = rol;
    }


    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }


    public void setNombreEstablecimiento(
            String nombreEstablecimiento) {

        this.nombreEstablecimiento =
                nombreEstablecimiento;
    }
}