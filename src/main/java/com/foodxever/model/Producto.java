package com.foodxever.model;

public class Producto {

    private Long id;

    private String nombre;

    private String categoria;

    private double precio;

    private double precioOriginal;

    private int stock;

    private String descripcion;

    private String horarioRecojo;

    private boolean activo;


    public Producto() {
    }


    public Producto(
            Long id,
            String nombre,
            String categoria,
            double precio,
            double precioOriginal,
            int stock,
            String descripcion,
            String horarioRecojo,
            boolean activo) {

        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.precioOriginal = precioOriginal;
        this.stock = stock;
        this.descripcion = descripcion;
        this.horarioRecojo = horarioRecojo;
        this.activo = activo;
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


    public String getCategoria() {
        return categoria;
    }


    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }


    public double getPrecio() {
        return precio;
    }


    public void setPrecio(double precio) {
        this.precio = precio;
    }


    public double getPrecioOriginal() {
        return precioOriginal;
    }


    public void setPrecioOriginal(double precioOriginal) {
        this.precioOriginal = precioOriginal;
    }


    public int getStock() {
        return stock;
    }


    public void setStock(int stock) {
        this.stock = stock;
    }


    public String getDescripcion() {
        return descripcion;
    }


    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getHorarioRecojo() {
        return horarioRecojo;
    }


    public void setHorarioRecojo(
            String horarioRecojo) {

        this.horarioRecojo =
                horarioRecojo;
    }


    public boolean isActivo() {
        return activo;
    }


    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}