package com.veranum.empleado.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "empleados")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @Column(name = "id_hotel", nullable = false)
    private Integer idHotel;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    @Column(name = "tipo_empleado", nullable = false, length = 30)
    private String tipoEmpleado;

    @Column(name = "estado_empleado", nullable = false, length = 30)
    private String estadoEmpleado;
}
