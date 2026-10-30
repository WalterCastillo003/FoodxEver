package com.foodxever.service;

import com.foodxever.model.MetodoPago;
import com.foodxever.repository.MetodoPagoRepository;
import com.foodxever.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MetodoPagoService {
    private final MetodoPagoRepository metodoPagoRepository;
    private final PagoRepository pagoRepository;

    public MetodoPagoService(MetodoPagoRepository metodoPagoRepository, PagoRepository pagoRepository) {
        this.metodoPagoRepository = metodoPagoRepository;
        this.pagoRepository = pagoRepository;
    }

    public List<MetodoPago> listar() {
        return metodoPagoRepository.findAllByOrderByNombreAsc();
    }

    public Optional<MetodoPago> buscarPorId(Long id) {
        return metodoPagoRepository.findById(id);
    }

    @Transactional
    public MetodoPago crear(MetodoPago metodo) {
        String nombre = validarNombre(metodo.getNombre());
        if (metodoPagoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException("Ya existe un método de pago con ese nombre.");
        }
        metodo.setNombre(nombre);
        return metodoPagoRepository.save(metodo);
    }

    @Transactional
    public MetodoPago actualizar(Long id, MetodoPago datos) {
        MetodoPago actual = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el método de pago."));
        String nombre = validarNombre(datos.getNombre());
        if (metodoPagoRepository.existsByNombreIgnoreCaseAndIdNot(nombre, id)) {
            throw new IllegalArgumentException("Ya existe otro método de pago con ese nombre.");
        }
        if (!datos.isActivo() && pagoRepository.existsByMetodoPago_Id(id)) {
            throw new IllegalArgumentException("No se puede desactivar un método que ya tiene pagos asociados.");
        }
        actual.setNombre(nombre);
        actual.setActivo(datos.isActivo());
        return metodoPagoRepository.save(actual);
    }

    @Transactional
    public void eliminar(Long id) {
        MetodoPago metodo = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el método de pago."));
        if (pagoRepository.existsByMetodoPago_Id(id)) {
            throw new IllegalArgumentException("No se puede eliminar un método que ya está utilizado en pagos. Puedes desactivarlo cuando no tenga pagos asociados.");
        }
        metodoPagoRepository.delete(metodo);
    }

    private String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre del método de pago es obligatorio.");
        String limpio = nombre.trim();
        if (limpio.length() > 60) throw new IllegalArgumentException("El nombre no puede superar 60 caracteres.");
        return limpio;
    }
}
