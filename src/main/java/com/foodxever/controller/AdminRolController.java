package com.foodxever.controller;

import com.foodxever.model.Rol;
import com.foodxever.service.RolService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/administracion/roles")
public class AdminRolController {
    private final RolService rolService;

    public AdminRolController(RolService rolService) { this.rolService = rolService; }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("roles", rolService.listar());
        return "admin/roles";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("rol", new Rol());
        model.addAttribute("modoEdicion", false);
        model.addAttribute("formAction", "/administracion/roles/guardar");
        return "admin/rol-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Rol rol, RedirectAttributes redirect) {
        try {
            rolService.crear(rol);
            redirect.addFlashAttribute("mensaje", "El rol se creó correctamente.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/administracion/roles";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Rol rol = rolService.buscarPorId(id).orElse(null);
        if (rol == null) {
            redirect.addFlashAttribute("error", "No se encontró el rol.");
            return "redirect:/administracion/roles";
        }
        model.addAttribute("rol", rol);
        model.addAttribute("modoEdicion", true);
        model.addAttribute("formAction", "/administracion/roles/actualizar/" + id);
        return "admin/rol-form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Rol rol, RedirectAttributes redirect) {
        try {
            rolService.actualizar(id, rol);
            redirect.addFlashAttribute("mensaje", "El rol se actualizó correctamente.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/administracion/roles";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            rolService.eliminar(id);
            redirect.addFlashAttribute("mensaje", "El rol se eliminó correctamente.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/administracion/roles";
    }
}
