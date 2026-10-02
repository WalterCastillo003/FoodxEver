package com.foodxever.controller;

import com.foodxever.model.Usuario;
import com.foodxever.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;


    public UsuarioController(
            UsuarioService usuarioService) {

        this.usuarioService =
                usuarioService;
    }


    // ==========================================
    // FORMULARIO LOGIN
    // ==========================================

    @GetMapping("/login")
    public String login(
            HttpSession session) {

        if (
                session.getAttribute(
                        "usuarioActual"
                ) != null
        ) {

            return "redirect:/";
        }

        return "login";
    }


    // ==========================================
    // PROCESAR LOGIN
    // ==========================================

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String correo,
            @RequestParam String contrasena,
            HttpSession session,
            Model model) {

        Optional<Usuario> resultado =
                usuarioService.autenticar(
                        correo,
                        contrasena
                );


        if (resultado.isEmpty()) {

            model.addAttribute(
                    "error",
                    "Correo o contraseña incorrectos."
            );

            model.addAttribute(
                    "correo",
                    correo
            );

            return "login";
        }


        Usuario usuario =
                resultado.get();


        session.setAttribute(
                "usuarioActual",
                usuario
        );


        return "redirect:/";
    }


    // ==========================================
    // FORMULARIO REGISTRO
    // ==========================================

    @GetMapping("/registro")
    public String registro(
            Model model,
            HttpSession session) {

        if (
                session.getAttribute(
                        "usuarioActual"
                ) != null
        ) {

            return "redirect:/";
        }


        model.addAttribute(
                "usuario",
                new Usuario()
        );


        return "registro";
    }


    // ==========================================
    // PROCESAR REGISTRO
    // ==========================================

    @PostMapping("/registro")
    public String procesarRegistro(
            @ModelAttribute Usuario usuario,
            Model model,
            HttpSession session) {


        // --------------------------------------
        // VALIDACIONES
        // --------------------------------------

        if (
                usuario.getNombre() == null ||
                usuario.getNombre().isBlank() ||
                usuario.getCorreo() == null ||
                usuario.getCorreo().isBlank() ||
                usuario.getContrasena() == null ||
                usuario.getContrasena().length() < 6 ||
                usuario.getRol() == null ||
                usuario.getRol().isBlank()
        ) {

            model.addAttribute(
                    "error",
                    "Completa correctamente todos los campos."
            );

            return "registro";
        }


        if (
                usuarioService.existeCorreo(
                        usuario.getCorreo()
                )
        ) {

            model.addAttribute(
                    "error",
                    "Ya existe una cuenta con ese correo."
            );

            return "registro";
        }


        // Si no es establecimiento,
        // no necesita nombre comercial.

        if (
                !"ESTABLECIMIENTO"
                        .equalsIgnoreCase(
                                usuario.getRol()
                        )
        ) {

            usuario.setNombreEstablecimiento(
                    null
            );
        }


        usuarioService.agregar(
                usuario
        );


        // Iniciar sesión automáticamente

        session.setAttribute(
                "usuarioActual",
                usuario
        );


        return "redirect:/";
    }


    // ==========================================
    // PERFIL
    // ==========================================

    @GetMapping("/perfil")
    public String perfil(
            HttpSession session,
            Model model) {

        Usuario usuario =
                (Usuario)
                        session.getAttribute(
                                "usuarioActual"
                        );


        if (usuario == null) {

            return "redirect:/login";
        }


        model.addAttribute(
                "usuario",
                usuario
        );


        return "perfil";
    }


    // ==========================================
    // CERRAR SESIÓN
    // ==========================================

    @PostMapping("/logout")
    public String logout(
            HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
}