package com.foodxever.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 70)
    private String nombre;

    @Column(name = "tipo_establecimiento", nullable = false, length = 70)
    private String tipoEstablecimiento;

    @Column(length = 250)
    private String descripcion;

    @Column(nullable = false)
    private boolean activa = true;

    public Categoria() {
    }

    public Categoria(String nombre, String tipoEstablecimiento, String descripcion) {
        this.nombre = nombre;
        this.tipoEstablecimiento = tipoEstablecimiento;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipoEstablecimiento() { return tipoEstablecimiento; }
    public void setTipoEstablecimiento(String tipoEstablecimiento) { this.tipoEstablecimiento = tipoEstablecimiento; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}
