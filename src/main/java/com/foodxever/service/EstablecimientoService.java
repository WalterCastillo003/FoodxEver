package com.foodxever.service;

import com.foodxever.model.Establecimiento;
import com.foodxever.repository.EstablecimientoRepository;
import com.foodxever.repository.HorarioRecojoRepository;
import com.foodxever.repository.ProductoRepository;
import com.foodxever.repository.ResenaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class EstablecimientoService {
    private final EstablecimientoRepository establecimientoRepository;
    private final ProductoRepository productoRepository;
    private final HorarioRecojoRepository horarioRecojoRepository;
    private final ResenaRepository resenaRepository;

    public EstablecimientoService(EstablecimientoRepository establecimientoRepository,
                                  ProductoRepository productoRepository,
                                  HorarioRecojoRepository horarioRecojoRepository,
                                  ResenaRepository resenaRepository) {
        this.establecimientoRepository = establecimientoRepository;
        this.productoRepository = productoRepository;
        this.horarioRecojoRepository = horarioRecojoRepository;
        this.resenaRepository = resenaRepository;
    }

    public List<Establecimiento> listar() {
        return establecimientoRepository.findByActivoTrueOrderByIdAsc();
    }

    public Establecimiento agregar(Establecimiento establecimiento) {
        establecimiento.setActivo(true);
        return establecimientoRepository.save(establecimiento);
    }

    public Optional<Establecimiento> buscarPorId(Long id) {
        return establecimientoRepository.findById(id);
    }

    public List<Establecimiento> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return listar();
        return establecimientoRepository.findByNombreContainingIgnoreCaseAndActivoTrueOrderByIdAsc(nombre.trim());
    }

    public List<Establecimiento> buscarPorCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) return listar();
        return establecimientoRepository.findByCategoriaIgnoreCaseAndActivoTrueOrderByIdAsc(categoria.trim());
    }

    @Transactional
    public boolean actualizar(Long id, Establecimiento actualizado) {
        Optional<Establecimiento> resultado = establecimientoRepository.findById(id);
        if (resultado.isEmpty()) return false;
        Establecimiento existente = resultado.get();
        existente.setNombre(actualizado.getNombre());
        existente.setDireccion(actualizado.getDireccion());
        existente.setCategoria(actualizado.getCategoria());
        existente.setTelefono(actualizado.getTelefono());
        existente.setCorreo(actualizado.getCorreo());
        establecimientoRepository.save(existente);
        return true;
    }

    @Transactional
    public boolean eliminar(Long id) {
        Optional<Establecimiento> resultado = establecimientoRepository.findById(id);
        if (resultado.isEmpty()) return false;
        Establecimiento establecimiento = resultado.get();
        boolean tieneRelaciones = productoRepository.existsByEstablecimientoRef_Id(id)
                || horarioRecojoRepository.existsByEstablecimiento_Id(id)
                || !resenaRepository.findByEstablecimiento_IdOrderByFechaCreacionDesc(id).isEmpty();
        if (tieneRelaciones) {
            // Conservamos productos, reseñas y horarios históricos vinculados.
            establecimiento.setActivo(false);
            establecimientoRepository.save(establecimiento);
        } else {
            establecimientoRepository.delete(establecimiento);
        }
        return true;
    }

    public int contar() {
        return listar().size();
    }
}
