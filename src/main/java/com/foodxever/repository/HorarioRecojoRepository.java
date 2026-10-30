package com.foodxever.repository;

import com.foodxever.model.HorarioRecojo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HorarioRecojoRepository extends JpaRepository<HorarioRecojo, Long> {
    List<HorarioRecojo> findByEstablecimiento_IdAndActivoTrueOrderByIdAsc(Long establecimientoId);
    boolean existsByEstablecimiento_Id(Long establecimientoId);
}
