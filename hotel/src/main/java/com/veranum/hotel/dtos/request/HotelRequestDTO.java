package com.veranum.hotel.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Datos necesarios para crear o actualizar un hotel")
@Data
public class HotelRequestDTO {

    @Schema(description = "Nombre del hotel", example = "Hotel Veranum Santiago")
    @NotBlank(message = "El nombre del hotel es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @Schema(description = "Ubicación del hotel", example = "Santiago")
    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 150, message = "La ubicación no puede superar 150 caracteres")
    private String ubicacion;
}
