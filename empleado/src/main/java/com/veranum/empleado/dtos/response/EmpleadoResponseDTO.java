package com.veranum.empleado.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Datos de un empleado registrado")
@Data
@Builder
public class EmpleadoResponseDTO {

    @Schema(description = "ID único del empleado", example = "1")
    private Integer idEmpleado;

    @Schema(description = "ID del hotel asociado al empleado", example = "1")
    private Integer idHotel;

    @Schema(description = "Nombres del empleado", example = "Carlos Andrés")
    private String nombres;

    @Schema(description = "Apellidos del empleado", example = "Muñoz Rojas")
    private String apellidos;

    @Schema(description = "RUT del empleado", example = "11111111-1")
    private String rut;

    @Schema(description = "Tipo de empleado", example = "ADMINISTRADOR")
    private String tipoEmpleado;

    @Schema(description = "Estado actual del empleado", example = "ACTIVO")
    private String estadoEmpleado;
}
