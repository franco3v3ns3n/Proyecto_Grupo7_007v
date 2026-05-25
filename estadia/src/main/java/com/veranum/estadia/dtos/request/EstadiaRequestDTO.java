package com.veranum.estadia.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EstadiaRequestDTO {

    @NotNull(message = "El id del cliente es obligatorio")
    private Integer idCliente;

    @NotNull(message = "El id de la habitación es obligatorio")
    private Integer idHabitacion;

    private Integer idReserva;

    @NotNull(message = "La fecha de check-in es obligatoria")
    private LocalDateTime fechaCheckin;

    private LocalDateTime fechaCheckout;

    @NotBlank(message = "El estado de la estadía es obligatorio")
    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    private String estadoEstadia;
}
