package com.foodxever.service;

import com.foodxever.dto.HorarioRecojoForm;
import com.foodxever.model.Establecimiento;
import com.foodxever.model.HorarioRecojo;
import com.foodxever.repository.EstablecimientoRepository;
import com.foodxever.repository.HorarioRecojoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class HorarioRecojoService {
    private static final List<String> DIAS = List.of(
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo");

    private final HorarioRecojoRepository horarioRepository;
    private final EstablecimientoRepository establecimientoRepository;

    public HorarioRecojoService(HorarioRecojoRepository horarioRepository,
                                EstablecimientoRepository establecimientoRepository) {
        this.horarioRepository = horarioRepository;
        this.establecimientoRepository = establecimientoRepository;
    }

    public List<HorarioRecojo> listar() {
        return horarioRepository.findAllByOrderByEstablecimiento_NombreAscDiaSemanaAscHoraInicioAsc();
    }

    public Optional<HorarioRecojo> buscarPorId(Long id) {
        return horarioRepository.findById(id);
    }

    @Transactional
    public HorarioRecojo crear(HorarioRecojoForm form) {
        validar(form, null);
        HorarioRecojo horario = new HorarioRecojo();
        aplicarDatos(horario, form);
        return horarioRepository.save(horario);
    }

    @Transactional
    public HorarioRecojo actualizar(Long id, HorarioRecojoForm form) {
        HorarioRecojo horario = horarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el horario."));
        validar(form, id);
        aplicarDatos(horario, form);
        return horarioRepository.save(horario);
    }

    @Transactional
    public void eliminar(Long id) {
        HorarioRecojo horario = horarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el horario."));
        horarioRepository.delete(horario);
    }

    private void validar(HorarioRecojoForm form, Long idActual) {
        if (form.getEstablecimientoId() == null) throw new IllegalArgumentException("Selecciona un establecimiento.");
        Establecimiento establecimiento = establecimientoRepository.findById(form.getEstablecimientoId())
                .filter(Establecimiento::isActivo)
                .orElseThrow(() -> new IllegalArgumentException("Selecciona un establecimiento activo."));
        if (form.getDiaSemana() == null || !DIAS.contains(form.getDiaSemana())) {
            throw new IllegalArgumentException("Selecciona un día de la semana válido.");
        }
        LocalTime inicio = form.getHoraInicio();
        LocalTime fin = form.getHoraFin();
        if (inicio == null || fin == null || !fin.isAfter(inicio)) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio.");
        }
        boolean duplicado = idActual == null
                ? horarioRepository.existsByEstablecimiento_IdAndDiaSemanaIgnoreCaseAndHoraInicioAndHoraFin(
                        establecimiento.getId(), form.getDiaSemana(), inicio, fin)
                : horarioRepository.existsByEstablecimiento_IdAndDiaSemanaIgnoreCaseAndHoraInicioAndHoraFinAndIdNot(
                        establecimiento.getId(), form.getDiaSemana(), inicio, fin, idActual);
        if (duplicado) throw new IllegalArgumentException("Ese horario ya está registrado para el establecimiento.");
    }

    private void aplicarDatos(HorarioRecojo horario, HorarioRecojoForm form) {
        Establecimiento establecimiento = establecimientoRepository.findById(form.getEstablecimientoId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el establecimiento."));
        horario.setEstablecimiento(establecimiento);
        horario.setDiaSemana(form.getDiaSemana());
        horario.setHoraInicio(form.getHoraInicio());
        horario.setHoraFin(form.getHoraFin());
        horario.setActivo(form.isActivo());
    }

    public HorarioRecojoForm convertirAFormulario(HorarioRecojo horario) {
        HorarioRecojoForm form = new HorarioRecojoForm();
        form.setEstablecimientoId(horario.getEstablecimiento().getId());
        form.setDiaSemana(horario.getDiaSemana());
        form.setHoraInicio(horario.getHoraInicio());
        form.setHoraFin(horario.getHoraFin());
        form.setActivo(horario.isActivo());
        return form;
    }
}
