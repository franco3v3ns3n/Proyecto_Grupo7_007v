package com.veranum.estadia.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Datos de una estadía registrada")
@Data
@Builder
public class EstadiaResponseDTO {

    @Schema(description = "ID único de la estadía", example = "1")
    private Integer idEstadia;

    @Schema(description = "ID del cliente asociado a la estadía", example = "5")
    private Integer idCliente;

    @Schema(description = "ID de la habitación ocupada", example = "2")
    private Integer idHabitacion;

    @Schema(description = "ID de la reserva asociada a la estadía", example = "12")
    private Integer idReserva;

    @Schema(description = "Fecha y hora de check-in", example = "2026-09-10T15:00:00")
    private LocalDateTime fechaCheckin;

    @Schema(description = "Fecha y hora de check-out", example = "2026-09-15T11:00:00")
    private LocalDateTime fechaCheckout;

    @Schema(description = "Estado actual de la estadía", example = "FINALIZADA")
    private String estadoEstadia;
}
