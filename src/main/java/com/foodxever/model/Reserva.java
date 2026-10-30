package com.foodxever.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private Long productoId;

    @Column(name = "producto_nombre", nullable = false, length = 140)
    private String productoNombre;

    @Column(nullable = false, length = 120)
    private String cliente;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private double total;

    @Column(nullable = false, length = 30)
    private String estado = "Pendiente";

    @Transient
    private Long usuarioId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioRef;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto productoRef;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Transient
    private Long establecimientoId;

    public Reserva() {
    }

    public Reserva(Long id, Long productoId, String productoNombre, String cliente,
                   int cantidad, double total, String estado) {
        this.id = id;
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.cliente = cliente;
        this.cantidad = cantidad;
        this.total = total;
        this.estado = estado;
    }

    @PrePersist
    public void prepararFechaCreacion() {
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        if (estado == null || estado.isBlank()) estado = "Pendiente";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductoId() {
        return productoRef != null ? productoRef.getId() : productoId;
    }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Long getUsuarioId() {
        return usuarioRef != null ? usuarioRef.getId() : usuarioId;
    }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public Usuario getUsuarioRef() { return usuarioRef; }
    public void setUsuarioRef(Usuario usuarioRef) {
        this.usuarioRef = usuarioRef;
        if (usuarioRef != null) this.usuarioId = usuarioRef.getId();
    }
    public Producto getProductoRef() { return productoRef; }
    public void setProductoRef(Producto productoRef) {
        this.productoRef = productoRef;
        if (productoRef != null) {
            this.productoId = productoRef.getId();
            this.productoNombre = productoRef.getNombre();
            this.establecimientoId = productoRef.getEstablecimientoId();
        }
    }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Long getEstablecimientoId() {
        if (productoRef != null) return productoRef.getEstablecimientoId();
        return establecimientoId;
    }
    public void setEstablecimientoId(Long establecimientoId) { this.establecimientoId = establecimientoId; }

    public void vincularProducto(Producto producto) {
        setProductoRef(producto);
    }
}
