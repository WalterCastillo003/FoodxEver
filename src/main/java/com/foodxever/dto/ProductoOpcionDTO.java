package com.foodxever.dto;

/** DTO mínimo para poblar el segundo desplegable de las reservas. */
public class ProductoOpcionDTO {
    private final Long id;
    private final String nombre;
    private final double precio;
    private final int stock;

    public ProductoOpcionDTO(Long id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
}
