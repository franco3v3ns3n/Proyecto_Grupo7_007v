package com.veranum.evento.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Datos de un centro de evento registrado")
@Data
@Builder
public class CentroEventoResponseDTO {

    @Schema(description = "ID único del centro de evento", example = "1")
    private Integer idCentroEvento;

    @Schema(description = "ID del hotel asociado al centro de evento", example = "1")
    private Integer idHotel;

    @Schema(description = "Nombre del centro de evento", example = "Salón Pacífico")
    private String nombre;

    @Schema(description = "Capacidad máxima de personas del centro de evento", example = "150")
    private Integer capacidadPersonas;

    @Schema(description = "Precio del centro de evento", example = "350000")
    private Double precioCentroEvento;

    @Schema(description = "Estado actual del centro de evento", example = "ACTIVO")
    private String estadoCentroEvento;
}
