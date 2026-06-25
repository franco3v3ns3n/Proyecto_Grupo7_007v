package com.veranum.reserva.services;

import com.veranum.reserva.clients.ClienteClient;
import com.veranum.reserva.clients.HabitacionClient;
import com.veranum.reserva.dtos.request.ReservaRequestDTO;
import com.veranum.reserva.dtos.response.ClienteResponseDTO;
import com.veranum.reserva.dtos.response.HabitacionResponseDTO;
import com.veranum.reserva.dtos.response.ReservaResponseDTO;
import com.veranum.reserva.exceptions.BusinessRuleException;
import com.veranum.reserva.exceptions.ResourceNotFoundException;
import com.veranum.reserva.models.ReservaModel;
import com.veranum.reserva.repositories.ReservaRepository;
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

// Habilita Mockito con JUnit 5 para crear e inyectar dobles de prueba.
@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    // Fechas fijas para mantener fixtures deterministas y reutilizables.
    private static final LocalDate FECHA_INICIO = LocalDate.of(2026, 7, 20);
    private static final LocalDate FECHA_FIN = LocalDate.of(2026, 7, 25);
    private static final LocalDateTime FECHA_CREACION = LocalDateTime.of(2026, 6, 20, 10, 30);

    @Mock
    private ReservaRepository reservaRepository; // Doble de prueba del repositorio.

    @Mock
    private ClienteClient clienteClient; // Feign Client simulado; no realiza llamadas reales.

    @Mock
    private HabitacionClient habitacionClient; // Feign Client simulado; no realiza llamadas reales.

    @InjectMocks
    private ReservaService reservaService; // Construye el servicio e inyecta los mocks anteriores.

    private ReservaModel reserva;
    private ReservaRequestDTO request;

    @BeforeEach
    void setUp() {
        // Crea una reserva y una solicitud válidas para cada prueba.
        reserva = crearReservaModel();
        request = crearRequest();
    }

    @Test
    public void testObtenerReservas() {
        // Arrange: el repositorio simulado contiene una reserva.
        when(reservaRepository.findAll()).thenReturn(List.of(reserva));

        // Act: se ejecuta el método del servicio.
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservas();

        // Assert: se comprueba la respuesta mapeada y la interacción con el repositorio.
        assertNotNull(reservas);
        assertEquals(1, reservas.size());
        assertEquals(1, reservas.get(0).getIdReserva());
        verify(reservaRepository).findAll();
    }

    @Test
    public void testObtenerReservaPorId() {
        // Arrange: el repositorio simulado encuentra la reserva por ID.
        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));

        // Act: se ejecuta la búsqueda del servicio.
        ReservaResponseDTO encontrada = reservaService.obtenerReservaPorId(1);

        // Assert: se comprueba el DTO devuelto y la consulta al repositorio.
        assertNotNull(encontrada);
        assertEquals(1, encontrada.getIdReserva());
        assertEquals("CONFIRMADA", encontrada.getEstadoReserva());
        verify(reservaRepository).findById(1);
    }

    @Test
    public void testObtenerReservasPorCliente() {
        // Arrange: el Feign Client valida el cliente sin llamada real y el repositorio entrega sus reservas.
        when(clienteClient.obtenerClientePorId(10)).thenReturn(crearCliente(10));
        when(reservaRepository.findByIdCliente(10)).thenReturn(List.of(reserva));

        // Act: se consultan las reservas por cliente.
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorCliente(10);

        // Assert: se comprueba el resultado y las dependencias invocadas.
        assertNotNull(reservas);
        assertEquals(1, reservas.size());
        assertEquals(10, reservas.get(0).getIdCliente());
        verify(clienteClient).obtenerClientePorId(10);
        verify(reservaRepository).findByIdCliente(10);
    }

    @Test
    public void testObtenerReservasPorHabitacion() {
        // Arrange: el Feign Client valida la habitación sin llamada real y el repositorio entrega sus reservas.
        when(habitacionClient.obtenerHabitacionPorId(20)).thenReturn(crearHabitacion(20));
        when(reservaRepository.findByIdHabitacion(20)).thenReturn(List.of(reserva));

        // Act: se consultan las reservas por habitación.
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorHabitacion(20);

        // Assert: se comprueba el resultado y las dependencias invocadas.
        assertNotNull(reservas);
        assertEquals(1, reservas.size());
        assertEquals(20, reservas.get(0).getIdHabitacion());
        verify(habitacionClient).obtenerHabitacionPorId(20);
        verify(reservaRepository).findByIdHabitacion(20);
    }

    @Test
    public void testObtenerReservasPorEstado() {
        // Arrange: el repositorio simulado devuelve reservas con estado CONFIRMADA.
        when(reservaRepository.findByEstadoReserva("CONFIRMADA")).thenReturn(List.of(reserva));

        // Act: se consultan las reservas por estado.
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorEstado("CONFIRMADA");

        // Assert: se comprueba el estado devuelto y la interacción con el repositorio.
        assertNotNull(reservas);
        assertEquals(1, reservas.size());
        assertEquals("CONFIRMADA", reservas.get(0).getEstadoReserva());
        verify(reservaRepository).findByEstadoReserva("CONFIRMADA");
    }

    @Test
    public void testCrearReserva() {
        // Arrange: los Feign Clients simulados validan Cliente y Habitación sin llamadas reales.
        when(clienteClient.obtenerClientePorId(10)).thenReturn(crearCliente(10));
        when(habitacionClient.obtenerHabitacionPorId(20)).thenReturn(crearHabitacion(20));
        // thenAnswer imita la persistencia asignando el ID al guardar la reserva.
        when(reservaRepository.save(any(ReservaModel.class))).thenAnswer(invocation -> {
            ReservaModel reserva = invocation.getArgument(0);
            reserva.setIdReserva(1);
            return reserva;
        });

        // Act: se ejecuta la creación en la capa de servicio.
        ReservaResponseDTO guardada = reservaService.crearReserva(request);

        // Assert: se comprueba el DTO creado y las dependencias requeridas.
        assertNotNull(guardada);
        assertEquals(1, guardada.getIdReserva());
        assertNotNull(guardada.getFechaCreacion());
        verify(clienteClient).obtenerClientePorId(10);
        verify(habitacionClient).obtenerHabitacionPorId(20);
        verify(reservaRepository).save(any(ReservaModel.class));
    }

    @Test
    public void testActualizarReserva() {
        // Arrange: existe la reserva original y los Feign Clients validan los nuevos IDs.
        request = crearRequestActualizado();
        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));
        when(clienteClient.obtenerClientePorId(11)).thenReturn(crearCliente(11));
        when(habitacionClient.obtenerHabitacionPorId(21)).thenReturn(crearHabitacion(21));
        when(reservaRepository.save(reserva)).thenReturn(reserva);

        // Act: se ejecuta la actualización en la capa de servicio.
        ReservaResponseDTO actualizada = reservaService.actualizarReserva(1, request);

        // Assert: se comprueba la respuesta actualizada y la persistencia.
        assertNotNull(actualizada);
        assertEquals(11, actualizada.getIdCliente());
        assertEquals("FINALIZADA", actualizada.getEstadoReserva());
        verify(reservaRepository).findById(1);
        verify(reservaRepository).save(reserva);
    }

    @Test
    public void testEliminarReserva() {
        // Arrange: el repositorio simulado encuentra la reserva que será eliminada.
        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));

        // Act: se ejecuta la eliminación en la capa de servicio.
        reservaService.eliminarReserva(1);

        // Assert: se comprueba que el repositorio elimine la reserva encontrada.
        verify(reservaRepository).findById(1);
        verify(reservaRepository).delete(reserva);
    }

    @Test
    public void testObtenerReservaPorIdInexistente() {
        // Arrange: el repositorio simulado no encuentra la reserva solicitada.
        when(reservaRepository.findById(99)).thenReturn(Optional.empty());

        // Act y Assert: assertThrows comprueba la excepción esperada para el recurso inexistente.
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reservaService.obtenerReservaPorId(99)
        );

        // Assert adicional: se verifica el mensaje y la consulta realizada.
        assertEquals("Reserva no encontrada", exception.getMessage());
        verify(reservaRepository).findById(99);
    }

    @Test
    public void testCrearReservaConFechasIguales() {
        // Arrange: la fecha de fin inválida debe detener el flujo antes de dependencias externas.
        request.setFechaFin(FECHA_INICIO);

        // Act y Assert: assertThrows comprueba la excepción de negocio esperada.
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> reservaService.crearReserva(request)
        );

        // never() verifica que no se consulten Feign Clients ni se guarden datos inválidos.
        assertEquals("La fecha de fin debe ser posterior a la fecha de inicio", exception.getMessage());
        verify(reservaRepository, never()).save(any(ReservaModel.class));
        verify(clienteClient, never()).obtenerClientePorId(anyInt());
        verify(habitacionClient, never()).obtenerHabitacionPorId(anyInt());
    }

    // Métodos privados que construyen fixtures reutilizables para mantener legibles las pruebas.
    private ReservaModel crearReservaModel() {
        return ReservaModel.builder()
                .idReserva(1)
                .idCliente(10)
                .idHabitacion(20)
                .fechaInicio(FECHA_INICIO)
                .fechaFin(FECHA_FIN)
                .estadoReserva("CONFIRMADA")
                .fechaCreacion(FECHA_CREACION)
                .build();
    }

    private ReservaRequestDTO crearRequest() {
        ReservaRequestDTO request = new ReservaRequestDTO();
        request.setIdCliente(10);
        request.setIdHabitacion(20);
        request.setFechaInicio(FECHA_INICIO);
        request.setFechaFin(FECHA_FIN);
        request.setEstadoReserva("CONFIRMADA");
        return request;
    }

    private ReservaRequestDTO crearRequestActualizado() {
        ReservaRequestDTO request = new ReservaRequestDTO();
        request.setIdCliente(11);
        request.setIdHabitacion(21);
        request.setFechaInicio(LocalDate.of(2026, 8, 1));
        request.setFechaFin(LocalDate.of(2026, 8, 5));
        request.setEstadoReserva("FINALIZADA");
        return request;
    }

    // Fixtures de respuestas externas usadas por los Feign Clients simulados.
    private ClienteResponseDTO crearCliente(Integer idCliente) {
        return ClienteResponseDTO.builder()
                .idCliente(idCliente)
                .nombres("Ana")
                .apellidos("Perez")
                .build();
    }

    private HabitacionResponseDTO crearHabitacion(Integer idHabitacion) {
        return HabitacionResponseDTO.builder()
                .idHabitacion(idHabitacion)
                .numeroHabitacion("101")
                .estadoHabitacion("DISPONIBLE")
                .build();
    }
}
