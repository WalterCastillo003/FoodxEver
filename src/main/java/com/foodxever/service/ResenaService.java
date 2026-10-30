package com.foodxever.service;

import com.foodxever.model.Establecimiento;
import com.foodxever.model.Resena;
import com.foodxever.model.Reserva;
import com.foodxever.model.Usuario;
import com.foodxever.repository.EstablecimientoRepository;
import com.foodxever.repository.ResenaRepository;
import com.foodxever.repository.ReservaRepository;
import com.foodxever.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstablecimientoRepository establecimientoRepository;
    private final ReservaRepository reservaRepository;

    public ResenaService(ResenaRepository resenaRepository,
                         UsuarioRepository usuarioRepository,
                         EstablecimientoRepository establecimientoRepository,
                         ReservaRepository reservaRepository) {
        this.resenaRepository = resenaRepository;
        this.usuarioRepository = usuarioRepository;
        this.establecimientoRepository = establecimientoRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<Resena> listar() {
        return resenaRepository.findAllByOrderByFechaCreacionDesc();
    }

    public Optional<Resena> buscarPorId(Long id) {
        return resenaRepository.findById(id);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAllByOrderByNombreAsc();
    }

    public List<Establecimiento> listarEstablecimientos() {
        return establecimientoRepository.findByActivoTrueOrderByIdAsc();
    }

    public List<Reserva> listarReservasDisponibles(Long resenaId) {
        Long reservaActualId = resenaId == null ? null : resenaRepository.findById(resenaId)
                .map(r -> r.getReserva() == null ? null : r.getReserva().getId()).orElse(null);
        return reservaRepository.findAllByOrderByIdDesc().stream()
                .filter(r -> !resenaRepository.existsByReserva_Id(r.getId())
                        || Objects.equals(r.getId(), reservaActualId))
                .toList();
    }

    @Transactional
    public Resena crear(Long usuarioId, Long establecimientoId, Long reservaId,
                        int calificacion, String comentario) {
        validarCalificacion(calificacion);
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Selecciona un usuario válido."));
        Establecimiento establecimiento = establecimientoRepository.findById(establecimientoId)
                .filter(Establecimiento::isActivo)
                .orElseThrow(() -> new IllegalArgumentException("Selecciona un establecimiento activo."));
        Reserva reserva = obtenerReservaYValidarVinculo(reservaId, establecimientoId, null);

        Resena resena = new Resena();
        resena.setUsuario(usuario);
        resena.setEstablecimiento(establecimiento);
        resena.setReserva(reserva);
        resena.setCalificacion(calificacion);
        resena.setComentario(limpiarComentario(comentario));
        return resenaRepository.save(resena);
    }

    @Transactional
    public Resena actualizar(Long id, Long usuarioId, Long establecimientoId, Long reservaId,
                             int calificacion, String comentario) {
        validarCalificacion(calificacion);
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la reseña que intentas editar."));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Selecciona un usuario válido."));
        Establecimiento establecimiento = establecimientoRepository.findById(establecimientoId)
                .orElseThrow(() -> new IllegalArgumentException("Selecciona un establecimiento válido."));
        Reserva reserva = obtenerReservaYValidarVinculo(reservaId, establecimientoId, id);

        resena.setUsuario(usuario);
        resena.setEstablecimiento(establecimiento);
        resena.setReserva(reserva);
        resena.setCalificacion(calificacion);
        resena.setComentario(limpiarComentario(comentario));
        return resenaRepository.save(resena);
    }

    @Transactional
    public void eliminar(Long id) {
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la reseña que intentas eliminar."));
        resenaRepository.delete(resena);
    }

    private Reserva obtenerReservaYValidarVinculo(Long reservaId, Long establecimientoId, Long resenaActualId) {
        if (reservaId == null) return null;
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Selecciona una reserva válida."));

        if (resenaRepository.existsByReserva_Id(reservaId)) {
            Long reservaActualId = resenaActualId == null ? null : resenaRepository.findById(resenaActualId)
                    .map(r -> r.getReserva() == null ? null : r.getReserva().getId()).orElse(null);
            if (!Objects.equals(reservaActualId, reservaId)) {
                throw new IllegalArgumentException("La reserva seleccionada ya tiene una reseña asociada.");
            }
        }

        Long establecimientoDeReserva = reserva.getProductoRef() == null
                ? null : reserva.getProductoRef().getEstablecimientoId();
        if (establecimientoDeReserva != null && !Objects.equals(establecimientoDeReserva, establecimientoId)) {
            throw new IllegalArgumentException("La reserva no pertenece al establecimiento seleccionado.");
        }
        return reserva;
    }

    private void validarCalificacion(int calificacion) {
        if (calificacion < 1 || calificacion > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5 estrellas.");
        }
    }

    private String limpiarComentario(String comentario) {
        if (comentario == null || comentario.isBlank()) return null;
        String limpio = comentario.trim();
        if (limpio.length() > 800) throw new IllegalArgumentException("El comentario no puede superar 800 caracteres.");
        return limpio;
    }
}
