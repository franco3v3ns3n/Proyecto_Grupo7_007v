package com.veranum.evento.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "centros_eventos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CentroEventoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_centro_evento")
    private Integer idCentroEvento;

    @Column(name = "id_hotel", nullable = false)
    private Integer idHotel;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "capacidad_personas", nullable = false)
    private Integer capacidadPersonas;

    @Column(name = "precio_centro_evento", nullable = false)
    private Double precioCentroEvento;

    @Column(name = "estado_centro_evento", nullable = false, length = 30)
    private String estadoCentroEvento;
}