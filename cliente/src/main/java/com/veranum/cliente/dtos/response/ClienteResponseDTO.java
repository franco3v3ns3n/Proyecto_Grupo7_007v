package com.veranum.cliente.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClienteResponseDTO {

    private Integer idCliente;
    private String nombres;
    private String apellidos;
    private String rut;
    private String telefono;
    private String correo;
    private String direccion;
}
