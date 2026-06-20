package com.veranum.servicio.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Datos necesarios para crear o actualizar un servicio")
@Data
public class ServicioRequestDTO {

    @Schema(description = "ID del hotel asociado al servicio", example = "1")
    @NotNull(message = "El id del hotel es obligatorio")
    private Integer idHotel;

    @Schema(description = "Nombre del servicio", example = "WiFi Premium")
    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @Schema(description = "Valor diario del servicio", example = "5000")
    @NotNull(message = "El valor diario es obligatorio")
    @Positive(message = "El valor diario debe ser mayor a 0")
    private Double valorDiario;

    @Schema(description = "Estado actual del servicio", example = "ACTIVO")
    @NotBlank(message = "El estado del servicio es obligatorio")
    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    private String estadoServicio;
}
