package com.foodxever.service;

import com.foodxever.dto.UsuarioAdminForm;
import com.foodxever.model.Rol;
import com.foodxever.model.Usuario;
import com.foodxever.repository.EstablecimientoRepository;
import com.foodxever.repository.ResenaRepository;
import com.foodxever.repository.ReservaRepository;
import com.foodxever.repository.RolRepository;
import com.foodxever.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReservaRepository reservaRepository;
    private final ResenaRepository resenaRepository;
    private final EstablecimientoRepository establecimientoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository,
                          PasswordEncoder passwordEncoder, ReservaRepository reservaRepository,
                          ResenaRepository resenaRepository, EstablecimientoRepository establecimientoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.reservaRepository = reservaRepository;
        this.resenaRepository = resenaRepository;
        this.establecimientoRepository = establecimientoRepository;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAllByOrderByNombreAsc();
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

    @Transactional
    public Usuario crearDesdeAdministracion(UsuarioAdminForm form) {
        validarDatosAdministrativos(form, null, true);
        Usuario usuario = new Usuario();
        aplicarDatosAdministrativos(usuario, form, true);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizarDesdeAdministracion(Long id, UsuarioAdminForm form) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario."));
        validarDatosAdministrativos(form, id, false);
        aplicarDatosAdministrativos(usuario, form, false);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminarDesdeAdministracion(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario."));
        if (reservaRepository.existsByUsuarioRef_Id(id)
                || resenaRepository.existsByUsuario_Id(id)
                || establecimientoRepository.existsByPropietarioRef_Id(id)) {
            throw new IllegalArgumentException("No se puede eliminar este usuario porque tiene reservas, reseñas o establecimientos asociados.");
        }
        if ("ADMIN".equalsIgnoreCase(usuario.getRol())
                && usuarioRepository.countByRolRef_NombreIgnoreCase("ADMIN") <= 1) {
            throw new IllegalArgumentException("No puedes eliminar el último administrador de FoodxEver.");
        }
        usuarioRepository.delete(usuario);
    }

    private void validarDatosAdministrativos(UsuarioAdminForm form, Long idActual, boolean esNuevo) {
        if (form.getNombre() == null || form.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio.");
        }
        if (form.getNombre().trim().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede superar 100 caracteres.");
        }
        if (form.getCorreo() == null || !form.getCorreo().trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Introduce un correo electrónico válido.");
        }
        if (form.getCorreo().trim().length() > 160) {
            throw new IllegalArgumentException("El correo no puede superar 160 caracteres.");
        }
        if (form.getNombreEstablecimiento() != null && form.getNombreEstablecimiento().trim().length() > 120) {
            throw new IllegalArgumentException("El nombre del establecimiento no puede superar 120 caracteres.");
        }
        String correo = form.getCorreo().trim().toLowerCase(Locale.ROOT);
        boolean correoDuplicado = idActual == null
                ? usuarioRepository.existsByCorreoIgnoreCase(correo)
                : usuarioRepository.existsByCorreoIgnoreCaseAndIdNot(correo, idActual);
        if (correoDuplicado) throw new IllegalArgumentException("Ya existe un usuario con ese correo.");
        if (form.getRolId() == null || !rolRepository.existsById(form.getRolId())) {
            throw new IllegalArgumentException("Selecciona un rol válido.");
        }
        String clave = form.getContrasena();
        if (esNuevo && (clave == null || clave.length() < 6)) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
        if (!esNuevo && clave != null && !clave.isBlank() && clave.length() < 6) {
            throw new IllegalArgumentException("La nueva contraseña debe tener al menos 6 caracteres.");
        }
    }

    private void aplicarDatosAdministrativos(Usuario usuario, UsuarioAdminForm form, boolean esNuevo) {
        Rol rol = rolRepository.findById(form.getRolId())
                .orElseThrow(() -> new IllegalArgumentException("Selecciona un rol válido."));
        usuario.setNombre(form.getNombre().trim());
        usuario.setCorreo(form.getCorreo().trim().toLowerCase(Locale.ROOT));
        usuario.setRolRef(rol);
        usuario.setNombreEstablecimiento("ESTABLECIMIENTO".equalsIgnoreCase(rol.getNombre())
                ? limpiar(form.getNombreEstablecimiento()) : null);
        if (esNuevo || (form.getContrasena() != null && !form.getContrasena().isBlank())) {
            usuario.setContrasena(passwordEncoder.encode(form.getContrasena()));
        }
    }

    private String limpiar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
