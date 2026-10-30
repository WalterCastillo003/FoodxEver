package com.foodxever.controller;

import com.foodxever.dto.ProductoOpcionDTO;
import com.foodxever.model.Establecimiento;
import com.foodxever.model.Producto;
import com.foodxever.model.Reserva;
import com.foodxever.model.Usuario;
import com.foodxever.service.EstablecimientoService;
import com.foodxever.service.ProductoService;
import com.foodxever.service.ReservaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final ProductoService productoService;
    private final EstablecimientoService establecimientoService;

    public ReservaController(ReservaService reservaService,
                             ProductoService productoService,
                             EstablecimientoService establecimientoService) {
        this.reservaService = reservaService;
        this.productoService = productoService;
        this.establecimientoService = establecimientoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("reservas", reservaService.listar());
        return "reservas";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        Reserva reserva = new Reserva();
        reserva.setCantidad(1);
        reserva.setEstado("Pendiente");
        List<Establecimiento> establecimientos = establecimientoService.listar();
        List<Producto> productos = productoService.listar();
        if (!establecimientos.isEmpty()) {
            Long primeraId = establecimientos.get(0).getId();
            reserva.setEstablecimientoId(primeraId);
            productos = productoService.listarPorEstablecimiento(primeraId);
        }
        prepararFormulario(model, reserva, productos, false);
        return "reserva-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Reserva reserva, HttpSession session) {
        Optional<Producto> resultado = productoService.buscarPorId(reserva.getProductoId());
        if (resultado.isEmpty()) return "redirect:/reservas/nueva?error=producto";
        Producto producto = resultado.get();
        if (!producto.isActivo() || reserva.getCantidad() <= 0 || reserva.getCantidad() > producto.getStock()) {
            return "redirect:/reservas/nueva?error=stock";
        }
        if (reserva.getEstablecimientoId() != null
                && !reserva.getEstablecimientoId().equals(producto.getEstablecimientoId())) {
            return "redirect:/reservas/nueva?error=producto";
        }

        reserva.vincularProducto(producto);
        reserva.setProductoNombre(producto.getNombre());
        reserva.setTotal(producto.getPrecio() * reserva.getCantidad());
        reserva.setEstado("Pendiente");
        Object usuarioActual = session.getAttribute("usuarioActual");
        if (usuarioActual instanceof Usuario usuario) {
            reserva.setUsuarioRef(usuario);
            if (reserva.getCliente() == null || reserva.getCliente().isBlank()) {
                reserva.setCliente(usuario.getNombre());
            }
        }
        reservaService.agregar(reserva);
        return "redirect:/reservas";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Optional<Reserva> resultado = reservaService.buscarPorId(id);
        if (resultado.isEmpty()) return "redirect:/reservas";
        model.addAttribute("reserva", resultado.get());
        return "reserva-detalle";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Reserva> resultado = reservaService.buscarPorId(id);
        if (resultado.isEmpty()) return "redirect:/reservas";
        Reserva reserva = resultado.get();
        Long establecimientoId = reserva.getEstablecimientoId();
        List<Producto> productos = new ArrayList<>(establecimientoId == null
                ? productoService.listar() : productoService.listarPorEstablecimiento(establecimientoId));
        // Incluye el producto ya reservado aunque haya quedado sin stock o inactivo,
        // para que el usuario pueda ver y editar la reserva histórica.
        if (reserva.getProductoId() != null && productos.stream().noneMatch(p -> p.getId().equals(reserva.getProductoId()))) {
            productoService.buscarPorId(reserva.getProductoId()).ifPresent(productos::add);
        }
        prepararFormulario(model, reserva, productos, true);
        return "reserva-form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Reserva reserva) {
        Optional<Producto> resultadoProducto = productoService.buscarPorId(reserva.getProductoId());
        if (resultadoProducto.isEmpty()) return "redirect:/reservas";
        Producto producto = resultadoProducto.get();
        if (reserva.getCantidad() <= 0 || reserva.getCantidad() > producto.getStock()) {
            return "redirect:/reservas/editar/" + id + "?error=stock";
        }
        if (reserva.getEstablecimientoId() != null
                && !reserva.getEstablecimientoId().equals(producto.getEstablecimientoId())) {
            return "redirect:/reservas/editar/" + id + "?error=producto";
        }
        reserva.vincularProducto(producto);
        reserva.setProductoNombre(producto.getNombre());
        reserva.setTotal(producto.getPrecio() * reserva.getCantidad());
        reservaService.actualizar(id, reserva);
        return "redirect:/reservas";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
        return "redirect:/reservas";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam(required = false) String cliente, Model model) {
        model.addAttribute("reservas", reservaService.buscarPorCliente(cliente));
        model.addAttribute("busqueda", cliente);
        return "reservas";
    }

    @PostMapping("/completar/{id}")
    public String completar(@PathVariable Long id) {
        reservaService.cambiarEstado(id, "Completada");
        return "redirect:/reservas";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id) {
        reservaService.cambiarEstado(id, "Cancelada");
        return "redirect:/reservas";
    }

    /** Endpoint JSON para la segunda lista en cascada: establecimiento -> productos disponibles. */
    @GetMapping("/productos-por-establecimiento/{establecimientoId}")
    @ResponseBody
    public List<ProductoOpcionDTO> productosPorEstablecimiento(@PathVariable Long establecimientoId) {
        return productoService.listarPorEstablecimiento(establecimientoId).stream()
                .map(producto -> new ProductoOpcionDTO(producto.getId(), producto.getNombre(),
                        producto.getPrecio(), producto.getStock()))
                .toList();
    }

    private void prepararFormulario(Model model, Reserva reserva, List<Producto> productos, boolean modoEdicion) {
        model.addAttribute("reserva", reserva);
        model.addAttribute("productos", productos);
        model.addAttribute("establecimientos", establecimientoService.listar());
        model.addAttribute("modoEdicion", modoEdicion);
    }
}
