package veranum.mantenimiento.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "mantenimientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MantenimientoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mantenimiento")
    private Integer id_mantenimiento;

    @Column(name = "id_hotel", nullable = false)
    private Integer id_hotel;

    @Column(name = "descripcion_mantenimiento", nullable = false, length = 500)
    private String descripcion_mantenimiento;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fecha_inicio;

    @Column(name = "fecha_termino", nullable = false)
    private LocalDate fecha_termino;

    @Column(name = "costo_mantenimiento", nullable = false)
    private Double costo_mantenimiento;

    @Column(name = "estado_mantenimiento", nullable = false)
    private String estado_mantenimiento;
}