package com.veranum.mantenimiento.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MantenimientoRequestDTO {

    @NotNull(message = "El id de la habitación es obligatorio")
    private Integer idHabitacion;

    @NotBlank(message = "El tipo de mantenimiento es obligatorio")
    @Size(max = 100, message = "El tipo de mantenimiento no puede superar 100 caracteres")
    private String tipoMantenimiento;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    @NotBlank(message = "El estado del mantenimiento es obligatorio")
    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    private String estadoMantenimiento;
}
