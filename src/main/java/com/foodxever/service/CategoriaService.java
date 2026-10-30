package com.foodxever.service;

import com.foodxever.model.Categoria;
import com.foodxever.repository.CategoriaRepository;
import com.foodxever.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoriaService {
    private static final Set<String> TIPOS_ESTABLECIMIENTO = Set.of(
            "Panadería", "Cafetería", "Restaurante", "Market");

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    /** Solo categorías activas: se utiliza en formularios públicos y listas en cascada. */
    public List<Categoria> listar() {
        return categoriaRepository.findByActivaTrueOrderByNombreAsc();
    }

    /** Todas las categorías, incluidas las que están desactivadas, para administración. */
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAllByOrderByNombreAsc();
    }

    public List<Categoria> listarPorTipoEstablecimiento(String tipo) {
        if (tipo == null || tipo.isBlank()) return listar();
        return categoriaRepository.findByTipoEstablecimientoIgnoreCaseAndActivaTrueOrderByNombreAsc(tipo.trim());
    }

    public Optional<Categoria> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return Optional.empty();
        return categoriaRepository.findByNombreIgnoreCase(nombre.trim());
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    @Transactional
    public Categoria crear(Categoria categoria) {
        validar(categoria, null);
        categoria.setNombre(categoria.getNombre().trim());
        categoria.setTipoEstablecimiento(categoria.getTipoEstablecimiento().trim());
        categoria.setDescripcion(validarDescripcion(categoria.getDescripcion()));
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Categoria actualizar(Long id, Categoria datos) {
        Categoria actual = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la categoría."));
        validar(datos, id);
        boolean tieneProductos = productoRepository.existsByCategoriaRef_Id(id);
        if (!datos.isActiva() && tieneProductos) {
            throw new IllegalArgumentException("No se puede desactivar una categoría que todavía está asignada a productos.");
        }
        if (tieneProductos && !actual.getTipoEstablecimiento().equalsIgnoreCase(datos.getTipoEstablecimiento().trim())) {
            throw new IllegalArgumentException("No se puede cambiar el tipo de una categoría que ya está asignada a productos.");
        }
        actual.setNombre(datos.getNombre().trim());
        actual.setTipoEstablecimiento(datos.getTipoEstablecimiento().trim());
        actual.setDescripcion(validarDescripcion(datos.getDescripcion()));
        actual.setActiva(datos.isActiva());
        return categoriaRepository.save(actual);
    }

    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la categoría."));
        if (productoRepository.existsByCategoriaRef_Id(id)) {
            throw new IllegalArgumentException("No se puede eliminar una categoría que está relacionada con productos. Puedes conservarla y editar sus datos.");
        }
        categoriaRepository.delete(categoria);
    }

    private void validar(Categoria categoria, Long idActual) {
        if (categoria.getNombre() == null || categoria.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio.");
        }
        if (categoria.getNombre().trim().length() > 70) {
            throw new IllegalArgumentException("El nombre no puede superar 70 caracteres.");
        }
        if (categoria.getTipoEstablecimiento() == null
                || !TIPOS_ESTABLECIMIENTO.contains(categoria.getTipoEstablecimiento().trim())) {
            throw new IllegalArgumentException("Selecciona un tipo de establecimiento válido.");
        }
        boolean duplicada = idActual == null
                ? categoriaRepository.existsByNombreIgnoreCase(categoria.getNombre().trim())
                : categoriaRepository.existsByNombreIgnoreCaseAndIdNot(categoria.getNombre().trim(), idActual);
        if (duplicada) throw new IllegalArgumentException("Ya existe una categoría con ese nombre.");
    }

    private String validarDescripcion(String valor) {
        if (valor == null || valor.isBlank()) return null;
        String limpio = valor.trim();
        if (limpio.length() > 250) throw new IllegalArgumentException("La descripción no puede superar 250 caracteres.");
        return limpio;
    }
}
