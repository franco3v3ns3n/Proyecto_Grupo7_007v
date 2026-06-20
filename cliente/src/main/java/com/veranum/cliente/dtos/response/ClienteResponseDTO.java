package com.veranum.cliente.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Datos de un cliente registrado")
@Data
@Builder
public class ClienteResponseDTO {

    @Schema(description = "ID único del cliente", example = "1")
    private Integer idCliente;

    @Schema(description = "Nombres del cliente", example = "Martina Sofia")
    private String nombres;

    @Schema(description = "Apellidos del cliente", example = "Silva Torres")
    private String apellidos;

    @Schema(description = "RUT del cliente", example = "20123456-5")
    private String rut;

    @Schema(description = "Teléfono de contacto del cliente", example = "987654321")
    private String telefono;

    @Schema(description = "Correo electrónico del cliente", example = "martina.silva@example.cl")
    private String correo;

    @Schema(description = "Dirección del cliente", example = "Las Condes")
    private String direccion;
}
