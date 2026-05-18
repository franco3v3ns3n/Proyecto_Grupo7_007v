package com.veranum.servicio.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ServicioRequestDTO {

    @NotNull(message = "El id del hotel es obligatorio")
    private Integer idHotel;

    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @NotNull(message = "El valor diario es obligatorio")
    @Positive(message = "El valor diario debe ser mayor a 0")
    private Double valorDiario;

    @NotBlank(message = "El estado del servicio es obligatorio")
    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    private String estadoServicio;
}