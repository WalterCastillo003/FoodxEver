package com.foodxever.controller;

import com.foodxever.dto.CategoriaOpcionDTO;
import com.foodxever.model.Establecimiento;
import com.foodxever.model.Producto;
import com.foodxever.service.CategoriaService;
import com.foodxever.service.EstablecimientoService;
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
    private final EstablecimientoService establecimientoService;
    private final CategoriaService categoriaService;

    public ProductoController(ProductoService productoService,
                              EstablecimientoService establecimientoService,
                              CategoriaService categoriaService) {
        this.productoService = productoService;
        this.establecimientoService = establecimientoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.listar());
        return "productos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Producto producto = new Producto();
        producto.setActivo(true);
        List<Establecimiento> establecimientos = establecimientoService.listar();
        if (!establecimientos.isEmpty()) producto.setEstablecimientoId(establecimientos.get(0).getId());
        prepararFormulario(model, producto, false);
        return "producto-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto, Model model) {
        try {
            productoService.agregar(producto);
            return "redirect:/productos";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            prepararFormulario(model, producto, false);
            return "producto-form";
        }
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Optional<Producto> resultado = productoService.buscarPorId(id);
        if (resultado.isEmpty()) return "redirect:/productos";
        model.addAttribute("producto", resultado.get());
        return "producto-detalle";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Producto> resultado = productoService.buscarPorId(id);
        if (resultado.isEmpty()) return "redirect:/productos";
        prepararFormulario(model, resultado.get(), true);
        return "producto-form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Producto producto, Model model) {
        try {
            productoService.actualizar(id, producto);
            return "redirect:/productos";
        } catch (IllegalArgumentException ex) {
            producto.setId(id);
            model.addAttribute("error", ex.getMessage());
            prepararFormulario(model, producto, true);
            return "producto-form";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return "redirect:/productos";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam(required = false) String nombre, Model model) {
        model.addAttribute("productos", productoService.buscarPorNombre(nombre));
        model.addAttribute("busqueda", nombre);
        return "productos";
    }

    /** Endpoint JSON para la primera lista en cascada: establecimiento -> categorías. */
    @GetMapping("/categorias-por-establecimiento/{establecimientoId}")
    @ResponseBody
    public List<CategoriaOpcionDTO> categoriasPorEstablecimiento(@PathVariable Long establecimientoId) {
        Optional<Establecimiento> establecimiento = establecimientoService.buscarPorId(establecimientoId);
        if (establecimiento.isEmpty()) return List.of();
        return categoriaService.listarPorTipoEstablecimiento(establecimiento.get().getCategoria())
                .stream()
                .map(categoria -> new CategoriaOpcionDTO(categoria.getId(), categoria.getNombre()))
                .toList();
    }

    private void prepararFormulario(Model model, Producto producto, boolean modoEdicion) {
        model.addAttribute("producto", producto);
        model.addAttribute("modoEdicion", modoEdicion);
        model.addAttribute("establecimientos", establecimientoService.listar());
        model.addAttribute("categorias", categoriaService.listar());
    }
}
