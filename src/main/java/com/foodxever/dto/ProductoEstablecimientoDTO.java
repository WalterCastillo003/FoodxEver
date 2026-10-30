package com.foodxever.dto;

/** Resultado de una consulta que une productos, categorías y establecimientos. */
public class ProductoEstablecimientoDTO {
    private final Long productoId;
    private final String productoNombre;
    private final String categoria;
    private final String establecimiento;
    private final String direccion;
    private final double precio;
    private final int stock;
    private final String horarioRecojo;

    public ProductoEstablecimientoDTO(Long productoId, String productoNombre, String categoria,
                                     String establecimiento, String direccion, double precio,
                                     int stock, String horarioRecojo) {
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.categoria = categoria;
        this.establecimiento = establecimiento;
        this.direccion = direccion;
        this.precio = precio;
        this.stock = stock;
        this.horarioRecojo = horarioRecojo;
    }

    public Long getProductoId() { return productoId; }
    public String getProductoNombre() { return productoNombre; }
    public String getCategoria() { return categoria; }
    public String getEstablecimiento() { return establecimiento; }
    public String getDireccion() { return direccion; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public String getHorarioRecojo() { return horarioRecojo; }
}
