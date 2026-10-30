package com.foodxever.service;

import com.foodxever.model.Rol;
import com.foodxever.model.Usuario;
import com.foodxever.repository.RolRepository;
import com.foodxever.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario agregar(Usuario usuario) {
        String nombreRol = usuario.getRol();
        if (nombreRol == null || nombreRol.isBlank()) nombreRol = "CONSUMIDOR";
        final String rolBuscado = nombreRol.trim();
        Rol rol = rolRepository.findByNombreIgnoreCase(rolBuscado)
                .orElseGet(() -> rolRepository.save(new Rol(rolBuscado, "Rol de usuario FoodxEver")));
        usuario.setRolRef(rol);
        String contrasena = usuario.getContrasena();
        if (contrasena != null && !contrasena.isBlank()
                && !contrasena.startsWith("$2a$")
                && !contrasena.startsWith("$2b$")
                && !contrasena.startsWith("$2y$")) {
            usuario.setContrasena(passwordEncoder.encode(contrasena));
        }
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        if (correo == null || correo.isBlank()) return Optional.empty();
        return usuarioRepository.findByCorreoIgnoreCase(correo.trim());
    }

    public boolean existeCorreo(String correo) {
        return correo != null && usuarioRepository.existsByCorreoIgnoreCase(correo.trim());
    }

    public Optional<Usuario> autenticar(String correo, String contrasena) {
        if (correo == null || contrasena == null) return Optional.empty();
        return usuarioRepository.findByCorreoIgnoreCase(correo.trim())
                .filter(usuario -> passwordEncoder.matches(contrasena, usuario.getContrasena()));
    }
}
