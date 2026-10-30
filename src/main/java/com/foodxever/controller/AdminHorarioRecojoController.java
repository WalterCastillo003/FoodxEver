package com.foodxever.controller;

import com.foodxever.dto.HorarioRecojoForm;
import com.foodxever.model.HorarioRecojo;
import com.foodxever.service.EstablecimientoService;
import com.foodxever.service.HorarioRecojoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/administracion/horarios")
public class AdminHorarioRecojoController {
    private static final List<String> DIAS = List.of("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo");
    private final HorarioRecojoService horarioService;
    private final EstablecimientoService establecimientoService;

    public AdminHorarioRecojoController(HorarioRecojoService horarioService,
                                        EstablecimientoService establecimientoService) {
        this.horarioService = horarioService;
        this.establecimientoService = establecimientoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("horarios", horarioService.listar());
        return "admin/horarios";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        HorarioRecojoForm form = new HorarioRecojoForm();
        form.setActivo(true);
        preparar(model, form, false, null);
        return "admin/horario-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("horarioForm") HorarioRecojoForm form,
                          Model model, RedirectAttributes redirect) {
        try {
            horarioService.crear(form);
            redirect.addFlashAttribute("mensaje", "El horario se registró correctamente.");
            return "redirect:/administracion/horarios";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            preparar(model, form, false, null);
            return "admin/horario-form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        HorarioRecojo horario = horarioService.buscarPorId(id).orElse(null);
        if (horario == null) {
            redirect.addFlashAttribute("error", "No se encontró el horario.");
            return "redirect:/administracion/horarios";
        }
        preparar(model, horarioService.convertirAFormulario(horario), true, id);
        return "admin/horario-form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @ModelAttribute("horarioForm") HorarioRecojoForm form,
                             Model model, RedirectAttributes redirect) {
        try {
            horarioService.actualizar(id, form);
            redirect.addFlashAttribute("mensaje", "El horario se actualizó correctamente.");
            return "redirect:/administracion/horarios";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("horarioId", id);
            preparar(model, form, true, id);
            return "admin/horario-form";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            horarioService.eliminar(id);
            redirect.addFlashAttribute("mensaje", "El horario se eliminó correctamente.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/administracion/horarios";
    }

    private void preparar(Model model, HorarioRecojoForm form, boolean edicion, Long id) {
        model.addAttribute("horarioForm", form);
        model.addAttribute("establecimientos", establecimientoService.listar());
        model.addAttribute("diasSemana", DIAS);
        model.addAttribute("modoEdicion", edicion);
        model.addAttribute("formAction", edicion
                ? "/administracion/horarios/actualizar/" + id
                : "/administracion/horarios/guardar");
    }
}
