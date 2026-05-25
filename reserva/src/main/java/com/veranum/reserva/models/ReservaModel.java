package com.veranum.reserva.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer idReserva;

    @Column(name = "id_cliente", nullable = false)
    private Integer idCliente;

    @Column(name = "id_habitacion", nullable = false)
    private Integer idHabitacion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "estado_reserva", nullable = false, length = 30)
    private String estadoReserva;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
}
