package com.veranum.estadia.services;

import com.veranum.estadia.clients.ClienteClient;
import com.veranum.estadia.clients.HabitacionClient;
import com.veranum.estadia.clients.ReservaClient;
import com.veranum.estadia.dtos.request.EstadiaRequestDTO;
import com.veranum.estadia.dtos.response.ClienteResponseDTO;
import com.veranum.estadia.dtos.response.EstadiaResponseDTO;
import com.veranum.estadia.dtos.response.HabitacionResponseDTO;
import com.veranum.estadia.dtos.response.ReservaResponseDTO;
import com.veranum.estadia.exceptions.BusinessRuleException;
import com.veranum.estadia.exceptions.ResourceNotFoundException;
import com.veranum.estadia.models.EstadiaModel;
import com.veranum.estadia.repositories.EstadiaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EstadiaServiceTest {

    private static final LocalDateTime FECHA_CHECKIN = LocalDateTime.of(2026, 9, 10, 15, 0);
    private static final LocalDateTime FECHA_CHECKOUT = LocalDateTime.of(2026, 9, 15, 11, 0);

    @Mock
    private EstadiaRepository estadiaRepository;

    @Mock
    private ClienteClient clienteClient;

    @Mock
    private HabitacionClient habitacionClient;

    @Mock
    private ReservaClient reservaClient;

    @InjectMocks
    private EstadiaService estadiaService;

    private EstadiaModel estadia;
    private EstadiaRequestDTO request;

    @BeforeEach
    void setUp() {
        // Configura los objetos de ejemplo antes de cada prueba.
        estadia = crearEstadiaModel();
        request = crearRequest();
    }

    @Test
    public void testObtenerEstadias() {
        // Define el comportamiento del repositorio simulado.
        when(estadiaRepository.findAll()).thenReturn(List.of(estadia));

        // Ejecuta el metodo del servicio.
        List<EstadiaResponseDTO> estadias = estadiaService.obtenerEstadias();

        // Verifica el resultado obtenido.
        assertNotNull(estadias);
        assertEquals(1, estadias.size());
        assertEquals(1, estadias.get(0).getIdEstadia());
        verify(estadiaRepository).findAll();
    }

    @Test
    public void testObtenerEstadiaPorId() {
        // Define el comportamiento del repositorio simulado.
        when(estadiaRepository.findById(1)).thenReturn(Optional.of(estadia));

        // Ejecuta el metodo del servicio.
        EstadiaResponseDTO encontrada = estadiaService.obtenerEstadiaPorId(1);

        // Verifica el resultado obtenido.
        assertNotNull(encontrada);
        assertEquals(1, encontrada.getIdEstadia());
        assertEquals("EN_CURSO", encontrada.getEstadoEstadia());
        verify(estadiaRepository).findById(1);
    }

    @Test
    public void testObtenerEstadiasPorCliente() {
        // Simula la existencia del cliente y las estadias asociadas.
        when(clienteClient.obtenerClientePorId(10)).thenReturn(crearCliente(10));
        when(estadiaRepository.findByIdCliente(10)).thenReturn(List.of(estadia));

        // Ejecuta el metodo del servicio.
        List<EstadiaResponseDTO> estadias = estadiaService.obtenerEstadiasPorCliente(10);

        // Verifica el resultado obtenido.
        assertNotNull(estadias);
        assertEquals(1, estadias.size());
        assertEquals(10, estadias.get(0).getIdCliente());
        verify(clienteClient).obtenerClientePorId(10);
        verify(estadiaRepository).findByIdCliente(10);
    }

    @Test
    public void testObtenerEstadiasPorHabitacion() {
        // Simula la existencia de la habitacion y sus estadias asociadas.
        when(habitacionClient.obtenerHabitacionPorId(20)).thenReturn(crearHabitacion(20));
        when(estadiaRepository.findByIdHabitacion(20)).thenReturn(List.of(estadia));

        // Ejecuta el metodo del servicio.
        List<EstadiaResponseDTO> estadias = estadiaService.obtenerEstadiasPorHabitacion(20);

        // Verifica el resultado obtenido.
        assertNotNull(estadias);
        assertEquals(1, estadias.size());
        assertEquals(20, estadias.get(0).getIdHabitacion());
        verify(habitacionClient).obtenerHabitacionPorId(20);
        verify(estadiaRepository).findByIdHabitacion(20);
    }

    @Test
    public void testObtenerEstadiasPorReserva() {
        // Simula la existencia de la reserva y sus estadias asociadas.
        when(reservaClient.obtenerReservaPorId(30)).thenReturn(crearReserva(30));
        when(estadiaRepository.findByIdReserva(30)).thenReturn(List.of(estadia));

        // Ejecuta el metodo del servicio.
        List<EstadiaResponseDTO> estadias = estadiaService.obtenerEstadiasPorReserva(30);

        // Verifica el resultado obtenido.
        assertNotNull(estadias);
        assertEquals(1, estadias.size());
        assertEquals(30, estadias.get(0).getIdReserva());
        verify(reservaClient).obtenerReservaPorId(30);
        verify(estadiaRepository).findByIdReserva(30);
    }

    @Test
    public void testObtenerEstadiasPorEstado() {
        // Define el comportamiento del repositorio simulado.
        when(estadiaRepository.findByEstadoEstadia("EN_CURSO")).thenReturn(List.of(estadia));

        // Ejecuta el metodo del servicio.
        List<EstadiaResponseDTO> estadias = estadiaService.obtenerEstadiasPorEstado("EN_CURSO");

        // Verifica el resultado obtenido.
        assertNotNull(estadias);
        assertEquals(1, estadias.size());
        assertEquals("EN_CURSO", estadias.get(0).getEstadoEstadia());
        verify(estadiaRepository).findByEstadoEstadia("EN_CURSO");
    }

    @Test
    public void testCrearEstadia() {
        // Simula las dependencias necesarias para crear la estadia.
        when(clienteClient.obtenerClientePorId(10)).thenReturn(crearCliente(10));
        when(habitacionClient.obtenerHabitacionPorId(20)).thenReturn(crearHabitacion(20));
        when(reservaClient.obtenerReservaPorId(30)).thenReturn(crearReserva(30));
        when(estadiaRepository.save(any(EstadiaModel.class))).thenAnswer(invocation -> {
            EstadiaModel estadia = invocation.getArgument(0);
            estadia.setIdEstadia(1);
            return estadia;
        });

        // Ejecuta el metodo del servicio.
        EstadiaResponseDTO guardada = estadiaService.crearEstadia(request);

        // Verifica el resultado obtenido.
        assertNotNull(guardada);
        assertEquals(1, guardada.getIdEstadia());
        assertEquals(10, guardada.getIdCliente());
        verify(clienteClient).obtenerClientePorId(10);
        verify(habitacionClient).obtenerHabitacionPorId(20);
        verify(reservaClient).obtenerReservaPorId(30);
        verify(estadiaRepository).save(any(EstadiaModel.class));
    }

    @Test
    public void testActualizarEstadia() {
        // Configura la solicitud actualizada y simula sus dependencias.
        request = crearRequestActualizado();
        when(estadiaRepository.findById(1)).thenReturn(Optional.of(estadia));
        when(clienteClient.obtenerClientePorId(11)).thenReturn(crearCliente(11));
        when(habitacionClient.obtenerHabitacionPorId(21)).thenReturn(crearHabitacion(21));
        when(reservaClient.obtenerReservaPorId(31)).thenReturn(crearReserva(31));
        when(estadiaRepository.save(estadia)).thenReturn(estadia);

        // Ejecuta el metodo del servicio.
        EstadiaResponseDTO actualizada = estadiaService.actualizarEstadia(1, request);

        // Verifica el resultado obtenido.
        assertNotNull(actualizada);
        assertEquals(11, actualizada.getIdCliente());
        assertEquals("FINALIZADA", actualizada.getEstadoEstadia());
        verify(estadiaRepository).findById(1);
        verify(clienteClient).obtenerClientePorId(11);
        verify(habitacionClient).obtenerHabitacionPorId(21);
        verify(reservaClient).obtenerReservaPorId(31);
        verify(estadiaRepository).save(estadia);
    }

    @Test
    public void testEliminarEstadia() {
        // Simula la busqueda de la estadia que sera eliminada.
        when(estadiaRepository.findById(1)).thenReturn(Optional.of(estadia));

        // Ejecuta el metodo del servicio.
        estadiaService.eliminarEstadia(1);

        // Verifica que el repositorio elimine la estadia encontrada.
        verify(estadiaRepository).findById(1);
        verify(estadiaRepository).delete(estadia);
    }

    @Test
    public void testObtenerEstadiaPorIdInexistente() {
        // Simula que la estadia solicitada no existe.
        when(estadiaRepository.findById(99)).thenReturn(Optional.empty());

        // Ejecuta el metodo y captura la excepcion esperada.
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> estadiaService.obtenerEstadiaPorId(99)
        );

        // Verifica el mensaje de la excepcion.
        assertEquals("Estad\u00eda no encontrada", exception.getMessage());
        verify(estadiaRepository).findById(99);
    }

    @Test
    public void testCrearEstadiaConFechasIguales() {
        // Configura una solicitud con fechas incoherentes.
        request.setFechaCheckout(FECHA_CHECKIN);

        // Ejecuta el metodo y captura la excepcion esperada.
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> estadiaService.crearEstadia(request)
        );

        // Verifica la regla de negocio y que no se invoquen dependencias posteriores.
        assertEquals("La fecha de checkout debe ser posterior a la fecha de check-in", exception.getMessage());
        verify(estadiaRepository, never()).save(any(EstadiaModel.class));
        verify(clienteClient, never()).obtenerClientePorId(anyInt());
        verify(habitacionClient, never()).obtenerHabitacionPorId(anyInt());
        verify(reservaClient, never()).obtenerReservaPorId(anyInt());
    }

    private EstadiaModel crearEstadiaModel() {
        return EstadiaModel.builder()
                .idEstadia(1)
                .idCliente(10)
                .idHabitacion(20)
                .idReserva(30)
                .fechaCheckin(FECHA_CHECKIN)
                .fechaCheckout(FECHA_CHECKOUT)
                .estadoEstadia("EN_CURSO")
                .build();
    }

    private EstadiaRequestDTO crearRequest() {
        EstadiaRequestDTO request = new EstadiaRequestDTO();
        request.setIdCliente(10);
        request.setIdHabitacion(20);
        request.setIdReserva(30);
        request.setFechaCheckin(FECHA_CHECKIN);
        request.setFechaCheckout(FECHA_CHECKOUT);
        request.setEstadoEstadia("EN_CURSO");
        return request;
    }

    private EstadiaRequestDTO crearRequestActualizado() {
        EstadiaRequestDTO request = new EstadiaRequestDTO();
        request.setIdCliente(11);
        request.setIdHabitacion(21);
        request.setIdReserva(31);
        request.setFechaCheckin(LocalDateTime.of(2026, 10, 1, 15, 0));
        request.setFechaCheckout(LocalDateTime.of(2026, 10, 5, 11, 0));
        request.setEstadoEstadia("FINALIZADA");
        return request;
    }

    private ClienteResponseDTO crearCliente(Integer idCliente) {
        return ClienteResponseDTO.builder()
                .idCliente(idCliente)
                .nombres("Ana")
                .apellidos("Perez")
                .rut("20123456-5")
                .build();
    }

    private HabitacionResponseDTO crearHabitacion(Integer idHabitacion) {
        return HabitacionResponseDTO.builder()
                .idHabitacion(idHabitacion)
                .idHotel(10)
                .tipoHabitacion("Simple")
                .numeroHabitacion("101")
                .estadoHabitacion("DISPONIBLE")
                .build();
    }

    private ReservaResponseDTO crearReserva(Integer idReserva) {
        return ReservaResponseDTO.builder()
                .idReserva(idReserva)
                .idCliente(10)
                .idHabitacion(20)
                .fechaInicio(LocalDate.of(2026, 9, 10))
                .fechaFin(LocalDate.of(2026, 9, 15))
                .estadoReserva("CONFIRMADA")
                .fechaCreacion(LocalDateTime.of(2026, 6, 20, 10, 30))
                .build();
    }
}
