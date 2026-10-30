package com.foodxever.repository;

import com.foodxever.dto.ReservaResumenDTO;
import com.foodxever.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findAllByOrderByIdDesc();
    List<Reserva> findByClienteContainingIgnoreCaseOrderByIdDesc(String cliente);
    List<Reserva> findByProductoNombreContainingIgnoreCaseOrderByIdDesc(String productoNombre);
    long countByEstadoIgnoreCase(String estado);
    boolean existsByProductoRef_Id(Long productoId);
    boolean existsByUsuarioRef_Id(Long usuarioId);

    @Query("""
        select new com.foodxever.dto.ReservaResumenDTO(
            r.id, r.cliente, r.productoNombre, e.nombre,
            r.cantidad, r.total, r.estado, r.fechaCreacion
        )
        from Reserva r
        join r.productoRef p
        join p.establecimientoRef e
        where (:establecimientoId is null or e.id = :establecimientoId)
        order by r.fechaCreacion desc, r.id desc
        """)
    List<ReservaResumenDTO> consultarReservasDetalladas(
            @Param("establecimientoId") Long establecimientoId);
}
