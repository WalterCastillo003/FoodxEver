package com.foodxever.dto;

/** Formulario administrativo: nunca expone la contraseña cifrada almacenada. */
public class UsuarioAdminForm {
    private String nombre;
    private String correo;
    private String contrasena;
    private Long rolId;
    private String nombreEstablecimiento;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public Long getRolId() { return rolId; }
    public void setRolId(Long rolId) { this.rolId = rolId; }
    public String getNombreEstablecimiento() { return nombreEstablecimiento; }
    public void setNombreEstablecimiento(String nombreEstablecimiento) { this.nombreEstablecimiento = nombreEstablecimiento; }
}
