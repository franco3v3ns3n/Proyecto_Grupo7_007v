package com.veranum.pago.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Datos de un pago registrado")
@Data
@Builder
public class PagoResponseDTO {

    @Schema(description = "ID único del pago", example = "1")
    private Integer idPago;

    @Schema(description = "ID de la estadía asociada al pago", example = "1")
    private Integer idEstadia;

    @Schema(description = "Monto del pago", example = "240000")
    private Double monto;

    @Schema(description = "Método utilizado para realizar el pago", example = "TARJETA_CREDITO")
    private String metodoPago;

    @Schema(description = "Estado actual del pago", example = "PAGADO")
    private String estadoPago;

    @Schema(description = "Fecha y hora del pago", example = "2026-04-13T10:50:00")
    private LocalDateTime fechaPago;
}
