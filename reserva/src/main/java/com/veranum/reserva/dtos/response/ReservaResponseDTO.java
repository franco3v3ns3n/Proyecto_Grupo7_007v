package com.veranum.reserva.dtos.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ReservaResponseDTO {

    private Integer idReserva;
    private Integer idCliente;
    private Integer idHabitacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estadoReserva;
    private LocalDateTime fechaCreacion;
}
