package com.foodxever.model;

import jakarta.persistence.*;

@Entity
@Table(name = "establecimientos")
public class Establecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 180)
    private String direccion;

    @Column(nullable = false, length = 70)
    private String categoria;

    @Column(length = 30)
    private String telefono;

    @Column(length = 160)
    private String correo;

    @Transient
    private Long propietarioId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "propietario_id")
    private Usuario propietarioRef;

    @Column(nullable = false)
    private boolean activo = true;

    public Establecimiento() {
    }

    public Establecimiento(Long id, String nombre, String direccion, String categoria,
                           String telefono, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.categoria = categoria;
        this.telefono = telefono;
        this.correo = correo;
        this.activo = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public Long getPropietarioId() {
        return propietarioRef != null ? propietarioRef.getId() : propietarioId;
    }
    public void setPropietarioId(Long propietarioId) { this.propietarioId = propietarioId; }
    public Usuario getPropietarioRef() { return propietarioRef; }
    public void setPropietarioRef(Usuario propietarioRef) {
        this.propietarioRef = propietarioRef;
        if (propietarioRef != null) this.propietarioId = propietarioRef.getId();
    }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
