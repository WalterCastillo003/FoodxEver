package com.foodxever.controller;

import com.foodxever.model.MetodoPago;
import com.foodxever.service.MetodoPagoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/administracion/metodos-pago")
public class AdminMetodoPagoController {
    private final MetodoPagoService metodoPagoService;

    public AdminMetodoPagoController(MetodoPagoService metodoPagoService) { this.metodoPagoService = metodoPagoService; }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("metodosPago", metodoPagoService.listar());
        return "admin/metodos-pago";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        MetodoPago metodo = new MetodoPago();
        metodo.setActivo(true);
        preparar(model, metodo, false);
        return "admin/metodo-pago-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute MetodoPago metodo, Model model, RedirectAttributes redirect) {
        try {
            metodoPagoService.crear(metodo);
            redirect.addFlashAttribute("mensaje", "El método de pago se creó correctamente.");
            return "redirect:/administracion/metodos-pago";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            preparar(model, metodo, false);
            return "admin/metodo-pago-form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        MetodoPago metodo = metodoPagoService.buscarPorId(id).orElse(null);
        if (metodo == null) {
            redirect.addFlashAttribute("error", "No se encontró el método de pago.");
            return "redirect:/administracion/metodos-pago";
        }
        preparar(model, metodo, true);
        return "admin/metodo-pago-form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute MetodoPago metodo,
                             Model model, RedirectAttributes redirect) {
        try {
            metodoPagoService.actualizar(id, metodo);
            redirect.addFlashAttribute("mensaje", "El método de pago se actualizó correctamente.");
            return "redirect:/administracion/metodos-pago";
        } catch (IllegalArgumentException ex) {
            metodo.setId(id);
            model.addAttribute("error", ex.getMessage());
            preparar(model, metodo, true);
            return "admin/metodo-pago-form";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            metodoPagoService.eliminar(id);
            redirect.addFlashAttribute("mensaje", "El método de pago se eliminó correctamente.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/administracion/metodos-pago";
    }

    private void preparar(Model model, MetodoPago metodo, boolean edicion) {
        model.addAttribute("metodoPago", metodo);
        model.addAttribute("modoEdicion", edicion);
        model.addAttribute("formAction", edicion
                ? "/administracion/metodos-pago/actualizar/" + metodo.getId()
                : "/administracion/metodos-pago/guardar");
    }
}
