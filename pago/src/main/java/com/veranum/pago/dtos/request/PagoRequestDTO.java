package com.veranum.pago.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PagoRequestDTO {

    @NotNull(message = "El id de la estadía es obligatorio")
    private Integer idEstadia;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double monto;

    @NotBlank(message = "El método de pago es obligatorio")
    @Size(max = 50, message = "El método de pago no puede superar 50 caracteres")
    private String metodoPago;

    @NotBlank(message = "El estado del pago es obligatorio")
    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    private String estadoPago;

    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDateTime fechaPago;
}
