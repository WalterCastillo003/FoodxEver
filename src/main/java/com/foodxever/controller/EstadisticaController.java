package com.foodxever.controller;

import com.foodxever.model.Producto;
import com.foodxever.model.Reserva;
import com.foodxever.service.EstablecimientoService;
import com.foodxever.service.ProductoService;
import com.foodxever.service.ReservaService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/estadisticas")
public class EstadisticaController {

    private final ProductoService productoService;
    private final EstablecimientoService establecimientoService;
    private final ReservaService reservaService;


    public EstadisticaController(
            ProductoService productoService,
            EstablecimientoService establecimientoService,
            ReservaService reservaService) {

        this.productoService = productoService;
        this.establecimientoService = establecimientoService;
        this.reservaService = reservaService;
    }


    // ==========================================
    // ESTADÍSTICAS GENERALES
    // ==========================================

    @GetMapping
    public String estadisticasGenerales(Model model) {

        List<Producto> productos =
                productoService.listar();

        // ======================================
        // TARJETAS DE RESUMEN
        // ======================================

        model.addAttribute(
                "totalProductos",
                productoService.contar()
        );

        model.addAttribute(
                "productosActivos",
                productoService.contarActivos()
        );

        model.addAttribute(
                "totalEstablecimientos",
                establecimientoService.contar()
        );

        model.addAttribute(
                "totalReservas",
                reservaService.contar()
        );


        // ======================================
        // PRODUCTOS POR CATEGORÍA
        // ======================================

        Map<String, Integer> productosPorCategoria =
                new LinkedHashMap<>();

        productosPorCategoria.put(
                "Panadería",
                0
        );

        productosPorCategoria.put(
                "Cafetería",
                0
        );

        productosPorCategoria.put(
                "Restaurante",
                0
        );

        productosPorCategoria.put(
                "Market",
                0
        );


        for (Producto producto : productos) {

            String categoria =
                    producto.getCategoria();

            productosPorCategoria.put(
                    categoria,
                    productosPorCategoria
                            .getOrDefault(
                                    categoria,
                                    0
                            ) + 1
            );
        }


        model.addAttribute(
                "categorias",
                new ArrayList<>(
                        productosPorCategoria.keySet()
                )
        );

        model.addAttribute(
                "cantidadPorCategoria",
                new ArrayList<>(
                        productosPorCategoria.values()
                )
        );


        // ======================================
        // ESTADOS DE RESERVAS
        // ======================================

        long pendientes =
                reservaService.contarPorEstado(
                        "Pendiente"
                );

        long completadas =
                reservaService.contarPorEstado(
                        "Completada"
                );

        long canceladas =
                reservaService.contarPorEstado(
                        "Cancelada"
                );


        model.addAttribute(
                "estadosReserva",
                List.of(
                        "Pendientes",
                        "Completadas",
                        "Canceladas"
                )
        );


        model.addAttribute(
                "cantidadEstadosReserva",
                List.of(
                        pendientes,
                        completadas,
                        canceladas
                )
        );


        return "estadisticas";
    }


    // ==========================================
    // ESTADÍSTICAS DE RESERVAS
    // ==========================================

    @GetMapping("/reservas")
    public String estadisticasReservas(
            Model model) {

        List<Reserva> reservas =
                reservaService.listar();


        // ======================================
        // RESUMEN
        // ======================================

        double totalReservado =
                reservas.stream()
                        .filter(
                                reserva ->
                                        !"Cancelada".equalsIgnoreCase(
                                                reserva.getEstado()
                                        )
                        )
                        .mapToDouble(
                                Reserva::getTotal
                        )
                        .sum();


        double totalCompletado =
                reservas.stream()
                        .filter(
                                reserva ->
                                        "Completada".equalsIgnoreCase(
                                                reserva.getEstado()
                                        )
                        )
                        .mapToDouble(
                                Reserva::getTotal
                        )
                        .sum();


        int unidadesReservadas =
                reservas.stream()
                        .filter(
                                reserva ->
                                        !"Cancelada".equalsIgnoreCase(
                                                reserva.getEstado()
                                        )
                        )
                        .mapToInt(
                                Reserva::getCantidad
                        )
                        .sum();


        model.addAttribute(
                "totalReservado",
                totalReservado
        );

        model.addAttribute(
                "totalCompletado",
                totalCompletado
        );

        model.addAttribute(
                "unidadesReservadas",
                unidadesReservadas
        );


        // ======================================
        // GRÁFICO LINEAL:
        // TOTAL DE CADA RESERVA
        // ======================================

        List<String> etiquetasReservas =
                new ArrayList<>();

        List<Double> totalesReservas =
                new ArrayList<>();


        for (Reserva reserva : reservas) {

            etiquetasReservas.add(
                    "Reserva #" +
                    reserva.getId()
            );

            totalesReservas.add(
                    reserva.getTotal()
            );
        }


        model.addAttribute(
                "etiquetasReservas",
                etiquetasReservas
        );

        model.addAttribute(
                "totalesReservas",
                totalesReservas
        );


        // ======================================
        // CANTIDAD RESERVADA POR PRODUCTO
        // ======================================

        Map<String, Integer> reservasPorProducto =
                new LinkedHashMap<>();


        for (Reserva reserva : reservas) {

            if (
                    "Cancelada".equalsIgnoreCase(
                            reserva.getEstado()
                    )
            ) {
                continue;
            }


            String producto =
                    reserva.getProductoNombre();


            reservasPorProducto.put(
                    producto,
                    reservasPorProducto
                            .getOrDefault(
                                    producto,
                                    0
                            )
                            + reserva.getCantidad()
            );
        }


        model.addAttribute(
                "productosReserva",
                new ArrayList<>(
                        reservasPorProducto.keySet()
                )
        );

        model.addAttribute(
                "cantidadesReserva",
                new ArrayList<>(
                        reservasPorProducto.values()
                )
        );


        return "estadisticas-reservas";
    }
}