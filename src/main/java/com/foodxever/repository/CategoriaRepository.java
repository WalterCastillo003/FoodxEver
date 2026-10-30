package com.foodxever.repository;

import com.foodxever.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombreIgnoreCase(String nombre);
    List<Categoria> findByActivaTrueOrderByNombreAsc();
    List<Categoria> findByTipoEstablecimientoIgnoreCaseAndActivaTrueOrderByNombreAsc(String tipoEstablecimiento);
}
