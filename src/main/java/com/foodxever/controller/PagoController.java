package com.foodxever.controller;

import com.foodxever.model.Pago;
import com.foodxever.service.PagoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public String listar(Model model,
                         @RequestParam(required = false) String creado,
                         @RequestParam(required = false) String actualizado,
                         @RequestParam(required = false) String eliminado,
                         @RequestParam(required = false) String error) {
        model.addAttribute("pagos", pagoService.listar());
        model.addAttribute("creado", creado != null);
        model.addAttribute("actualizado", actualizado != null);
        model.addAttribute("eliminado", eliminado != null);
        model.addAttribute("error", error);
        return "pagos";
    }

    @GetMapping({"/nuevo", "/nueva"})
    public String nuevo(Model model) {
        prepararFormulario(model, new Pago(), false, null, null);
        return "pago-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Pago pago,
                          @RequestParam Long reservaId,
                          @RequestParam Long metodoPagoId,
                          Model model) {
        try {
            pagoService.crear(reservaId, metodoPagoId, pago.getEstado(), pago.getReferencia());
            return "redirect:/pagos?creado";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            prepararFormulario(model, pago, false, reservaId, metodoPagoId);
            return "pago-form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Pago> resultado = pagoService.buscarPorId(id);
        if (resultado.isEmpty()) return "redirect:/pagos?error=No+se+encontró+el+pago";
        Pago pago = resultado.get();
        prepararFormulario(model, pago, true, pago.getReserva().getId(), pago.getMetodoPago().getId());
        return "pago-form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @ModelAttribute Pago pago,
                             @RequestParam Long reservaId,
                             @RequestParam Long metodoPagoId,
                             Model model) {
        try {
            pagoService.actualizar(id, reservaId, metodoPagoId, pago.getEstado(), pago.getReferencia());
            return "redirect:/pagos?actualizado";
        } catch (IllegalArgumentException ex) {
            pago.setId(id);
            model.addAttribute("error", ex.getMessage());
            prepararFormulario(model, pago, true, reservaId, metodoPagoId);
            return "pago-form";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        try {
            pagoService.eliminar(id);
            return "redirect:/pagos?eliminado";
        } catch (IllegalArgumentException ex) {
            return "redirect:/pagos?error=" + java.net.URLEncoder.encode(ex.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }

    private void prepararFormulario(Model model, Pago pago, boolean modoEdicion,
                                    Long reservaSeleccionadaId, Long metodoPagoSeleccionadoId) {
        model.addAttribute("pago", pago);
        model.addAttribute("formAction", modoEdicion ? "/pagos/actualizar/" + pago.getId() : "/pagos/guardar");
        model.addAttribute("modoEdicion", modoEdicion);
        model.addAttribute("reservas", pagoService.listarReservasDisponibles(modoEdicion ? pago.getId() : null));
        model.addAttribute("metodosPago", pagoService.listarMetodosPago());
        model.addAttribute("reservaSeleccionadaId", reservaSeleccionadaId);
        model.addAttribute("metodoPagoSeleccionadoId", metodoPagoSeleccionadoId);
    }
}
