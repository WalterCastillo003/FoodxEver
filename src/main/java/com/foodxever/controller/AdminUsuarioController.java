package com.foodxever.controller;

import com.foodxever.dto.UsuarioAdminForm;
import com.foodxever.model.Rol;
import com.foodxever.model.Usuario;
import com.foodxever.service.RolService;
import com.foodxever.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/administracion/usuarios")
public class AdminUsuarioController {
    private final UsuarioService usuarioService;
    private final RolService rolService;

    public AdminUsuarioController(UsuarioService usuarioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listar());
        return "admin/usuarios";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        prepararFormulario(model, new UsuarioAdminForm(), false, null);
        return "admin/usuario-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("usuarioForm") UsuarioAdminForm form, Model model,
                          RedirectAttributes redirect) {
        try {
            usuarioService.crearDesdeAdministracion(form);
            redirect.addFlashAttribute("mensaje", "El usuario se registró correctamente.");
            return "redirect:/administracion/usuarios";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            prepararFormulario(model, form, false, null);
            return "admin/usuario-form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Optional<Usuario> resultado = usuarioService.buscarPorId(id);
        if (resultado.isEmpty()) {
            redirect.addFlashAttribute("error", "No se encontró el usuario.");
            return "redirect:/administracion/usuarios";
        }
        Usuario usuario = resultado.get();
        UsuarioAdminForm form = new UsuarioAdminForm();
        form.setNombre(usuario.getNombre());
        form.setCorreo(usuario.getCorreo());
        form.setRolId(usuario.getRolRef().getId());
        form.setNombreEstablecimiento(usuario.getNombreEstablecimiento());
        form.setContrasena(""); // Nunca enviar el hash de la contraseña al navegador.
        prepararFormulario(model, form, true, id);
        return "admin/usuario-form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @ModelAttribute("usuarioForm") UsuarioAdminForm form,
                             Model model, HttpSession session, RedirectAttributes redirect) {
        try {
            Object sesion = session.getAttribute("usuarioActual");
            if (sesion instanceof Usuario actual && actual.getId().equals(id)) {
                Rol nuevoRol = form.getRolId() == null ? null : rolService.buscarPorId(form.getRolId()).orElse(null);
                if (nuevoRol == null || !"ADMIN".equalsIgnoreCase(nuevoRol.getNombre())) {
                    throw new IllegalArgumentException("No puedes quitarte el rol ADMIN mientras tienes la sesión administrativa activa.");
                }
            }
            usuarioService.actualizarDesdeAdministracion(id, form);
            Object usuarioSesion = session.getAttribute("usuarioActual");
            if (usuarioSesion instanceof Usuario actual && actual.getId().equals(id)) {
                session.setAttribute("usuarioActual", usuarioService.buscarPorId(id).orElse(actual));
            }
            redirect.addFlashAttribute("mensaje", "Los datos del usuario se actualizaron correctamente.");
            return "redirect:/administracion/usuarios";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            prepararFormulario(model, form, true, id);
            return "admin/usuario-form";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session, RedirectAttributes redirect) {
        Object sesion = session.getAttribute("usuarioActual");
        if (sesion instanceof Usuario actual && actual.getId().equals(id)) {
            redirect.addFlashAttribute("error", "No puedes eliminar la cuenta con la que has iniciado sesión.");
            return "redirect:/administracion/usuarios";
        }
        try {
            usuarioService.eliminarDesdeAdministracion(id);
            redirect.addFlashAttribute("mensaje", "El usuario se eliminó correctamente.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/administracion/usuarios";
    }

    private void prepararFormulario(Model model, UsuarioAdminForm form, boolean edicion, Long id) {
        model.addAttribute("usuarioForm", form);
        model.addAttribute("roles", rolService.listar());
        model.addAttribute("modoEdicion", edicion);
        model.addAttribute("passwordRequired", !edicion);
        model.addAttribute("formAction", edicion
                ? "/administracion/usuarios/actualizar/" + id
                : "/administracion/usuarios/guardar");
    }
}
