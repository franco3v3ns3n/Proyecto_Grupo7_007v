package com.veranum.mantenimiento.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MantenimientoRequestDTO {

    @NotNull(message = "El id del hotel es obligatorio")
    private Integer idHotel;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcionMantenimiento;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de término es obligatoria")
    private LocalDate fechaTermino;

    @NotNull(message = "El costo es obligatorio")
    @Positive(message = "El costo debe ser un valor positivo")
    private Double costoMantenimiento;

    @NotBlank(message = "El estado del mantenimiento es obligatorio")
    private String estadoMantenimiento;
}