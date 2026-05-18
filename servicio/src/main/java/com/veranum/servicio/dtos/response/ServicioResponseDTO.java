package com.veranum.servicio.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServicioResponseDTO {

    private Integer idServicio;
    private Integer idHotel;
    private String nombre;
    private Double valorDiario;
    private String estadoServicio;
}