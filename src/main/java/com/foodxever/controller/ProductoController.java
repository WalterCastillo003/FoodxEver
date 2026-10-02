package com.foodxever.controller;

import com.foodxever.model.Producto;
import com.foodxever.service.ProductoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;


    public ProductoController(
            ProductoService productoService) {

        this.productoService = productoService;
    }


    // ==========================================
    // LISTAR PRODUCTOS
    // ==========================================

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "productos",
                productoService.listar()
        );

        return "productos";
    }


    // ==========================================
    // MOSTRAR FORMULARIO NUEVO
    // ==========================================

@GetMapping("/nuevo")
public String nuevo(Model model) {

    Producto producto =
            new Producto();

    producto.setActivo(true);


    model.addAttribute(
            "producto",
            producto
    );


    model.addAttribute(
            "modoEdicion",
            false
    );


    return "producto-form";
}


    // ==========================================
    // GUARDAR PRODUCTO
    // ==========================================

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute Producto producto) {

        productoService.agregar(
                producto
        );

        return "redirect:/productos";
    }


    // ==========================================
    // VER DETALLE
    // ==========================================

    @GetMapping("/detalle/{id}")
    public String detalle(
            @PathVariable Long id,
            Model model) {

        Optional<Producto> resultado =
                productoService.buscarPorId(id);


        if (resultado.isEmpty()) {

            return "redirect:/productos";
        }


        model.addAttribute(
                "producto",
                resultado.get()
        );


        return "producto-detalle";
    }


    // ==========================================
    // FORMULARIO EDITAR
    // ==========================================

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model) {

        Optional<Producto> resultado =
                productoService.buscarPorId(id);


        if (resultado.isEmpty()) {

            return "redirect:/productos";
        }


        model.addAttribute(
                "producto",
                resultado.get()
        );


        model.addAttribute(
                "modoEdicion",
                true
        );


        return "producto-form";
    }


    // ==========================================
    // ACTUALIZAR PRODUCTO
    // ==========================================

    @PostMapping("/actualizar/{id}")
    public String actualizar(
            @PathVariable Long id,
            @ModelAttribute Producto producto) {

        productoService.actualizar(
                id,
                producto
        );


        return "redirect:/productos";
    }


    // ==========================================
    // ELIMINAR PRODUCTO
    // ==========================================

    @PostMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id) {

        productoService.eliminar(id);

        return "redirect:/productos";
    }


    // ==========================================
    // BUSCAR PRODUCTOS
    // ==========================================

    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(required = false)
            String nombre,
            Model model) {

        List<Producto> productos =
                productoService.buscarPorNombre(
                        nombre
                );


        model.addAttribute(
                "productos",
                productos
        );


        model.addAttribute(
                "busqueda",
                nombre
        );


        return "productos";
    }
}