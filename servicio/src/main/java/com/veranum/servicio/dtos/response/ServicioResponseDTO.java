package com.veranum.servicio.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Datos de un servicio registrado")
@Data
@Builder
public class ServicioResponseDTO {

    @Schema(description = "ID único del servicio", example = "1")
    private Integer idServicio;

    @Schema(description = "ID del hotel asociado al servicio", example = "1")
    private Integer idHotel;

    @Schema(description = "Nombre del servicio", example = "WiFi Premium")
    private String nombre;

    @Schema(description = "Valor diario del servicio", example = "5000")
    private Double valorDiario;

    @Schema(description = "Estado actual del servicio", example = "ACTIVO")
    private String estadoServicio;
}
