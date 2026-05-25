package ms_cotizacion.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComidaDTO {
    private String descripcion;
    private Double precio;
}
