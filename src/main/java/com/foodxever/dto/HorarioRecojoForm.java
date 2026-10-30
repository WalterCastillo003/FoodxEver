package com.foodxever.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalTime;

public class HorarioRecojoForm {
    private Long establecimientoId;
    private String diaSemana;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaFin;

    private boolean activo = true;

    public Long getEstablecimientoId() { return establecimientoId; }
    public void setEstablecimientoId(Long establecimientoId) { this.establecimientoId = establecimientoId; }
    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
