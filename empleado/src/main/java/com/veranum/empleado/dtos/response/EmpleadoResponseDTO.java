package com.veranum.empleado.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmpleadoResponseDTO {

    private Integer idEmpleado;
    private Integer idHotel;
    private String nombres;
    private String apellidos;
    private String rut;
    private String tipoEmpleado;
    private String estadoEmpleado;
}
