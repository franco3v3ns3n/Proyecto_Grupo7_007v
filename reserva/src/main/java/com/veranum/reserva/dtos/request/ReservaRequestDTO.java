package com.veranum.reserva.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservaRequestDTO {

    @NotNull(message = "El id del cliente es obligatorio")
    private Integer idCliente;

    @NotNull(message = "El id de la habitación es obligatorio")
    private Integer idHabitacion;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    @NotBlank(message = "El estado de la reserva es obligatorio")
    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    private String estadoReserva;
}
