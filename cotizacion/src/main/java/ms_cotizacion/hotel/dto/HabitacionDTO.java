package ms_cotizacion.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionDTO {
    private Long id;
    private Double precioBase;
    private String tipo;
}
