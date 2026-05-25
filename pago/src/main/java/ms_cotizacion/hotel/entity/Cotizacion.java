package ms_cotizacion.hotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cotizaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;
    private Long habitacionId;
    private Integer cantidadNoches;
    private Double totalHabitacion;
    private Double totalServicios;
    private Double totalComida;
    private Double totalFinal;

    private LocalDateTime fechaEmision;

    @PrePersist
    protected void onCreate() {
        fechaEmision = LocalDateTime.now();
    }
}
