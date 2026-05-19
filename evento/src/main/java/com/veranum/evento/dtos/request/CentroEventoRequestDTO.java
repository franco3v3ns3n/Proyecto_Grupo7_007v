package com.veranum.evento.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CentroEventoRequestDTO {

    @NotNull(message = "El id del hotel es obligatorio")
    private Integer idHotel;

    @NotBlank(message = "El nombre del centro de evento es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser mayor a 0")
    private Integer capacidadPersonas;

    @NotNull(message = "El precio del centro de evento es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precioCentroEvento;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    private String estadoCentroEvento;
}