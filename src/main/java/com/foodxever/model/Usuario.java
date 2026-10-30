package com.foodxever.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 160)
    private String correo;

    @Column(name = "contrasena", nullable = false, length = 100)
    private String contrasena;

    // La propiedad de texto se conserva para no romper el formulario Thymeleaf.
    @Transient
    private String rol;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rolRef;

    @Column(name = "nombre_establecimiento", length = 120)
    private String nombreEstablecimiento;

    public Usuario() {
    }

    public Usuario(Long id, String nombre, String correo, String contrasena,
                   String rol, String nombreEstablecimiento) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getRol() {
        return rolRef != null ? rolRef.getNombre() : rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Rol getRolRef() { return rolRef; }
    public void setRolRef(Rol rolRef) {
        this.rolRef = rolRef;
        if (rolRef != null) this.rol = rolRef.getNombre();
    }

    public String getNombreEstablecimiento() { return nombreEstablecimiento; }
    public void setNombreEstablecimiento(String nombreEstablecimiento) { this.nombreEstablecimiento = nombreEstablecimiento; }
}
