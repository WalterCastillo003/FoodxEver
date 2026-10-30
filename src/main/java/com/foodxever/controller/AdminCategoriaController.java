package com.foodxever.controller;

import com.foodxever.model.Categoria;
import com.foodxever.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/administracion/categorias")
public class AdminCategoriaController {
    private static final List<String> TIPOS = List.of("Panadería", "Cafetería", "Restaurante", "Market");
    private final CategoriaService categoriaService;

    public AdminCategoriaController(CategoriaService categoriaService) { this.categoriaService = categoriaService; }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "admin/categorias";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Categoria categoria = new Categoria();
        categoria.setActiva(true);
        preparar(model, categoria, false);
        return "admin/categoria-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria, Model model, RedirectAttributes redirect) {
        try {
            categoriaService.crear(categoria);
            redirect.addFlashAttribute("mensaje", "La categoría se creó correctamente.");
            return "redirect:/administracion/categorias";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            preparar(model, categoria, false);
            return "admin/categoria-form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Categoria categoria = categoriaService.buscarPorId(id).orElse(null);
        if (categoria == null) {
            redirect.addFlashAttribute("error", "No se encontró la categoría.");
            return "redirect:/administracion/categorias";
        }
        preparar(model, categoria, true);
        return "admin/categoria-form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Categoria categoria,
                             Model model, RedirectAttributes redirect) {
        try {
            categoriaService.actualizar(id, categoria);
            redirect.addFlashAttribute("mensaje", "La categoría se actualizó correctamente.");
            return "redirect:/administracion/categorias";
        } catch (IllegalArgumentException ex) {
            categoria.setId(id);
            model.addAttribute("error", ex.getMessage());
            preparar(model, categoria, true);
            return "admin/categoria-form";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            categoriaService.eliminar(id);
            redirect.addFlashAttribute("mensaje", "La categoría se eliminó correctamente.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/administracion/categorias";
    }

    private void preparar(Model model, Categoria categoria, boolean edicion) {
        model.addAttribute("categoria", categoria);
        model.addAttribute("tiposEstablecimiento", TIPOS);
        model.addAttribute("modoEdicion", edicion);
        model.addAttribute("formAction", edicion
                ? "/administracion/categorias/actualizar/" + categoria.getId()
                : "/administracion/categorias/guardar");
    }
}
