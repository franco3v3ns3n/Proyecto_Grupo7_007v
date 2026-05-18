package com.veranum.servicio.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HotelResponseDTO {

    private Integer idHotel;
    private String nombre;
    private String ubicacion;
}