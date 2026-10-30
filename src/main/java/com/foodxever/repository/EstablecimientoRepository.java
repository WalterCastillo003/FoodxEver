package com.foodxever.repository;

import com.foodxever.model.Establecimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Long> {
    List<Establecimiento> findByActivoTrueOrderByIdAsc();
    List<Establecimiento> findAllByOrderByIdAsc();
    List<Establecimiento> findByNombreContainingIgnoreCaseAndActivoTrueOrderByIdAsc(String nombre);
    List<Establecimiento> findByCategoriaIgnoreCaseAndActivoTrueOrderByIdAsc(String categoria);
    Optional<Establecimiento> findByNombreIgnoreCase(String nombre);
}
