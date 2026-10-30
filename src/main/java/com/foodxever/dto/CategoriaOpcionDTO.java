package com.foodxever.dto;

/** DTO pequeño para no exponer entidades JPA desde el endpoint de opciones. */
public class CategoriaOpcionDTO {
    private final Long id;
    private final String nombre;

    public CategoriaOpcionDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
}
