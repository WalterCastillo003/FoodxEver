package com.foodxever.controller;

import com.foodxever.service.ConsultaService;
import com.foodxever.service.EstablecimientoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consultas")
public class ConsultasController {
    private final ConsultaService consultaService;
    private final EstablecimientoService establecimientoService;

    public ConsultasController(ConsultaService consultaService,
                               EstablecimientoService establecimientoService) {
        this.consultaService = consultaService;
        this.establecimientoService = establecimientoService;
    }

    @GetMapping
    public String consultas(@RequestParam(required = false) Long establecimientoId, Model model) {
        model.addAttribute("establecimientos", establecimientoService.listar());
        model.addAttribute("establecimientoSeleccionado", establecimientoId);
        model.addAttribute("productosDTO", consultaService.productosPorEstablecimiento(establecimientoId));
        model.addAttribute("reservasDTO", consultaService.reservasPorEstablecimiento(establecimientoId));
        return "consultas";
    }
}
