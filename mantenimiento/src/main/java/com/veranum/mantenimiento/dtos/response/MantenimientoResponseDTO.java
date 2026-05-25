package com.veranum.mantenimiento.dtos.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MantenimientoResponseDTO {

    private Integer idMantenimiento;
    private Integer idHabitacion;
    private String tipoMantenimiento;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String estadoMantenimiento;
}
