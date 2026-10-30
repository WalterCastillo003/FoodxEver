package com.foodxever.repository;

import com.foodxever.dto.ProductoEstablecimientoDTO;
import com.foodxever.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findAllByOrderByIdAsc();
    List<Producto> findByNombreContainingIgnoreCaseOrderByIdAsc(String nombre);
    List<Producto> findByCategoriaRef_NombreIgnoreCaseOrderByIdAsc(String categoria);
    List<Producto> findByEstablecimientoRef_IdAndActivoTrueAndStockGreaterThanOrderByNombreAsc(Long establecimientoId, int stock);
    boolean existsByEstablecimientoRef_Id(Long establecimientoId);
    boolean existsByCategoriaRef_Id(Long categoriaId);
    long countByActivoTrue();

    @Query("""
        select new com.foodxever.dto.ProductoEstablecimientoDTO(
            p.id, p.nombre, c.nombre, e.nombre, e.direccion,
            p.precio, p.stock, p.horarioRecojo
        )
        from Producto p
        join p.categoriaRef c
        join p.establecimientoRef e
        where (:establecimientoId is null or e.id = :establecimientoId)
        order by e.nombre, c.nombre, p.nombre
        """)
    List<ProductoEstablecimientoDTO> consultarProductosPorEstablecimiento(
            @Param("establecimientoId") Long establecimientoId);
}
