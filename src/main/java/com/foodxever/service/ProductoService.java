package com.foodxever.service;

import com.foodxever.model.Categoria;
import com.foodxever.model.Establecimiento;
import com.foodxever.model.Producto;
import com.foodxever.repository.CategoriaRepository;
import com.foodxever.repository.EstablecimientoRepository;
import com.foodxever.repository.ProductoRepository;
import com.foodxever.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final EstablecimientoRepository establecimientoRepository;
    private final ReservaRepository reservaRepository;

    public ProductoService(ProductoRepository productoRepository,
                           CategoriaRepository categoriaRepository,
                           EstablecimientoRepository establecimientoRepository,
                           ReservaRepository reservaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.establecimientoRepository = establecimientoRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<Producto> listar() {
        return productoRepository.findAllByOrderByIdAsc();
    }

    public Producto agregar(Producto producto) {
        prepararRelaciones(producto, null);
        if (producto.getImagen() == null || producto.getImagen().isBlank()) {
            producto.setImagen("/img/hero-foodxever.jpg");
        }
        return productoRepository.save(producto);
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return listar();
        return productoRepository.findByNombreContainingIgnoreCaseOrderByIdAsc(nombre.trim());
    }

    public List<Producto> buscarPorCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) return listar();
        return productoRepository.findByCategoriaRef_NombreIgnoreCaseOrderByIdAsc(categoria.trim());
    }

    public List<Producto> listarPorEstablecimiento(Long establecimientoId) {
        if (establecimientoId == null) return List.of();
        return productoRepository.findByEstablecimientoRef_IdAndActivoTrueAndStockGreaterThanOrderByNombreAsc(establecimientoId, 0);
    }

    @Transactional
    public boolean actualizar(Long id, Producto actualizado) {
        Optional<Producto> existenteOpt = productoRepository.findById(id);
        if (existenteOpt.isEmpty()) return false;

        Producto existente = existenteOpt.get();
        prepararRelaciones(actualizado, existente);
        existente.setNombre(actualizado.getNombre());
        existente.setCategoriaRef(actualizado.getCategoriaRef());
        existente.setEstablecimientoRef(actualizado.getEstablecimientoRef());
        existente.setPrecio(actualizado.getPrecio());
        existente.setPrecioOriginal(actualizado.getPrecioOriginal());
        existente.setStock(actualizado.getStock());
        existente.setDescripcion(actualizado.getDescripcion());
        existente.setHorarioRecojo(actualizado.getHorarioRecojo());
        existente.setActivo(actualizado.isActivo());
        existente.setImagen((actualizado.getImagen() == null || actualizado.getImagen().isBlank())
                ? "/img/hero-foodxever.jpg" : actualizado.getImagen());
        productoRepository.save(existente);
        return true;
    }

    @Transactional
    public boolean eliminar(Long id) {
        Optional<Producto> existenteOpt = productoRepository.findById(id);
        if (existenteOpt.isEmpty()) return false;

        Producto producto = existenteOpt.get();
        // Las reservas mantienen una referencia histórica al producto.
        // Si ya se reservó, lo desactivamos en lugar de romper el historial.
        if (reservaRepository.existsByProductoRef_Id(id)) {
            producto.setActivo(false);
            producto.setStock(0);
            productoRepository.save(producto);
        } else {
            productoRepository.delete(producto);
        }
        return true;
    }

    public int contar() {
        return Math.toIntExact(productoRepository.count());
    }

    public long contarActivos() {
        return productoRepository.countByActivoTrue();
    }

    private void prepararRelaciones(Producto producto, Producto existente) {
        String nombreCategoria = producto.getCategoria();
        Categoria categoria = (nombreCategoria == null || nombreCategoria.isBlank())
                ? null : categoriaRepository.findByNombreIgnoreCase(nombreCategoria.trim()).orElse(null);
        if (categoria == null && existente != null) categoria = existente.getCategoriaRef();
        if (categoria == null) {
            throw new IllegalArgumentException("Selecciona una categoría válida para el producto.");
        }

        Long establecimientoId = producto.getEstablecimientoId();
        Establecimiento establecimiento = null;
        if (establecimientoId != null) {
            establecimiento = establecimientoRepository.findById(establecimientoId).orElse(null);
        }
        if (establecimiento == null && existente != null) establecimiento = existente.getEstablecimientoRef();
        if (establecimiento == null) {
            establecimiento = establecimientoRepository.findByActivoTrueOrderByIdAsc().stream().findFirst().orElse(null);
        }
        if (establecimiento == null) {
            throw new IllegalArgumentException("Primero registra un establecimiento activo.");
        }
        if (!categoria.getTipoEstablecimiento().equalsIgnoreCase(establecimiento.getCategoria())) {
            throw new IllegalArgumentException("La categoría seleccionada no corresponde al tipo de establecimiento.");
        }

        producto.setCategoriaRef(categoria);
        producto.setEstablecimientoRef(establecimiento);
    }
}
