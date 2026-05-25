package com.veranum.cliente.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteRequestDTO {

    @NotBlank(message = "Los nombres del cliente son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden superar 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos del cliente son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden superar 100 caracteres")
    private String apellidos;

    @NotBlank(message = "El rut del cliente es obligatorio")
    @Size(max = 12, message = "El rut no puede superar 12 caracteres")
    private String rut;

    @Size(max = 20, message = "El telefono no puede superar 20 caracteres")
    private String telefono;

    @Email(message = "El correo debe tener un formato valido")
    @Size(max = 100, message = "El correo no puede superar 100 caracteres")
    private String correo;

    @Size(max = 150, message = "La direccion no puede superar 150 caracteres")
    private String direccion;
}
