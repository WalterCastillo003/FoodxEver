package com.foodxever.model;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 140)
    private String nombre;

    // Se conserva como propiedad de formulario; la FK real es categoria_id.
    @Transient
    private String categoria;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoriaRef;

    @Column(nullable = false)
    private double precio;

    @Column(name = "precio_original", nullable = false)
    private double precioOriginal;

    @Column(nullable = false)
    private int stock;

    @Column(length = 700)
    private String descripcion;

    @Column(name = "horario_recojo", length = 100)
    private String horarioRecojo;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(length = 240)
    private String imagen;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "establecimiento_id", nullable = false)
    private Establecimiento establecimientoRef;

    @Transient
    private Long establecimientoId;

    public Producto() {
    }

    public Producto(Long id, String nombre, String categoria, double precio,
                    double precioOriginal, int stock, String descripcion,
                    String horarioRecojo, boolean activo, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.precioOriginal = precioOriginal;
        this.stock = stock;
        this.descripcion = descripcion;
        this.horarioRecojo = horarioRecojo;
        this.activo = activo;
        this.imagen = imagen;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() {
        return categoriaRef != null ? categoriaRef.getNombre() : categoria;
    }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public Categoria getCategoriaRef() { return categoriaRef; }
    public void setCategoriaRef(Categoria categoriaRef) {
        this.categoriaRef = categoriaRef;
        if (categoriaRef != null) this.categoria = categoriaRef.getNombre();
    }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public double getPrecioOriginal() { return precioOriginal; }
    public void setPrecioOriginal(double precioOriginal) { this.precioOriginal = precioOriginal; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getHorarioRecojo() { return horarioRecojo; }
    public void setHorarioRecojo(String horarioRecojo) { this.horarioRecojo = horarioRecojo; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public Long getEstablecimientoId() {
        return establecimientoRef != null ? establecimientoRef.getId() : establecimientoId;
    }
    public void setEstablecimientoId(Long establecimientoId) { this.establecimientoId = establecimientoId; }
    public Establecimiento getEstablecimientoRef() { return establecimientoRef; }
    public void setEstablecimientoRef(Establecimiento establecimientoRef) {
        this.establecimientoRef = establecimientoRef;
        if (establecimientoRef != null) this.establecimientoId = establecimientoRef.getId();
    }
    public String getEstablecimientoNombre() {
        return establecimientoRef != null ? establecimientoRef.getNombre() : "Sin establecimiento";
    }
}
