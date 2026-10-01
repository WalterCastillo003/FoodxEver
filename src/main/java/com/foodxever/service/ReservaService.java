package com.foodxever.service;

import com.foodxever.model.Reserva;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservaService {

    private final List<Reserva> reservas =
            new ArrayList<>();

    private final AtomicLong contadorId =
            new AtomicLong(1);


    public ReservaService() {

        agregar(new Reserva(
                null,
                1L,
                "Pack sorpresa de panadería",
                "Cliente Demo",
                1,
                8.90,
                "Pendiente"
        ));
    }


    // ================================
    // LISTAR
    // ================================

    public List<Reserva> listar() {

        return new ArrayList<>(reservas);
    }


    // ================================
    // AGREGAR
    // ================================

    public Reserva agregar(Reserva reserva) {

        reserva.setId(
                contadorId.getAndIncrement()
        );

        // Si no se eligió un estado,
        // automáticamente será Pendiente.

        if (
                reserva.getEstado() == null ||
                reserva.getEstado().isBlank()
        ) {

            reserva.setEstado("Pendiente");
        }

        reservas.add(reserva);

        return reserva;
    }


    // ================================
    // CONSULTAR POR ID
    // ================================

    public Optional<Reserva> buscarPorId(
            Long id) {

        return reservas.stream()
                .filter(
                        reserva ->
                                reserva
                                        .getId()
                                        .equals(id)
                )
                .findFirst();
    }


    // ================================
    // BUSCAR POR CLIENTE
    // ================================

    public List<Reserva> buscarPorCliente(
            String cliente) {

        if (cliente == null || cliente.isBlank()) {
            return listar();
        }

        String texto =
                cliente.toLowerCase();

        return reservas.stream()
                .filter(
                        reserva ->
                                reserva
                                        .getCliente()
                                        .toLowerCase()
                                        .contains(texto)
                )
                .toList();
    }


    // ================================
    // BUSCAR POR PRODUCTO
    // ================================

    public List<Reserva> buscarPorProducto(
            String producto) {

        if (producto == null || producto.isBlank()) {
            return listar();
        }

        String texto =
                producto.toLowerCase();

        return reservas.stream()
                .filter(
                        reserva ->
                                reserva
                                        .getProductoNombre()
                                        .toLowerCase()
                                        .contains(texto)
                )
                .toList();
    }


    // ================================
    // ACTUALIZAR
    // ================================

    public boolean actualizar(
            Long id,
            Reserva actualizada) {

        Optional<Reserva> resultado =
                buscarPorId(id);

        if (resultado.isEmpty()) {
            return false;
        }

        Reserva reserva =
                resultado.get();

        reserva.setProductoId(
                actualizada.getProductoId()
        );

        reserva.setProductoNombre(
                actualizada.getProductoNombre()
        );

        reserva.setCliente(
                actualizada.getCliente()
        );

        reserva.setCantidad(
                actualizada.getCantidad()
        );

        reserva.setTotal(
                actualizada.getTotal()
        );

        reserva.setEstado(
                actualizada.getEstado()
        );

        return true;
    }


    // ================================
    // CAMBIAR ESTADO
    // ================================

    public boolean cambiarEstado(
            Long id,
            String estado) {

        Optional<Reserva> resultado =
                buscarPorId(id);

        if (resultado.isEmpty()) {
            return false;
        }

        resultado
                .get()
                .setEstado(estado);

        return true;
    }


    // ================================
    // ELIMINAR
    // ================================

    public boolean eliminar(Long id) {

        return reservas.removeIf(
                reserva ->
                        reserva
                                .getId()
                                .equals(id)
        );
    }


    // ================================
    // ESTADÍSTICAS
    // ================================

    public long contarPorEstado(
            String estado) {

        return reservas.stream()
                .filter(
                        reserva ->
                                reserva
                                        .getEstado()
                                        .equalsIgnoreCase(estado)
                )
                .count();
    }


    public int contar() {

        return reservas.size();
    }
}