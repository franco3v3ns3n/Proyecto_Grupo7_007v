package ms_cotizacion.hotel.service;

import lombok.extern.slf4j.Slf4j;
import ms_cotizacion.hotel.dto.*;
import ms_cotizacion.hotel.entity.Cotizacion;
import ms_cotizacion.hotel.exception.HabitacionNotFoundException;
import ms_cotizacion.hotel.exception.RemoteServiceException;
import ms_cotizacion.hotel.repository.CotizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class CotizacionService {

    @Autowired
    private CotizacionRepository cotizacionRepository;

    @Autowired
    private WebClient webClient;

    @org.springframework.beans.factory.annotation.Value("${service.ms-cliente.url}")
    private String CLIENTE_SERVICE_URL;

    @org.springframework.beans.factory.annotation.Value("${service.ms-auth.url}")
    private String AUTH_SERVICE_URL;

    @org.springframework.beans.factory.annotation.Value("${service.ms-habitacion.url}")
    private String HABITACION_SERVICE_URL;

    public void validarPermisos(Long usuarioId) {
        log.info("Validando permisos en ms-auth para el usuario: {}", usuarioId);
        
        Boolean esValido = webClient.get()
                .uri(AUTH_SERVICE_URL + "/auth/validar/" + usuarioId)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response -> {
                    log.error("Usuario sin permisos o error en ms-auth. Status: {}", response.statusCode());
                    return Mono.error(new RemoteServiceException("Acceso denegado o error de autenticación", (HttpStatus) response.statusCode()));
                })
                .bodyToMono(Boolean.class)
                .block();

        if (esValido == null || !esValido) {
            throw new RemoteServiceException("El usuario no tiene permisos suficientes", HttpStatus.FORBIDDEN);
        }
    }

    public ClienteExternoDTO obtenerDatosCliente(Long id) {
        log.info("Iniciando llamada remota a ms-cliente para el ID: {}", id);

        return webClient.get()
                .uri(CLIENTE_SERVICE_URL + "/clientes/" + id)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> {
                    log.error("Cliente no encontrado en ms-cliente para el ID: {}", id);
                    return Mono.error(new RemoteServiceException("Cliente no encontrado", HttpStatus.NOT_FOUND));
                })
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response -> {
                    log.error("Error de comunicación con ms-cliente. Status: {}", response.statusCode());
                    return Mono.error(new RemoteServiceException("Error en el servicio de clientes", (HttpStatus) response.statusCode()));
                })
                .bodyToMono(ClienteExternoDTO.class)
                .block(); // Bloqueamos para mantener compatibilidad con el flujo imperativo de este ms
    }

    public CotizacionResponseDTO procesarCotizacion(CotizacionRequestDTO request) {
        // 1. Validar cliente antes de cotizar
        ClienteExternoDTO cliente = obtenerDatosCliente(request.getClienteId());
        if (cliente == null) {
            throw new RemoteServiceException("Los datos del cliente son obligatorios y no pueden ser nulos", HttpStatus.BAD_REQUEST);
        }

        // 2. Consultar precio base de la habitación usando WebClient
        log.info("Consultando precio base para habitación ID: {}", request.getHabitacionId());
        HabitacionDTO habitacion = webClient.get()
                .uri(HABITACION_SERVICE_URL + "/habitaciones/" + request.getHabitacionId())
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> 
                    Mono.error(new HabitacionNotFoundException("La habitación no existe")))
                .bodyToMono(HabitacionDTO.class)
                .block();

        if (habitacion == null) {
            throw new HabitacionNotFoundException("No se encontró información para la habitación ID: " + request.getHabitacionId());
        }

        // 2. Cálculo de cobro por habitación
        Double totalHabitacion = habitacion.getPrecioBase() * request.getCantidadNoches();

        // 3. Cálculo de cobro por servicios
        Double totalServicios = calculateTotalServicios(request.getServicios());

        // 4. Cálculo de cobro por comida
        Double totalComida = calculateTotalComida(request.getComidas());

        // 5. Cálculo total final
        Double totalFinal = totalHabitacion + totalServicios + totalComida;

        // 6. Guardar en base de datos
        Cotizacion cotizacion = Cotizacion.builder()
                .clienteId(request.getClienteId())
                .habitacionId(request.getHabitacionId())
                .cantidadNoches(request.getCantidadNoches())
                .totalHabitacion(totalHabitacion)
                .totalServicios(totalServicios)
                .totalComida(totalComida)
                .totalFinal(totalFinal)
                .build();

        Cotizacion savedCotizacion = cotizacionRepository.save(cotizacion);

        // 7. Mapear a Response DTO
        return mapToResponseDTO(savedCotizacion);
    }

    private Double calculateTotalServicios(List<ServicioDTO> servicios) {
        if (servicios == null || servicios.isEmpty()) return 0.0;
        return servicios.stream()
                .mapToDouble(ServicioDTO::getPrecio)
                .sum();
    }

    private Double calculateTotalComida(List<ComidaDTO> comidas) {
        if (comidas == null || comidas.isEmpty()) return 0.0;
        return comidas.stream()
                .mapToDouble(ComidaDTO::getPrecio)
                .sum();
    }

    private CotizacionResponseDTO mapToResponseDTO(Cotizacion cotizacion) {
        return CotizacionResponseDTO.builder()
                .id(cotizacion.getId())
                .clienteId(cotizacion.getClienteId())
                .habitacionId(cotizacion.getHabitacionId())
                .cantidadNoches(cotizacion.getCantidadNoches())
                .totalHabitacion(cotizacion.getTotalHabitacion())
                .totalServicios(cotizacion.getTotalServicios())
                .totalComida(cotizacion.getTotalComida())
                .totalFinal(cotizacion.getTotalFinal())
                .fechaEmision(cotizacion.getFechaEmision())
                .build();
    }
}
