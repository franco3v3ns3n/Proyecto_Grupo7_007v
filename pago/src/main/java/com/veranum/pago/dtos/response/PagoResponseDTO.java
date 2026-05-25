package com.veranum.pago.dtos.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PagoResponseDTO {

    private Integer idPago;
    private Integer idEstadia;
    private Double monto;
    private String metodoPago;
    private String estadoPago;
    private LocalDateTime fechaPago;
}
