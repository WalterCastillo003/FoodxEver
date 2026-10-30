package com.foodxever.service;

import com.foodxever.model.MetodoPago;
import com.foodxever.model.Pago;
import com.foodxever.model.Reserva;
import com.foodxever.repository.MetodoPagoRepository;
import com.foodxever.repository.PagoRepository;
import com.foodxever.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaRepository reservaRepository;
    private final MetodoPagoRepository metodoPagoRepository;

    public PagoService(PagoRepository pagoRepository,
                      ReservaRepository reservaRepository,
                      MetodoPagoRepository metodoPagoRepository) {
        this.pagoRepository = pagoRepository;
        this.reservaRepository = reservaRepository;
        this.metodoPagoRepository = metodoPagoRepository;
    }

    public List<Pago> listar() {
        return pagoRepository.findAllByOrderByFechaPagoDesc();
    }

    public Optional<Pago> buscarPorId(Long id) {
        return pagoRepository.findById(id);
    }

    public List<Reserva> listarReservasDisponibles(Long pagoId) {
        // Una reserva puede tener más de un registro de pago (por ejemplo, un intento fallido y uno posterior).
        return reservaRepository.findAllByOrderByIdDesc();
    }

    public List<MetodoPago> listarMetodosPago() {
        return metodoPagoRepository.findAllByOrderByNombreAsc();
    }

    @Transactional
    public Pago crear(Long reservaId, Long metodoPagoId, String estado, String referencia) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Selecciona una reserva válida."));
        MetodoPago metodoPago = metodoPagoRepository.findById(metodoPagoId)
                .orElseThrow(() -> new IllegalArgumentException("Selecciona un método de pago válido."));

        Pago pago = new Pago();
        pago.setReserva(reserva);
        pago.setMetodoPago(metodoPago);
        pago.setMonto(calcularMonto(reserva));
        pago.setEstado(normalizarEstado(estado));
        pago.setReferencia(limpiarReferencia(referencia));
        return pagoRepository.save(pago);
    }

    @Transactional
    public Pago actualizar(Long id, Long reservaId, Long metodoPagoId, String estado, String referencia) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el pago que intentas editar."));
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Selecciona una reserva válida."));
        MetodoPago metodoPago = metodoPagoRepository.findById(metodoPagoId)
                .orElseThrow(() -> new IllegalArgumentException("Selecciona un método de pago válido."));

        pago.setReserva(reserva);
        pago.setMetodoPago(metodoPago);
        pago.setMonto(calcularMonto(reserva));
        pago.setEstado(normalizarEstado(estado));
        pago.setReferencia(limpiarReferencia(referencia));
        return pagoRepository.save(pago);
    }

    @Transactional
    public void eliminar(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el pago que intentas eliminar."));
        pagoRepository.delete(pago);
    }

    private BigDecimal calcularMonto(Reserva reserva) {
        return BigDecimal.valueOf(reserva.getTotal()).setScale(2, RoundingMode.HALF_UP);
    }

    private String normalizarEstado(String estado) {
        if (estado == null || estado.isBlank()) return "Pendiente";
        String normalizado = estado.trim();
        if (!List.of("Pendiente", "Pagado", "Fallido", "Reembolsado").contains(normalizado)) {
            throw new IllegalArgumentException("El estado de pago no es válido.");
        }
        return normalizado;
    }

    private String limpiarReferencia(String referencia) {
        if (referencia == null || referencia.isBlank()) return null;
        return referencia.trim();
    }
}
