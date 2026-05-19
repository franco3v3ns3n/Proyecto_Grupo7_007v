package com.veranum.habitacion.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "habitaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitacionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habitacion")
    private Integer idHabitacion;

    @Column(name = "id_hotel", nullable = false)
    private Integer idHotel;

    @Column(name = "tipo_habitacion", nullable = false, length = 100)
    private String tipoHabitacion;

    @Column(name = "numero_habitacion", nullable = false, length = 20)
    private String numeroHabitacion;

    @Column(nullable = false)
    private Integer capacidadPersonas;

    @Column(nullable = false)
    private Integer cantidadCamas;

    @Column(nullable = false)
    private Integer cantidadBanos;

    @Column(name = "precio_diario", nullable = false)
    private Double precioDiario;

    @Column(name = "estado_habitacion", nullable = false, length = 30)
    private String estadoHabitacion;
}