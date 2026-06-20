package com.veranum.pago.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Datos necesarios para crear o actualizar un pago")
@Data
public class PagoRequestDTO {

    @Schema(description = "ID de la estadía asociada al pago", example = "1")
    @NotNull(message = "El id de la estadía es obligatorio")
    private Integer idEstadia;

    @Schema(description = "Monto del pago", example = "240000")
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double monto;

    @Schema(description = "Método utilizado para realizar el pago", example = "TARJETA_CREDITO")
    @NotBlank(message = "El método de pago es obligatorio")
    @Size(max = 50, message = "El método de pago no puede superar 50 caracteres")
    private String metodoPago;

    @Schema(description = "Estado actual del pago", example = "PAGADO")
    @NotBlank(message = "El estado del pago es obligatorio")
    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    private String estadoPago;

    @Schema(description = "Fecha y hora del pago", example = "2026-04-13T10:50:00")
    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDateTime fechaPago;
}
