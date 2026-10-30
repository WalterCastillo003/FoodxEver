package com.foodxever.dto;

import java.time.LocalDateTime;

/** Vista resumida de reservas unidas con el producto y su establecimiento. */
public class ReservaResumenDTO {
    private final Long reservaId;
    private final String cliente;
    private final String producto;
    private final String establecimiento;
    private final int cantidad;
    private final double total;
    private final String estado;
    private final LocalDateTime fechaCreacion;

    public ReservaResumenDTO(Long reservaId, String cliente, String producto,
                            String establecimiento, int cantidad, double total,
                            String estado, LocalDateTime fechaCreacion) {
        this.reservaId = reservaId;
        this.cliente = cliente;
        this.producto = producto;
        this.establecimiento = establecimiento;
        this.cantidad = cantidad;
        this.total = total;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getReservaId() { return reservaId; }
    public String getCliente() { return cliente; }
    public String getProducto() { return producto; }
    public String getEstablecimiento() { return establecimiento; }
    public int getCantidad() { return cantidad; }
    public double getTotal() { return total; }
    public String getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}
