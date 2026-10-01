package com.foodxever.service;

import com.foodxever.model.Establecimiento;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EstablecimientoService {

    private final List<Establecimiento> establecimientos =
            new ArrayList<>();

    private final AtomicLong contadorId =
            new AtomicLong(1);


    public EstablecimientoService() {

        agregar(new Establecimiento(
                null,
                "Panadería San Miguel",
                "San Miguel",
                "Panadería",
                "987654321",
                "sanmiguel@foodxever.com"
        ));

        agregar(new Establecimiento(
                null,
                "Café Central",
                "Pueblo Libre",
                "Cafetería",
                "987654322",
                "cafecentral@foodxever.com"
        ));

        agregar(new Establecimiento(
                null,
                "Pizza House",
                "Magdalena",
                "Restaurante",
                "987654323",
                "pizzahouse@foodxever.com"
        ));
    }


    // LISTAR

    public List<Establecimiento> listar() {

        return new ArrayList<>(
                establecimientos
        );
    }


    // AGREGAR

    public Establecimiento agregar(
            Establecimiento establecimiento) {

        establecimiento.setId(
                contadorId.getAndIncrement()
        );

        establecimientos.add(
                establecimiento
        );

        return establecimiento;
    }


    // CONSULTAR

    public Optional<Establecimiento> buscarPorId(
            Long id) {

        return establecimientos.stream()
                .filter(
                        establecimiento ->
                                establecimiento
                                        .getId()
                                        .equals(id)
                )
                .findFirst();
    }


    // BUSCAR

    public List<Establecimiento> buscarPorNombre(
            String nombre) {

        if (nombre == null || nombre.isBlank()) {
            return listar();
        }

        String texto =
                nombre.toLowerCase();

        return establecimientos.stream()
                .filter(
                        establecimiento ->
                                establecimiento
                                        .getNombre()
                                        .toLowerCase()
                                        .contains(texto)
                )
                .toList();
    }


    public List<Establecimiento> buscarPorCategoria(
            String categoria) {

        if (categoria == null || categoria.isBlank()) {
            return listar();
        }

        return establecimientos.stream()
                .filter(
                        establecimiento ->
                                establecimiento
                                        .getCategoria()
                                        .equalsIgnoreCase(categoria)
                )
                .toList();
    }


    // ACTUALIZAR

    public boolean actualizar(
            Long id,
            Establecimiento actualizado) {

        Optional<Establecimiento> resultado =
                buscarPorId(id);

        if (resultado.isEmpty()) {
            return false;
        }

        Establecimiento establecimiento =
                resultado.get();

        establecimiento.setNombre(
                actualizado.getNombre()
        );

        establecimiento.setDireccion(
                actualizado.getDireccion()
        );

        establecimiento.setCategoria(
                actualizado.getCategoria()
        );

        establecimiento.setTelefono(
                actualizado.getTelefono()
        );

        establecimiento.setCorreo(
                actualizado.getCorreo()
        );

        return true;
    }


    // ELIMINAR

    public boolean eliminar(Long id) {

        return establecimientos.removeIf(
                establecimiento ->
                        establecimiento
                                .getId()
                                .equals(id)
        );
    }


    public int contar() {

        return establecimientos.size();
    }
}