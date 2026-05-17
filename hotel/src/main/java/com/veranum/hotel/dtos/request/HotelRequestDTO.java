package com.veranum.hotel.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HotelRequestDTO {

    @NotBlank(message = "El nombre del hotel es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 150, message = "La ubicación no puede superar 150 caracteres")
    private String ubicacion;
}