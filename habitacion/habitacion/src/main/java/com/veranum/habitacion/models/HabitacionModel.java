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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Sincronizado con el AUTO_INCREMENT de tu SQL
    @Column(name = "id_habitacion")
    private Integer id_habitacion;

    @Column(name = "id_hotel", nullable = false) // Mapeo de la FK del script
    private Integer idHotel;

    @Column(name = "tipo_habitacion", nullable = false, length = 100)
    private String tipo_habitacion;

    @Column(name = "numero_habitacion", nullable = false, length = 20)
    private String numero_habitacion;

    @Column(nullable = false)
    private Integer capacidad_personas;

    @Column(nullable = false)
    private Integer cantidad_camas;

    @Column(nullable = false)
    private Integer cantidad_banos;

    @Column(name = "precio_diario", nullable = false)
    private Double precio_diario; // Corresponde al DECIMAL(10,2)

    @Column(name = "estado_habitacion", nullable = false, length = 30)
    private String estado_habitacion;
}