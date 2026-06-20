package com.veranum.mantenimiento.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Datos necesarios para crear o actualizar un mantenimiento")
@Data
public class MantenimientoRequestDTO {

    @Schema(description = "ID de la habitación asociada al mantenimiento", example = "1")
    @NotNull(message = "El id de la habitación es obligatorio")
    private Integer idHabitacion;

    @Schema(description = "Tipo de mantenimiento realizado o programado", example = "Limpieza profunda")
    @NotBlank(message = "El tipo de mantenimiento es obligatorio")
    @Size(max = 100, message = "El tipo de mantenimiento no puede superar 100 caracteres")
    private String tipoMantenimiento;

    @Schema(description = "Fecha y hora de inicio del mantenimiento", example = "2026-04-01T09:00:00")
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    @Schema(description = "Fecha y hora de término del mantenimiento", example = "2026-04-01T12:00:00")
    private LocalDateTime fechaFin;

    @Schema(description = "Estado actual del mantenimiento", example = "FINALIZADO")
    @NotBlank(message = "El estado del mantenimiento es obligatorio")
    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    @Pattern(regexp = "^(PROGRAMADO|EN_PROCESO|FINALIZADO)$",
            message = "El estado debe ser PROGRAMADO, EN_PROCESO o FINALIZADO")
    private String estadoMantenimiento;
}
