package com.foodxever.controller;

import com.foodxever.model.Establecimiento;
import com.foodxever.service.EstablecimientoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/establecimientos")
public class EstablecimientoController {

    private final EstablecimientoService establecimientoService;


    public EstablecimientoController(
            EstablecimientoService establecimientoService) {

        this.establecimientoService =
                establecimientoService;
    }


    // ==========================================
    // LISTAR
    // ==========================================

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "establecimientos",
                establecimientoService.listar()
        );

        return "establecimientos";
    }


    // ==========================================
    // NUEVO
    // ==========================================

    @GetMapping("/nuevo")
    public String nuevo(Model model) {

        model.addAttribute(
                "establecimiento",
                new Establecimiento()
        );

        model.addAttribute(
                "modoEdicion",
                false
        );

        return "establecimiento-form";
    }


    // ==========================================
    // GUARDAR
    // ==========================================

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute
            Establecimiento establecimiento) {

        establecimientoService.agregar(
                establecimiento
        );

        return "redirect:/establecimientos";
    }


    // ==========================================
    // DETALLE
    // ==========================================

    @GetMapping("/detalle/{id}")
    public String detalle(
            @PathVariable Long id,
            Model model) {

        Optional<Establecimiento> resultado =
                establecimientoService.buscarPorId(id);


        if (resultado.isEmpty()) {

            return "redirect:/establecimientos";
        }


        model.addAttribute(
                "establecimiento",
                resultado.get()
        );

        return "establecimiento-detalle";
    }


    // ==========================================
    // EDITAR
    // ==========================================

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model) {

        Optional<Establecimiento> resultado =
                establecimientoService.buscarPorId(id);


        if (resultado.isEmpty()) {

            return "redirect:/establecimientos";
        }


        model.addAttribute(
                "establecimiento",
                resultado.get()
        );

        model.addAttribute(
                "modoEdicion",
                true
        );

        return "establecimiento-form";
    }


    // ==========================================
    // ACTUALIZAR
    // ==========================================

    @PostMapping("/actualizar/{id}")
    public String actualizar(
            @PathVariable Long id,
            @ModelAttribute
            Establecimiento establecimiento) {

        establecimientoService.actualizar(
                id,
                establecimiento
        );

        return "redirect:/establecimientos";
    }


    // ==========================================
    // ELIMINAR
    // ==========================================

    @PostMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id) {

        establecimientoService.eliminar(id);

        return "redirect:/establecimientos";
    }


    // ==========================================
    // BUSCAR
    // ==========================================

    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(required = false)
            String nombre,
            Model model) {

        List<Establecimiento> establecimientos =
                establecimientoService
                        .buscarPorNombre(nombre);


        model.addAttribute(
                "establecimientos",
                establecimientos
        );


        model.addAttribute(
                "busqueda",
                nombre
        );


        return "establecimientos";
    }
}