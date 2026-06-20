package com.veranum.reserva.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

// @Schema en la clase describe el modelo completo que se mostrará en Swagger.
@Schema(description = "Datos de una reserva registrada")
@Data
@Builder
public class ReservaResponseDTO {

    // @Schema en un atributo agrega descripción y ejemplo para esa propiedad.
    @Schema(description = "ID único de la reserva", example = "1")
    private Integer idReserva;

    @Schema(description = "ID del cliente asociado a la reserva", example = "1")
    private Integer idCliente;

    @Schema(description = "ID de la habitación asociada a la reserva", example = "1")
    private Integer idHabitacion;

    @Schema(description = "Fecha de inicio de la reserva", example = "2026-07-20")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin de la reserva", example = "2026-07-25")
    private LocalDate fechaFin;

    @Schema(description = "Estado actual de la reserva", example = "CONFIRMADA")
    private String estadoReserva;

    @Schema(description = "Fecha y hora de creación de la reserva", example = "2026-06-20T10:30:00")
    private LocalDateTime fechaCreacion;
}
