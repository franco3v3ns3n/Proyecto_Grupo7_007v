package com.veranum.evento.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Datos necesarios para crear o actualizar un centro de evento")
@Data
public class CentroEventoRequestDTO {

    @Schema(description = "ID del hotel asociado al centro de evento", example = "1")
    @NotNull(message = "El id del hotel es obligatorio")
    private Integer idHotel;

    @Schema(description = "Nombre del centro de evento", example = "Salón Pacífico")
    @NotBlank(message = "El nombre del centro de evento es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @Schema(description = "Capacidad máxima de personas del centro de evento", example = "150")
    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser mayor a 0")
    private Integer capacidadPersonas;

    @Schema(description = "Precio del centro de evento", example = "350000")
    @NotNull(message = "El precio del centro de evento es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precioCentroEvento;

    @Schema(description = "Estado actual del centro de evento", example = "ACTIVO")
    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    private String estadoCentroEvento;
}
