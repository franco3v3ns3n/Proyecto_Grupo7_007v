package com.veranum.estadia.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "estadias")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadiaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estadia")
    private Integer idEstadia;

    @Column(name = "id_cliente", nullable = false)
    private Integer idCliente;

    @Column(name = "id_habitacion", nullable = false)
    private Integer idHabitacion;

    @Column(name = "id_reserva")
    private Integer idReserva;

    @Column(name = "fecha_checkin", nullable = false)
    private LocalDateTime fechaCheckin;

    @Column(name = "fecha_checkout")
    private LocalDateTime fechaCheckout;

    @Column(name = "estado_estadia", nullable = false, length = 30)
    private String estadoEstadia;
}
