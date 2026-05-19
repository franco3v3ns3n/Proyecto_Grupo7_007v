package com.veranum.evento.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CentroEventoResponseDTO {

    private Integer idCentroEvento;
    private Integer idHotel;
    private String nombre;
    private Integer capacidadPersonas;
    private Double precioCentroEvento;
    private String estadoCentroEvento;
}