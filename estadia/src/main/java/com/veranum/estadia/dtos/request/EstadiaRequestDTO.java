package com.veranum.estadia.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Datos necesarios para crear o actualizar una estadía")
@Data
public class EstadiaRequestDTO {

    @Schema(description = "ID del cliente asociado a la estadía", example = "2")
    @NotNull(message = "El id del cliente es obligatorio")
    private Integer idCliente;

    @Schema(description = "ID de la habitación ocupada", example = "7")
    @NotNull(message = "El id de la habitación es obligatorio")
    private Integer idHabitacion;

    @Schema(description = "ID de la reserva asociada a la estadía, si existe", example = "7")
    private Integer idReserva;

    @Schema(description = "Fecha y hora de check-in", example = "2026-05-20T15:00:00")
    @NotNull(message = "La fecha de check-in es obligatoria")
    private LocalDateTime fechaCheckin;

    @Schema(description = "Fecha y hora de check-out", example = "2026-05-23T11:00:00")
    private LocalDateTime fechaCheckout;

    @Schema(description = "Estado actual de la estadía", example = "EN_CURSO")
    @NotBlank(message = "El estado de la estadía es obligatorio")
    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    private String estadoEstadia;
}
