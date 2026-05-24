package com.veranum.estadia.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HabitacionResponseDTO {

    private Integer idHabitacion;
    private Integer idHotel;
    private String tipoHabitacion;
    private String numeroHabitacion;
    private Integer capacidadPersonas;
    private Integer cantidadCamas;
    private Integer cantidadBanos;
    private Double precioDiario;
    private String estadoHabitacion;
}
