package com.veranum.empleado.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Datos necesarios para crear o actualizar un empleado")
@Data
public class EmpleadoRequestDTO {

    @Schema(description = "ID del hotel asociado al empleado", example = "1")
    @NotNull(message = "El id del hotel es obligatorio")
    private Integer idHotel;

    @Schema(description = "Nombres del empleado", example = "Ignacio Tomas")
    @NotBlank(message = "Los nombres del empleado son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden superar 100 caracteres")
    private String nombres;

    @Schema(description = "Apellidos del empleado", example = "Lagos Vera")
    @NotBlank(message = "Los apellidos del empleado son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden superar 100 caracteres")
    private String apellidos;

    @Schema(description = "RUT del empleado", example = "21436587-1")
    @NotBlank(message = "El rut del empleado es obligatorio")
    @Size(max = 12, message = "El rut no puede superar 12 caracteres")
    private String rut;

    @Schema(description = "Tipo de empleado", example = "ADMINISTRADOR")
    @NotBlank(message = "El tipo de empleado es obligatorio")
    @Size(max = 30, message = "El tipo de empleado no puede superar 30 caracteres")
    private String tipoEmpleado;

    @Schema(description = "Estado actual del empleado", example = "ACTIVO")
    @NotBlank(message = "El estado del empleado es obligatorio")
    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    private String estadoEmpleado;
}
