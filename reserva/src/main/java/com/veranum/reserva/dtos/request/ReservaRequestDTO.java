package com.veranum.reserva.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

// @Schema en la clase describe el modelo completo que se mostrará en Swagger.
@Schema(description = "Datos necesarios para crear o actualizar una reserva")
@Data
public class ReservaRequestDTO {

    // @Schema en un atributo agrega descripción y ejemplo para esa propiedad.
    @Schema(description = "ID del cliente que realiza la reserva", example = "1")
    @NotNull(message = "El id del cliente es obligatorio")
    private Integer idCliente;

    @Schema(description = "ID de la habitación reservada", example = "1")
    @NotNull(message = "El id de la habitación es obligatorio")
    private Integer idHabitacion;

    @Schema(description = "Fecha de inicio de la reserva", example = "2026-07-20")
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin de la reserva", example = "2026-07-25")
    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    @Schema(description = "Estado actual de la reserva", example = "CONFIRMADA")
    @NotBlank(message = "El estado de la reserva es obligatorio")
    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    private String estadoReserva;
}
