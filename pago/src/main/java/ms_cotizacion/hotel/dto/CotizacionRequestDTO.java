package ms_cotizacion.hotel.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotizacionRequestDTO {
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "El ID de la habitación es obligatorio")
    private Long habitacionId;

    @Min(value = 1, message = "La cantidad de noches debe ser al menos 1")
    private Integer cantidadNoches;

    private List<ServicioDTO> servicios;
    private List<ComidaDTO> comidas;
}
