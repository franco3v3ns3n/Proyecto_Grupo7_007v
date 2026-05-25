package veranum.mantenimiento.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MantenimientoResponseDTO {
    private Integer idMantenimiento;
    private Integer idHotel;
    private String descripcionMantenimiento;
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;
    private Double costoMantenimiento;
    private String estadoMantenimiento;
}