package com.foodxever.controller;

import com.foodxever.repository.HistorialReservaRepository;
import com.foodxever.service.ReservaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/administracion/historial")
public class AdminHistorialController {
    private final HistorialReservaRepository historialRepository;
    private final ReservaService reservaService;

    public AdminHistorialController(HistorialReservaRepository historialRepository, ReservaService reservaService) {
        this.historialRepository = historialRepository;
        this.reservaService = reservaService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) Long reservaId, Model model) {
        model.addAttribute("historiales", reservaId == null
                ? historialRepository.findAllByOrderByFechaCambioDesc()
                : historialRepository.findByReserva_IdOrderByFechaCambioDesc(reservaId));
        model.addAttribute("reservas", reservaService.listar());
        model.addAttribute("reservaSeleccionada", reservaId);
        return "admin/historial";
    }
}
