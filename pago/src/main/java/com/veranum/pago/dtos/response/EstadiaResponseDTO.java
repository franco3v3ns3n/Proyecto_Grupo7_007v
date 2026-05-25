package com.veranum.pago.dtos.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EstadiaResponseDTO {

    private Integer idEstadia;
    private Integer idCliente;
    private Integer idHabitacion;
    private Integer idReserva;
    private LocalDateTime fechaCheckin;
    private LocalDateTime fechaCheckout;
    private String estadoEstadia;
}
