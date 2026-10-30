package com.foodxever.service;

import com.foodxever.model.HistorialReserva;
import com.foodxever.model.Reserva;
import com.foodxever.repository.HistorialReservaRepository;
import com.foodxever.repository.PagoRepository;
import com.foodxever.repository.ResenaRepository;
import com.foodxever.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final HistorialReservaRepository historialRepository;
    private final PagoRepository pagoRepository;
    private final ResenaRepository resenaRepository;

    public ReservaService(ReservaRepository reservaRepository,
                          HistorialReservaRepository historialRepository,
                          PagoRepository pagoRepository,
                          ResenaRepository resenaRepository) {
        this.reservaRepository = reservaRepository;
        this.historialRepository = historialRepository;
        this.pagoRepository = pagoRepository;
        this.resenaRepository = resenaRepository;
    }

    public List<Reserva> listar() {
        return reservaRepository.findAllByOrderByIdDesc();
    }

    @Transactional
    public Reserva agregar(Reserva reserva) {
        if (reserva.getEstado() == null || reserva.getEstado().isBlank()) {
            reserva.setEstado("Pendiente");
        }
        Reserva guardada = reservaRepository.save(reserva);
        historialRepository.save(new HistorialReserva(guardada, null, guardada.getEstado(), "Reserva creada"));
        return guardada;
    }

    public Optional<Reserva> buscarPorId(Long id) {
        return reservaRepository.findById(id);
    }

    public List<Reserva> buscarPorCliente(String cliente) {
        if (cliente == null || cliente.isBlank()) return listar();
        return reservaRepository.findByClienteContainingIgnoreCaseOrderByIdDesc(cliente.trim());
    }

    public List<Reserva> buscarPorProducto(String producto) {
        if (producto == null || producto.isBlank()) return listar();
        return reservaRepository.findByProductoNombreContainingIgnoreCaseOrderByIdDesc(producto.trim());
    }

    @Transactional
    public boolean actualizar(Long id, Reserva actualizada) {
        Optional<Reserva> resultado = reservaRepository.findById(id);
        if (resultado.isEmpty()) return false;
        Reserva actual = resultado.get();
        String estadoAnterior = actual.getEstado();

        if (actualizada.getProductoRef() != null) {
            actual.setProductoRef(actualizada.getProductoRef());
        } else {
            actual.setProductoId(actualizada.getProductoId());
        }
        actual.setProductoNombre(actualizada.getProductoNombre());
        actual.setCliente(actualizada.getCliente());
        actual.setCantidad(actualizada.getCantidad());
        actual.setTotal(actualizada.getTotal());
        if (actualizada.getEstado() != null && !actualizada.getEstado().isBlank()) {
            actual.setEstado(actualizada.getEstado());
        }
        Reserva guardada = reservaRepository.save(actual);
        if (!estadoAnterior.equalsIgnoreCase(guardada.getEstado())) {
            historialRepository.save(new HistorialReserva(guardada, estadoAnterior,
                    guardada.getEstado(), "Estado actualizado desde el formulario"));
        }
        return true;
    }

    @Transactional
    public boolean cambiarEstado(Long id, String estado) {
        Optional<Reserva> resultado = reservaRepository.findById(id);
        if (resultado.isEmpty()) return false;
        Reserva reserva = resultado.get();
        String anterior = reserva.getEstado();
        if (estado == null || estado.isBlank() || anterior.equalsIgnoreCase(estado)) return true;
        reserva.setEstado(estado);
        Reserva guardada = reservaRepository.save(reserva);
        historialRepository.save(new HistorialReserva(guardada, anterior, estado, "Cambio de estado de reserva"));
        return true;
    }

    @Transactional
    public boolean eliminar(Long id) {
        Optional<Reserva> resultado = reservaRepository.findById(id);
        if (resultado.isEmpty()) return false;
        // No se elimina una reserva si forma parte de un pago o una reseña.
        // Así se conserva la integridad referencial de las tablas relacionadas.
        if (pagoRepository.existsByReserva_Id(id) || resenaRepository.existsByReserva_Id(id)) {
            return false;
        }
        historialRepository.deleteByReserva_Id(id);
        reservaRepository.delete(resultado.get());
        return true;
    }

    public long contarPorEstado(String estado) {
        return reservaRepository.countByEstadoIgnoreCase(estado);
    }

    public int contar() {
        return Math.toIntExact(reservaRepository.count());
    }
}
