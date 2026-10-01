package com.foodxever.controller;

import com.foodxever.model.Producto;
import com.foodxever.model.Reserva;
import com.foodxever.service.ProductoService;
import com.foodxever.service.ReservaService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final ProductoService productoService;


    public ReservaController(
            ReservaService reservaService,
            ProductoService productoService) {

        this.reservaService = reservaService;
        this.productoService = productoService;
    }


    // ==========================================
    // LISTAR
    // ==========================================

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "reservas",
                reservaService.listar()
        );

        return "reservas";
    }


    // ==========================================
    // NUEVA RESERVA
    // ==========================================

    @GetMapping("/nueva")
    public String nueva(Model model) {

        Reserva reserva = new Reserva();

        reserva.setCantidad(1);
        reserva.setEstado("Pendiente");

        model.addAttribute(
                "reserva",
                reserva
        );

        model.addAttribute(
                "productos",
                productoService.listar()
        );

        model.addAttribute(
                "modoEdicion",
                false
        );

        return "reserva-form";
    }


    // ==========================================
    // GUARDAR RESERVA
    // ==========================================

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute Reserva reserva) {

        Optional<Producto> resultado =
                productoService.buscarPorId(
                        reserva.getProductoId()
                );


        if (resultado.isEmpty()) {

            return "redirect:/reservas/nueva?error=producto";
        }


        Producto producto =
                resultado.get();


        // Validar cantidad

        if (
                reserva.getCantidad() <= 0 ||
                reserva.getCantidad() > producto.getStock()
        ) {

            return "redirect:/reservas/nueva?error=stock";
        }


        // La información importante se calcula
        // en el servidor.

        reserva.setProductoNombre(
                producto.getNombre()
        );


        reserva.setTotal(
                producto.getPrecio()
                        * reserva.getCantidad()
        );


        reserva.setEstado(
                "Pendiente"
        );


        reservaService.agregar(
                reserva
        );


        return "redirect:/reservas";
    }


    // ==========================================
    // DETALLE
    // ==========================================

    @GetMapping("/detalle/{id}")
    public String detalle(
            @PathVariable Long id,
            Model model) {

        Optional<Reserva> resultado =
                reservaService.buscarPorId(id);


        if (resultado.isEmpty()) {

            return "redirect:/reservas";
        }


        model.addAttribute(
                "reserva",
                resultado.get()
        );


        return "reserva-detalle";
    }


    // ==========================================
    // EDITAR
    // ==========================================

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model) {

        Optional<Reserva> resultado =
                reservaService.buscarPorId(id);


        if (resultado.isEmpty()) {

            return "redirect:/reservas";
        }


        model.addAttribute(
                "reserva",
                resultado.get()
        );


        model.addAttribute(
                "productos",
                productoService.listar()
        );


        model.addAttribute(
                "modoEdicion",
                true
        );


        return "reserva-form";
    }


    // ==========================================
    // ACTUALIZAR
    // ==========================================

    @PostMapping("/actualizar/{id}")
    public String actualizar(
            @PathVariable Long id,
            @ModelAttribute Reserva reserva) {


        Optional<Producto> resultadoProducto =
                productoService.buscarPorId(
                        reserva.getProductoId()
                );


        if (resultadoProducto.isEmpty()) {

            return "redirect:/reservas";
        }


        Producto producto =
                resultadoProducto.get();


        if (
                reserva.getCantidad() <= 0 ||
                reserva.getCantidad() > producto.getStock()
        ) {

            return "redirect:/reservas/editar/"
                    + id
                    + "?error=stock";
        }


        reserva.setProductoNombre(
                producto.getNombre()
        );


        reserva.setTotal(
                producto.getPrecio()
                        * reserva.getCantidad()
        );


        reservaService.actualizar(
                id,
                reserva
        );


        return "redirect:/reservas";
    }


    // ==========================================
    // ELIMINAR
    // ==========================================

    @PostMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id) {

        reservaService.eliminar(id);

        return "redirect:/reservas";
    }


    // ==========================================
    // BUSCAR POR CLIENTE
    // ==========================================

    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(required = false)
            String cliente,
            Model model) {

        List<Reserva> reservas =
                reservaService.buscarPorCliente(
                        cliente
                );


        model.addAttribute(
                "reservas",
                reservas
        );


        model.addAttribute(
                "busqueda",
                cliente
        );


        return "reservas";
    }


    // ==========================================
    // COMPLETAR
    // ==========================================

    @PostMapping("/completar/{id}")
    public String completar(
            @PathVariable Long id) {

        reservaService.cambiarEstado(
                id,
                "Completada"
        );


        return "redirect:/reservas";
    }


    // ==========================================
    // CANCELAR
    // ==========================================

    @PostMapping("/cancelar/{id}")
    public String cancelar(
            @PathVariable Long id) {

        reservaService.cambiarEstado(
                id,
                "Cancelada"
        );


        return "redirect:/reservas";
    }
}