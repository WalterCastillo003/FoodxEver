package com.foodxever.service;

import com.foodxever.model.Rol;
import com.foodxever.repository.RolRepository;
import com.foodxever.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
public class RolService {
    private static final Set<String> ROLES_SISTEMA = Set.of("ADMIN", "CONSUMIDOR", "ESTABLECIMIENTO");

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    public RolService(RolRepository rolRepository, UsuarioRepository usuarioRepository) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Rol> listar() {
        return rolRepository.findAllByOrderByNombreAsc();
    }

    public Optional<Rol> buscarPorId(Long id) {
        return rolRepository.findById(id);
    }

    @Transactional
    public Rol crear(Rol rol) {
        String nombre = normalizarNombre(rol.getNombre());
        if (rolRepository.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException("Ya existe un rol con ese nombre.");
        }
        rol.setNombre(nombre);
        rol.setDescripcion(validarDescripcion(rol.getDescripcion()));
        return rolRepository.save(rol);
    }

    @Transactional
    public Rol actualizar(Long id, Rol datos) {
        Rol actual = rolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el rol."));
        String nuevoNombre = normalizarNombre(datos.getNombre());
        boolean cambioNombre = !actual.getNombre().equalsIgnoreCase(nuevoNombre);
        if (cambioNombre && ROLES_SISTEMA.contains(actual.getNombre().toUpperCase(Locale.ROOT))) {
            throw new IllegalArgumentException("No se puede cambiar el nombre de un rol reservado del sistema.");
        }
        if (cambioNombre && usuarioRepository.existsByRolRef_Id(id)) {
            throw new IllegalArgumentException("No se puede renombrar un rol que ya está asignado a usuarios.");
        }
        if (rolRepository.existsByNombreIgnoreCaseAndIdNot(nuevoNombre, id)) {
            throw new IllegalArgumentException("Ya existe otro rol con ese nombre.");
        }
        actual.setNombre(nuevoNombre);
        actual.setDescripcion(validarDescripcion(datos.getDescripcion()));
        return rolRepository.save(actual);
    }

    @Transactional
    public void eliminar(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el rol."));
        if (ROLES_SISTEMA.contains(rol.getNombre().toUpperCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Los roles ADMIN, CONSUMIDOR y ESTABLECIMIENTO están reservados para el funcionamiento de FoodxEver.");
        }
        if (usuarioRepository.existsByRolRef_Id(id)) {
            throw new IllegalArgumentException("No se puede eliminar un rol que está asignado a usuarios.");
        }
        rolRepository.delete(rol);
    }

    private String normalizarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio.");
        }
        String limpio = nombre.trim().toUpperCase(Locale.ROOT);
        if (limpio.length() > 40) throw new IllegalArgumentException("El nombre del rol no puede superar 40 caracteres.");
        if (!limpio.matches("[A-ZÁÉÍÓÚÜÑ][A-ZÁÉÍÓÚÜÑ0-9_ -]*")) {
            throw new IllegalArgumentException("Usa letras, números, espacios, guiones o guion bajo en el rol.");
        }
        return limpio;
    }

    private String validarDescripcion(String valor) {
        if (valor == null || valor.isBlank()) return null;
        String limpio = valor.trim();
        if (limpio.length() > 180) throw new IllegalArgumentException("La descripción no puede superar 180 caracteres.");
        return limpio;
    }
}
