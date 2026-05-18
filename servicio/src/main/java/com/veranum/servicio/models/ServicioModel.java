package com.veranum.servicio.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "servicios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Integer idServicio;

    @Column(name = "id_hotel", nullable = false)
    private Integer idHotel;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "valor_diario", nullable = false)
    private Double valorDiario;

    @Column(name = "estado_servicio", nullable = false, length = 30)
    private String estadoServicio;
}