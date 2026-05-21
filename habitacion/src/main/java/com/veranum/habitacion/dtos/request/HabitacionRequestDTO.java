package com.veranum.habitacion.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HabitacionRequestDTO {

    @NotNull(message = "El id del hotel es obligatorio")
    @Positive(message = "El id del hotel debe ser mayor a 0")
    private Integer idHotel;

    @NotBlank(message = "El tipo de habitación es obligatorio")
    @Size(max = 100, message = "El tipo de habitación no puede superar los 100 caracteres")
    private String tipoHabitacion;

    @NotBlank(message = "El número de habitación es obligatorio")
    @Size(max = 20, message = "El número de habitación no puede superar los 20 caracteres")
    private String numeroHabitacion;

    @NotNull(message = "La capacidad de personas es obligatoria")
    @Positive(message = "La capacidad de personas debe ser mayor a 0")
    private Integer capacidadPersonas;

    @NotNull(message = "La cantidad de camas es obligatoria")
    @Positive(message = "La cantidad de camas debe ser mayor a 0")
    private Integer cantidadCamas;

    @NotNull(message = "La cantidad de baños es obligatoria")
    @Positive(message = "La cantidad de baños debe ser mayor a 0")
    private Integer cantidadBanos;

    @NotNull(message = "El precio diario es obligatorio")
    @Positive(message = "El precio diario debe ser mayor a 0")
    private Double precioDiario;

    @NotBlank(message = "El estado de la habitación es obligatorio")
    @Size(max = 30, message = "El estado no puede superar los 30 caracteres")
    private String estadoHabitacion;
}
