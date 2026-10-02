package com.foodxever.service;

import com.foodxever.model.Producto;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductoService {

    private final List<Producto> productos =
            new ArrayList<>();

    private final AtomicLong contadorId =
            new AtomicLong(1);


    public ProductoService() {

        // ======================================
        // PRODUCTOS INICIALES FOODXEVER
        // ======================================

        agregar(
                new Producto(
                        null,
                        "Pack sorpresa de panadería",
                        "Panadería",
                        8.90,
                        22.00,
                        4,
                        "Pack con productos disponibles al finalizar el día.",
                        "7:00 PM - 9:00 PM",
                        true,
                        "/img/panaderia.jpg"
                )
        );


        agregar(
                new Producto(
                        null,
                        "Pack de postres",
                        "Cafetería",
                        12.00,
                        24.00,
                        2,
                        "Selección de postres preparados durante el día.",
                        "8:00 PM - 9:30 PM",
                        true,
                        "/img/postres.jpg"
                )
        );


        agregar(
                new Producto(
                        null,
                        "Pizza del día",
                        "Restaurante",
                        16.90,
                        30.00,
                        5,
                        "Pizza disponible antes del cierre del establecimiento.",
                        "9:00 PM - 10:00 PM",
                        true,
                        "/img/pizza.jpg"
                )
        );


        agregar(
                new Producto(
                        null,
                        "Pack de frutas y verduras",
                        "Market",
                        10.50,
                        23.00,
                        7,
                        "Selección de frutas y verduras disponibles durante el día.",
                        "6:00 PM - 8:00 PM",
                        true,
                        "/img/frutas-verduras.jpg"
                )
        );


        agregar(
                new Producto(
                        null,
                        "Menú ejecutivo",
                        "Restaurante",
                        13.90,
                        24.00,
                        3,
                        "Menú preparado durante el servicio de almuerzo.",
                        "4:00 PM - 5:30 PM",
                        true,
                        "/img/menu-ejecutivo.jpg"
                )
        );


        agregar(
                new Producto(
                        null,
                        "Sándwich + café",
                        "Cafetería",
                        7.90,
                        16.00,
                        3,
                        "Sándwich acompañado de café disponible antes del cierre.",
                        "5:30 PM - 7:30 PM",
                        true,
                        "/img/sandwich-cafe.jpg"
                )
        );
    }


    // ======================================
    // LISTAR
    // ======================================

    public List<Producto> listar() {

        return new ArrayList<>(productos);
    }


    // ======================================
    // AGREGAR
    // ======================================

    public Producto agregar(
            Producto producto) {

        producto.setId(
                contadorId.getAndIncrement()
        );


        // Imagen por defecto

        if (
                producto.getImagen() == null ||
                producto.getImagen().isBlank()
        ) {

            producto.setImagen(
                    "/img/hero-foodxever.jpg"
            );
        }


        productos.add(producto);

        return producto;
    }


    // ======================================
    // CONSULTAR POR ID
    // ======================================

    public Optional<Producto> buscarPorId(
            Long id) {

        return productos
                .stream()
                .filter(
                        producto ->
                                producto.getId()
                                        .equals(id)
                )
                .findFirst();
    }


    // ======================================
    // BUSCAR POR NOMBRE
    // ======================================

    public List<Producto> buscarPorNombre(
            String nombre) {

        if (
                nombre == null ||
                nombre.isBlank()
        ) {

            return listar();
        }


        String texto =
                nombre.toLowerCase();


        return productos
                .stream()
                .filter(
                        producto ->
                                producto
                                        .getNombre()
                                        .toLowerCase()
                                        .contains(texto)
                )
                .toList();
    }


    // ======================================
    // BUSCAR POR CATEGORÍA
    // ======================================

    public List<Producto> buscarPorCategoria(
            String categoria) {

        if (
                categoria == null ||
                categoria.isBlank()
        ) {

            return listar();
        }


        return productos
                .stream()
                .filter(
                        producto ->
                                producto
                                        .getCategoria()
                                        .equalsIgnoreCase(
                                                categoria
                                        )
                )
                .toList();
    }


    // ======================================
    // ACTUALIZAR
    // ======================================

    public boolean actualizar(
            Long id,
            Producto productoActualizado) {

        Optional<Producto> resultado =
                buscarPorId(id);


        if (resultado.isEmpty()) {
            return false;
        }


        Producto producto =
                resultado.get();


        producto.setNombre(
                productoActualizado.getNombre()
        );

        producto.setCategoria(
                productoActualizado.getCategoria()
        );

        producto.setPrecio(
                productoActualizado.getPrecio()
        );

        producto.setPrecioOriginal(
                productoActualizado
                        .getPrecioOriginal()
        );

        producto.setStock(
                productoActualizado.getStock()
        );

        producto.setDescripcion(
                productoActualizado
                        .getDescripcion()
        );

        producto.setHorarioRecojo(
                productoActualizado
                        .getHorarioRecojo()
        );

        producto.setActivo(
                productoActualizado.isActivo()
        );

        producto.setImagen(
                productoActualizado.getImagen()
        );


        return true;
    }


    // ======================================
    // ELIMINAR
    // ======================================

    public boolean eliminar(Long id) {

        return productos.removeIf(
                producto ->
                        producto.getId()
                                .equals(id)
        );
    }


    // ======================================
    // CONTAR
    // ======================================

    public int contar() {

        return productos.size();
    }


    public long contarActivos() {

        return productos
                .stream()
                .filter(
                        Producto::isActivo
                )
                .count();
    }
}