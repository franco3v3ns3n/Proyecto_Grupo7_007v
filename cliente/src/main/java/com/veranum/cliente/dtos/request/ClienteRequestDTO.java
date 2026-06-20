package com.veranum.cliente.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Datos necesarios para crear o actualizar un cliente")
@Data
public class ClienteRequestDTO {

    @Schema(description = "Nombres del cliente", example = "Juan Pablo")
    @NotBlank(message = "Los nombres del cliente son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden superar 100 caracteres")
    private String nombres;

    @Schema(description = "Apellidos del cliente", example = "Roa Soto")
    @NotBlank(message = "Los apellidos del cliente son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden superar 100 caracteres")
    private String apellidos;

    @Schema(description = "RUT del cliente", example = "12345678-9")
    @NotBlank(message = "El rut del cliente es obligatorio")
    @Size(max = 12, message = "El rut no puede superar 12 caracteres")
    private String rut;

    @Schema(description = "Teléfono de contacto del cliente", example = "912345678")
    @Size(max = 20, message = "El telefono no puede superar 20 caracteres")
    private String telefono;

    @Schema(description = "Correo electrónico del cliente", example = "juan.roa@test.cl")
    @Email(message = "El correo debe tener un formato valido")
    @Size(max = 100, message = "El correo no puede superar 100 caracteres")
    private String correo;

    @Schema(description = "Dirección del cliente", example = "Santiago Centro")
    @Size(max = 150, message = "La direccion no puede superar 150 caracteres")
    private String direccion;
}
