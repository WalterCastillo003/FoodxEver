package com.foodxever.controller;

import com.foodxever.model.Resena;
import com.foodxever.service.ResenaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Controller
@RequestMapping("/resenas")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @GetMapping
    public String listar(Model model,
                         @RequestParam(required = false) String creada,
                         @RequestParam(required = false) String actualizada,
                         @RequestParam(required = false) String eliminada,
                         @RequestParam(required = false) String error) {
        model.addAttribute("resenas", resenaService.listar());
        model.addAttribute("creada", creada != null);
        model.addAttribute("actualizada", actualizada != null);
        model.addAttribute("eliminada", eliminada != null);
        model.addAttribute("error", error);
        return "resenas";
    }

    @GetMapping({"/nuevo", "/nueva"})
    public String nueva(Model model) {
        Resena resena = new Resena();
        resena.setCalificacion(5);
        prepararFormulario(model, resena, false, null, null, null);
        return "resena-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Resena resena,
                          @RequestParam Long usuarioId,
                          @RequestParam Long establecimientoId,
                          @RequestParam(required = false) Long reservaId,
                          Model model) {
        try {
            resenaService.crear(usuarioId, establecimientoId, reservaId,
                    resena.getCalificacion(), resena.getComentario());
            return "redirect:/resenas?creada";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            prepararFormulario(model, resena, false, usuarioId, establecimientoId, reservaId);
            return "resena-form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Resena> resultado = resenaService.buscarPorId(id);
        if (resultado.isEmpty()) return "redirect:/resenas?error=No+se+encontró+la+reseña";
        Resena resena = resultado.get();
        prepararFormulario(model, resena, true, resena.getUsuario().getId(),
                resena.getEstablecimiento().getId(),
                resena.getReserva() == null ? null : resena.getReserva().getId());
        return "resena-form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @ModelAttribute Resena resena,
                             @RequestParam Long usuarioId,
                             @RequestParam Long establecimientoId,
                             @RequestParam(required = false) Long reservaId,
                             Model model) {
        try {
            resenaService.actualizar(id, usuarioId, establecimientoId, reservaId,
                    resena.getCalificacion(), resena.getComentario());
            return "redirect:/resenas?actualizada";
        } catch (IllegalArgumentException ex) {
            resena.setId(id);
            model.addAttribute("error", ex.getMessage());
            prepararFormulario(model, resena, true, usuarioId, establecimientoId, reservaId);
            return "resena-form";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        try {
            resenaService.eliminar(id);
            return "redirect:/resenas?eliminada";
        } catch (IllegalArgumentException ex) {
            return "redirect:/resenas?error=" + URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
        }
    }

    private void prepararFormulario(Model model, Resena resena, boolean modoEdicion,
                                    Long usuarioSeleccionadoId, Long establecimientoSeleccionadoId,
                                    Long reservaSeleccionadaId) {
        model.addAttribute("resena", resena);
        model.addAttribute("formAction", modoEdicion ? "/resenas/actualizar/" + resena.getId() : "/resenas/guardar");
        model.addAttribute("modoEdicion", modoEdicion);
        model.addAttribute("usuarios", resenaService.listarUsuarios());
        model.addAttribute("establecimientos", resenaService.listarEstablecimientos());
        model.addAttribute("reservas", resenaService.listarReservasDisponibles(modoEdicion ? resena.getId() : null));
        model.addAttribute("usuarioSeleccionadoId", usuarioSeleccionadoId);
        model.addAttribute("establecimientoSeleccionadoId", establecimientoSeleccionadoId);
        model.addAttribute("reservaSeleccionadaId", reservaSeleccionadaId);
    }
}
