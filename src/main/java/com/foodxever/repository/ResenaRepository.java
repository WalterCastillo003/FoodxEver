package com.foodxever.repository;

import com.foodxever.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findAllByOrderByFechaCreacionDesc();
    List<Resena> findByEstablecimiento_IdOrderByFechaCreacionDesc(Long establecimientoId);
    boolean existsByReserva_Id(Long reservaId);
    boolean existsByUsuario_Id(Long usuarioId);
}
