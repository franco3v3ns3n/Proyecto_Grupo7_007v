package com.veranum.habitacion.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Datos necesarios para crear o actualizar una habitación")
@Data
public class HabitacionRequestDTO {

    @Schema(description = "ID del hotel asociado a la habitación", example = "1")
    @NotNull(message = "El id del hotel es obligatorio")
    @Positive(message = "El id del hotel debe ser mayor a 0")
    private Integer idHotel;

    @Schema(description = "Tipo de habitación", example = "Simple")
    @NotBlank(message = "El tipo de habitación es obligatorio")
    @Size(max = 100, message = "El tipo de habitación no puede superar los 100 caracteres")
    private String tipoHabitacion;

    @Schema(description = "Número de habitación", example = "101")
    @NotBlank(message = "El número de habitación es obligatorio")
    @Size(max = 20, message = "El número de habitación no puede superar los 20 caracteres")
    private String numeroHabitacion;

    @Schema(description = "Capacidad máxima de personas", example = "1")
    @NotNull(message = "La capacidad de personas es obligatoria")
    @Positive(message = "La capacidad de personas debe ser mayor a 0")
    private Integer capacidadPersonas;

    @Schema(description = "Cantidad de camas disponibles", example = "1")
    @NotNull(message = "La cantidad de camas es obligatoria")
    @Positive(message = "La cantidad de camas debe ser mayor a 0")
    private Integer cantidadCamas;

    @Schema(description = "Cantidad de baños disponibles", example = "1")
    @NotNull(message = "La cantidad de baños es obligatoria")
    @Positive(message = "La cantidad de baños debe ser mayor a 0")
    private Integer cantidadBanos;

    @Schema(description = "Precio diario de la habitación", example = "45000")
    @NotNull(message = "El precio diario es obligatorio")
    @Positive(message = "El precio diario debe ser mayor a 0")
    private Double precioDiario;

    @Schema(description = "Estado actual de la habitación", example = "DISPONIBLE")
    @NotBlank(message = "El estado de la habitación es obligatorio")
    @Size(max = 30, message = "El estado no puede superar los 30 caracteres")
    @Pattern(regexp = "^(DISPONIBLE|OCUPADA|MANTENIMIENTO)$",
            message = "El estado debe ser DISPONIBLE, OCUPADA o MANTENIMIENTO")
    private String estadoHabitacion;
}
