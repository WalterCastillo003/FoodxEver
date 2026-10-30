package com.foodxever.repository;

import com.foodxever.model.HistorialReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistorialReservaRepository extends JpaRepository<HistorialReserva, Long> {
    List<HistorialReserva> findByReserva_IdOrderByFechaCambioAsc(Long reservaId);
    List<HistorialReserva> findByReserva_IdOrderByFechaCambioDesc(Long reservaId);
    List<HistorialReserva> findAllByOrderByFechaCambioDesc();
    void deleteByReserva_Id(Long reservaId);
}
