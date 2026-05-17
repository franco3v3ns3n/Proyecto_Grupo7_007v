package com.hotel.habitacion.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "Habitaciones")
public class ModelHabitacion {

    @Id
    private Long id_habitacion;
    private String numero_habitacion;
    private String tipo_habitacion;
    private Integer capacidad_personas;
    private Integer cantidad_camas;
    private Integer cantidad_banos;
    private String estado_habitacion;

}