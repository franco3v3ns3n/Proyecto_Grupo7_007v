package com.veranum.habitacion.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Datos de una habitación registrada")
@Data
@Builder
public class HabitacionResponseDTO {

    @Schema(description = "ID único de la habitación", example = "1")
    private Integer idHabitacion;

    @Schema(description = "ID del hotel asociado a la habitación", example = "1")
    private Integer idHotel;

    @Schema(description = "Tipo de habitación", example = "Simple")
    private String tipoHabitacion;

    @Schema(description = "Número de habitación", example = "101")
    private String numeroHabitacion;

    @Schema(description = "Capacidad máxima de personas", example = "1")
    private Integer capacidadPersonas;

    @Schema(description = "Cantidad de camas disponibles", example = "1")
    private Integer cantidadCamas;

    @Schema(description = "Cantidad de baños disponibles", example = "1")
    private Integer cantidadBanos;

    @Schema(description = "Precio diario de la habitación", example = "45000")
    private Double precioDiario;

    @Schema(description = "Estado actual de la habitación", example = "DISPONIBLE")
    private String estadoHabitacion;
}
