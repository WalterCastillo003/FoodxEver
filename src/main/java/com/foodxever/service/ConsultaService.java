package com.foodxever.service;

import com.foodxever.dto.ProductoEstablecimientoDTO;
import com.foodxever.dto.ReservaResumenDTO;
import com.foodxever.repository.ProductoRepository;
import com.foodxever.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ConsultaService {
    private final ProductoRepository productoRepository;
    private final ReservaRepository reservaRepository;

    public ConsultaService(ProductoRepository productoRepository,
                           ReservaRepository reservaRepository) {
        this.productoRepository = productoRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<ProductoEstablecimientoDTO> productosPorEstablecimiento(Long establecimientoId) {
        return productoRepository.consultarProductosPorEstablecimiento(establecimientoId);
    }

    public List<ReservaResumenDTO> reservasPorEstablecimiento(Long establecimientoId) {
        return reservaRepository.consultarReservasDetalladas(establecimientoId);
    }
}
