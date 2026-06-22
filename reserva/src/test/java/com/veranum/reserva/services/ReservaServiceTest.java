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

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    private static final LocalDate FECHA_INICIO = LocalDate.of(2026, 7, 20);
    private static final LocalDate FECHA_FIN = LocalDate.of(2026, 7, 25);
    private static final LocalDateTime FECHA_CREACION = LocalDateTime.of(2026, 6, 20, 10, 30);

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ClienteClient clienteClient;

    @Mock
    private HabitacionClient habitacionClient;

    @InjectMocks
    private ReservaService reservaService;

    private ReservaModel reserva;
    private ReservaRequestDTO request;

    @BeforeEach
    void setUp() {
        // Configura los objetos de ejemplo antes de cada prueba.
        reserva = crearReservaModel();
        request = crearRequest();
    }

    @Test
    public void testObtenerReservas() {
        // Define el comportamiento del repositorio simulado.
        when(reservaRepository.findAll()).thenReturn(List.of(reserva));

        // Ejecuta el metodo del servicio.
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservas();

        // Verifica el resultado obtenido.
        assertNotNull(reservas);
        assertEquals(1, reservas.size());
        assertEquals(1, reservas.get(0).getIdReserva());
        verify(reservaRepository).findAll();
    }

    @Test
    public void testObtenerReservaPorId() {
        // Define el comportamiento del repositorio simulado.
        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));

        // Ejecuta el metodo del servicio.
        ReservaResponseDTO encontrada = reservaService.obtenerReservaPorId(1);

        // Verifica el resultado obtenido.
        assertNotNull(encontrada);
        assertEquals(1, encontrada.getIdReserva());
        assertEquals("CONFIRMADA", encontrada.getEstadoReserva());
        verify(reservaRepository).findById(1);
    }

    @Test
    public void testObtenerReservasPorCliente() {
        // Simula la existencia del cliente y las reservas asociadas.
        when(clienteClient.obtenerClientePorId(10)).thenReturn(crearCliente(10));
        when(reservaRepository.findByIdCliente(10)).thenReturn(List.of(reserva));

        // Ejecuta el metodo del servicio.
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorCliente(10);

        // Verifica el resultado obtenido.
        assertNotNull(reservas);
        assertEquals(1, reservas.size());
        assertEquals(10, reservas.get(0).getIdCliente());
        verify(clienteClient).obtenerClientePorId(10);
        verify(reservaRepository).findByIdCliente(10);
    }

    @Test
    public void testObtenerReservasPorHabitacion() {
        // Simula la existencia de la habitacion y sus reservas asociadas.
        when(habitacionClient.obtenerHabitacionPorId(20)).thenReturn(crearHabitacion(20));
        when(reservaRepository.findByIdHabitacion(20)).thenReturn(List.of(reserva));

        // Ejecuta el metodo del servicio.
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorHabitacion(20);

        // Verifica el resultado obtenido.
        assertNotNull(reservas);
        assertEquals(1, reservas.size());
        assertEquals(20, reservas.get(0).getIdHabitacion());
        verify(habitacionClient).obtenerHabitacionPorId(20);
        verify(reservaRepository).findByIdHabitacion(20);
    }

    @Test
    public void testObtenerReservasPorEstado() {
        // Define el comportamiento del repositorio simulado.
        when(reservaRepository.findByEstadoReserva("CONFIRMADA")).thenReturn(List.of(reserva));

        // Ejecuta el metodo del servicio.
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorEstado("CONFIRMADA");

        // Verifica el resultado obtenido.
        assertNotNull(reservas);
        assertEquals(1, reservas.size());
        assertEquals("CONFIRMADA", reservas.get(0).getEstadoReserva());
        verify(reservaRepository).findByEstadoReserva("CONFIRMADA");
    }

    @Test
    public void testCrearReserva() {
        // Simula las dependencias necesarias para crear la reserva.
        when(clienteClient.obtenerClientePorId(10)).thenReturn(crearCliente(10));
        when(habitacionClient.obtenerHabitacionPorId(20)).thenReturn(crearHabitacion(20));
        when(reservaRepository.save(any(ReservaModel.class))).thenAnswer(invocation -> {
            ReservaModel reserva = invocation.getArgument(0);
            reserva.setIdReserva(1);
            return reserva;
        });

        // Ejecuta el metodo del servicio.
        ReservaResponseDTO guardada = reservaService.crearReserva(request);

        // Verifica el resultado obtenido.
        assertNotNull(guardada);
        assertEquals(1, guardada.getIdReserva());
        assertNotNull(guardada.getFechaCreacion());
        verify(clienteClient).obtenerClientePorId(10);
        verify(habitacionClient).obtenerHabitacionPorId(20);
        verify(reservaRepository).save(any(ReservaModel.class));
    }

    @Test
    public void testActualizarReserva() {
        // Configura la solicitud actualizada y simula sus dependencias.
        request = crearRequestActualizado();
        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));
        when(clienteClient.obtenerClientePorId(11)).thenReturn(crearCliente(11));
        when(habitacionClient.obtenerHabitacionPorId(21)).thenReturn(crearHabitacion(21));
        when(reservaRepository.save(reserva)).thenReturn(reserva);

        // Ejecuta el metodo del servicio.
        ReservaResponseDTO actualizada = reservaService.actualizarReserva(1, request);

        // Verifica el resultado obtenido.
        assertNotNull(actualizada);
        assertEquals(11, actualizada.getIdCliente());
        assertEquals("FINALIZADA", actualizada.getEstadoReserva());
        verify(reservaRepository).findById(1);
        verify(reservaRepository).save(reserva);
    }

    @Test
    public void testEliminarReserva() {
        // Simula la busqueda de la reserva que sera eliminada.
        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));

        // Ejecuta el metodo del servicio.
        reservaService.eliminarReserva(1);

        // Verifica que el repositorio elimine la reserva encontrada.
        verify(reservaRepository).findById(1);
        verify(reservaRepository).delete(reserva);
    }

    @Test
    public void testObtenerReservaPorIdInexistente() {
        // Simula que la reserva solicitada no existe.
        when(reservaRepository.findById(99)).thenReturn(Optional.empty());

        // Ejecuta el metodo y captura la excepcion esperada.
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reservaService.obtenerReservaPorId(99)
        );

        // Verifica el mensaje de la excepcion.
        assertEquals("Reserva no encontrada", exception.getMessage());
        verify(reservaRepository).findById(99);
    }

    @Test
    public void testCrearReservaConFechasIguales() {
        // Configura una solicitud con fechas incoherentes.
        request.setFechaFin(FECHA_INICIO);

        // Ejecuta el metodo y captura la excepcion esperada.
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> reservaService.crearReserva(request)
        );

        // Verifica la regla de negocio y que no se invoquen dependencias posteriores.
        assertEquals("La fecha de fin debe ser posterior a la fecha de inicio", exception.getMessage());
        verify(reservaRepository, never()).save(any(ReservaModel.class));
        verify(clienteClient, never()).obtenerClientePorId(anyInt());
        verify(habitacionClient, never()).obtenerHabitacionPorId(anyInt());
    }

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
