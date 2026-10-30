package com.foodxever.repository;

import com.foodxever.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findAllByOrderByFechaPagoDesc();
    List<Pago> findByReserva_IdOrderByFechaPagoDesc(Long reservaId);
    boolean existsByReserva_Id(Long reservaId);
    boolean existsByMetodoPago_Id(Long metodoPagoId);
}
