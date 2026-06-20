package com.veranum.mantenimiento.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Datos de un mantenimiento registrado")
@Data
@Builder
public class MantenimientoResponseDTO {

    @Schema(description = "ID único del mantenimiento", example = "1")
    private Integer idMantenimiento;

    @Schema(description = "ID de la habitación asociada al mantenimiento", example = "1")
    private Integer idHabitacion;

    @Schema(description = "Tipo de mantenimiento realizado o programado", example = "Limpieza profunda")
    private String tipoMantenimiento;

    @Schema(description = "Fecha y hora de inicio del mantenimiento", example = "2026-04-01T09:00:00")
    private LocalDateTime fechaInicio;

    @Schema(description = "Fecha y hora de término del mantenimiento", example = "2026-04-01T12:00:00")
    private LocalDateTime fechaFin;

    @Schema(description = "Estado actual del mantenimiento", example = "FINALIZADO")
    private String estadoMantenimiento;
}
