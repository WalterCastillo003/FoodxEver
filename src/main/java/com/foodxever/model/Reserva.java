package com.foodxever.model;

public class Reserva {

    private Long id;

    private Long productoId;

    private String productoNombre;

    private String cliente;

    private int cantidad;

    private double total;

    private String estado;


    public Reserva() {
    }


    public Reserva(
            Long id,
            Long productoId,
            String productoNombre,
            String cliente,
            int cantidad,
            double total,
            String estado) {

        this.id = id;
        this.productoId = productoId;
        this.productoNombre =
                productoNombre;
        this.cliente = cliente;
        this.cantidad = cantidad;
        this.total = total;
        this.estado = estado;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getProductoId() {
        return productoId;
    }


    public void setProductoId(
            Long productoId) {

        this.productoId =
                productoId;
    }


    public String getProductoNombre() {
        return productoNombre;
    }


    public void setProductoNombre(
            String productoNombre) {

        this.productoNombre =
                productoNombre;
    }


    public String getCliente() {
        return cliente;
    }


    public void setCliente(String cliente) {
        this.cliente = cliente;
    }


    public int getCantidad() {
        return cantidad;
    }


    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


    public double getTotal() {
        return total;
    }


    public void setTotal(double total) {
        this.total = total;
    }


    public String getEstado() {
        return estado;
    }


    public void setEstado(String estado) {
        this.estado = estado;
    }
}