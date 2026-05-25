package ms_cotizacion.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteExternoDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String rut;
    private String email;
    private String telefono;
}
