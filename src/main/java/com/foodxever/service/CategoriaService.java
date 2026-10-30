package com.foodxever.service;

import com.foodxever.model.Categoria;
import com.foodxever.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listar() {
        return categoriaRepository.findByActivaTrueOrderByNombreAsc();
    }

    public List<Categoria> listarPorTipoEstablecimiento(String tipo) {
        if (tipo == null || tipo.isBlank()) return listar();
        return categoriaRepository.findByTipoEstablecimientoIgnoreCaseAndActivaTrueOrderByNombreAsc(tipo.trim());
    }

    public Optional<Categoria> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return Optional.empty();
        return categoriaRepository.findByNombreIgnoreCase(nombre.trim());
    }
}
