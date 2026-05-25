package com.veranum.empleado.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmpleadoRequestDTO {

    @NotNull(message = "El id del hotel es obligatorio")
    private Integer idHotel;

    @NotBlank(message = "Los nombres del empleado son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden superar 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos del empleado son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden superar 100 caracteres")
    private String apellidos;

    @NotBlank(message = "El rut del empleado es obligatorio")
    @Size(max = 12, message = "El rut no puede superar 12 caracteres")
    private String rut;

    @NotBlank(message = "El tipo de empleado es obligatorio")
    @Size(max = 30, message = "El tipo de empleado no puede superar 30 caracteres")
    private String tipoEmpleado;

    @NotBlank(message = "El estado del empleado es obligatorio")
    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    private String estadoEmpleado;
}
