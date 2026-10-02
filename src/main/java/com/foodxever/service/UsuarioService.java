package com.foodxever.service;

import com.foodxever.model.Usuario;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UsuarioService {

    private final List<Usuario> usuarios =
            new ArrayList<>();

    private final AtomicLong contadorId =
            new AtomicLong(1);


    public UsuarioService() {

        // Usuario de demostración

        agregar(
                new Usuario(
                        null,
                        "Usuario Demo",
                        "demo@foodxever.com",
                        "123456",
                        "CONSUMIDOR",
                        null
                )
        );
    }


    // ==========================================
    // LISTAR
    // ==========================================

    public List<Usuario> listar() {

        return new ArrayList<>(usuarios);
    }


    // ==========================================
    // AGREGAR
    // ==========================================

    public Usuario agregar(
            Usuario usuario) {

        usuario.setId(
                contadorId.getAndIncrement()
        );

        usuarios.add(usuario);

        return usuario;
    }


    // ==========================================
    // BUSCAR POR ID
    // ==========================================

    public Optional<Usuario> buscarPorId(
            Long id) {

        return usuarios
                .stream()
                .filter(
                        usuario ->
                                usuario.getId()
                                        .equals(id)
                )
                .findFirst();
    }


    // ==========================================
    // BUSCAR POR CORREO
    // ==========================================

    public Optional<Usuario> buscarPorCorreo(
            String correo) {

        if (correo == null) {
            return Optional.empty();
        }

        return usuarios
                .stream()
                .filter(
                        usuario ->
                                usuario.getCorreo()
                                        .equalsIgnoreCase(
                                                correo.trim()
                                        )
                )
                .findFirst();
    }


    // ==========================================
    // VERIFICAR CORREO
    // ==========================================

    public boolean existeCorreo(
            String correo) {

        return buscarPorCorreo(correo)
                .isPresent();
    }


    // ==========================================
    // LOGIN
    // ==========================================

    public Optional<Usuario> autenticar(
            String correo,
            String contrasena) {

        return usuarios
                .stream()
                .filter(
                        usuario ->
                                usuario.getCorreo()
                                        .equalsIgnoreCase(
                                                correo.trim()
                                        )
                                &&
                                usuario.getContrasena()
                                        .equals(contrasena)
                )
                .findFirst();
    }
}