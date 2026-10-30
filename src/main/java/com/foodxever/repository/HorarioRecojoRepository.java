package com.foodxever.repository;

import com.foodxever.model.HorarioRecojo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalTime;
import java.util.List;

public interface HorarioRecojoRepository extends JpaRepository<HorarioRecojo, Long> {
    List<HorarioRecojo> findByEstablecimiento_IdAndActivoTrueOrderByIdAsc(Long establecimientoId);
    List<HorarioRecojo> findAllByOrderByEstablecimiento_NombreAscDiaSemanaAscHoraInicioAsc();
    boolean existsByEstablecimiento_Id(Long establecimientoId);
    boolean existsByEstablecimiento_IdAndDiaSemanaIgnoreCaseAndHoraInicioAndHoraFin(
            Long establecimientoId, String diaSemana, LocalTime horaInicio, LocalTime horaFin);
    boolean existsByEstablecimiento_IdAndDiaSemanaIgnoreCaseAndHoraInicioAndHoraFinAndIdNot(
            Long establecimientoId, String diaSemana, LocalTime horaInicio, LocalTime horaFin, Long id);
}
