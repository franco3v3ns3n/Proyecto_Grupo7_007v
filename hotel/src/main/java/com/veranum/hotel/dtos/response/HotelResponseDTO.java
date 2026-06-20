package com.veranum.hotel.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Datos de un hotel registrado")
@Data
@Builder
public class HotelResponseDTO {

    @Schema(description = "ID único del hotel", example = "1")
    private Integer idHotel;

    @Schema(description = "Nombre del hotel", example = "Hotel Veranum Santiago")
    private String nombre;

    @Schema(description = "Ubicación del hotel", example = "Santiago")
    private String ubicacion;
}
