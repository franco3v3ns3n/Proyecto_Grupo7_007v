package com.veranum.habitacion.services;

import com.veranum.habitacion.clients.HotelClient;
import com.veranum.habitacion.dtos.request.HabitacionRequestDTO;
import com.veranum.habitacion.dtos.response.HabitacionResponseDTO;
import com.veranum.habitacion.dtos.response.HotelResponseDTO;
import com.veranum.habitacion.exceptions.ResourceNotFoundException;
import com.veranum.habitacion.models.HabitacionModel;
import com.veranum.habitacion.repositories.HabitacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HabitacionServiceTest {

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private HotelClient hotelClient;

    @InjectMocks
    private HabitacionService habitacionService;

    private HabitacionModel habitacion;
    private HabitacionRequestDTO request;

    @BeforeEach
    void setUp() {
        // Configura los objetos de ejemplo antes de cada prueba.
        habitacion = crearHabitacionModel();
        request = crearRequest();
    }

    @Test
    public void testObtenerHabitaciones() {
        // Define el comportamiento del repositorio simulado.
        when(habitacionRepository.findAll()).thenReturn(List.of(habitacion));

        // Ejecuta el metodo del servicio.
        List<HabitacionResponseDTO> habitaciones = habitacionService.obtenerHabitaciones();

        // Verifica el resultado obtenido.
        assertNotNull(habitaciones);
        assertEquals(1, habitaciones.size());
        assertEquals(1, habitaciones.get(0).getIdHabitacion());
        verify(habitacionRepository).findAll();
    }

    @Test
    public void testObtenerHabitacionPorId() {
        // Define el comportamiento del repositorio simulado.
        when(habitacionRepository.findById(1)).thenReturn(Optional.of(habitacion));

        // Ejecuta el metodo del servicio.
        HabitacionResponseDTO encontrada = habitacionService.obtenerHabitacionPorId(1);

        // Verifica el resultado obtenido.
        assertNotNull(encontrada);
        assertEquals(1, encontrada.getIdHabitacion());
        assertEquals("DISPONIBLE", encontrada.getEstadoHabitacion());
        verify(habitacionRepository).findById(1);
    }

    @Test
    public void testObtenerHabitacionesPorHotel() {
        // Simula la existencia del hotel y las habitaciones asociadas.
        when(hotelClient.obtenerHotelPorId(10)).thenReturn(crearHotel(10));
        when(habitacionRepository.findByIdHotel(10)).thenReturn(List.of(habitacion));

        // Ejecuta el metodo del servicio.
        List<HabitacionResponseDTO> habitaciones = habitacionService.obtenerHabitacionesPorHotel(10);

        // Verifica el resultado obtenido.
        assertNotNull(habitaciones);
        assertEquals(1, habitaciones.size());
        assertEquals(10, habitaciones.get(0).getIdHotel());
        verify(hotelClient).obtenerHotelPorId(10);
        verify(habitacionRepository).findByIdHotel(10);
    }

    @Test
    public void testObtenerHabitacionesPorHotelYTipo() {
        // Simula la existencia del hotel y las habitaciones asociadas por tipo.
        when(hotelClient.obtenerHotelPorId(10)).thenReturn(crearHotel(10));
        when(habitacionRepository.findByIdHotelAndTipoHabitacion(10, "Simple"))
                .thenReturn(List.of(habitacion));

        // Ejecuta el metodo del servicio.
        List<HabitacionResponseDTO> habitaciones =
                habitacionService.obtenerHabitacionesPorHotelYTipo(10, "Simple");

        // Verifica el resultado obtenido.
        assertNotNull(habitaciones);
        assertEquals(1, habitaciones.size());
        assertEquals("Simple", habitaciones.get(0).getTipoHabitacion());
        verify(hotelClient).obtenerHotelPorId(10);
        verify(habitacionRepository).findByIdHotelAndTipoHabitacion(10, "Simple");
    }

    @Test
    public void testObtenerHabitacionesPorHotelYEstado() {
        // Simula la existencia del hotel y las habitaciones asociadas por estado.
        when(hotelClient.obtenerHotelPorId(10)).thenReturn(crearHotel(10));
        when(habitacionRepository.findByIdHotelAndEstadoHabitacion(10, "DISPONIBLE"))
                .thenReturn(List.of(habitacion));

        // Ejecuta el metodo del servicio.
        List<HabitacionResponseDTO> habitaciones =
                habitacionService.obtenerHabitacionesPorHotelYEstado(10, "DISPONIBLE");

        // Verifica el resultado obtenido.
        assertNotNull(habitaciones);
        assertEquals(1, habitaciones.size());
        assertEquals("DISPONIBLE", habitaciones.get(0).getEstadoHabitacion());
        verify(hotelClient).obtenerHotelPorId(10);
        verify(habitacionRepository).findByIdHotelAndEstadoHabitacion(10, "DISPONIBLE");
    }

    @Test
    public void testCrearHabitacion() {
        // Simula las dependencias necesarias para crear la habitacion.
        when(hotelClient.obtenerHotelPorId(10)).thenReturn(crearHotel(10));
        when(habitacionRepository.save(any(HabitacionModel.class))).thenAnswer(invocation -> {
            HabitacionModel habitacion = invocation.getArgument(0);
            habitacion.setIdHabitacion(1);
            return habitacion;
        });

        // Ejecuta el metodo del servicio.
        HabitacionResponseDTO guardada = habitacionService.crearHabitacion(request);

        // Verifica el resultado obtenido.
        assertNotNull(guardada);
        assertEquals(1, guardada.getIdHabitacion());
        assertEquals("101", guardada.getNumeroHabitacion());
        verify(hotelClient).obtenerHotelPorId(10);
        verify(habitacionRepository).save(any(HabitacionModel.class));
    }

    @Test
    public void testActualizarHabitacion() {
        // Configura la solicitud actualizada y simula sus dependencias.
        request = crearRequestActualizado();
        when(habitacionRepository.findById(1)).thenReturn(Optional.of(habitacion));
        when(hotelClient.obtenerHotelPorId(11)).thenReturn(crearHotel(11));
        when(habitacionRepository.save(habitacion)).thenReturn(habitacion);

        // Ejecuta el metodo del servicio.
        HabitacionResponseDTO actualizada = habitacionService.actualizarHabitacion(1, request);

        // Verifica el resultado obtenido.
        assertNotNull(actualizada);
        assertEquals(11, actualizada.getIdHotel());
        assertEquals("OCUPADA", actualizada.getEstadoHabitacion());
        verify(habitacionRepository).findById(1);
        verify(hotelClient).obtenerHotelPorId(11);
        verify(habitacionRepository).save(habitacion);
    }

    @Test
    public void testEliminarHabitacion() {
        // Simula la busqueda de la habitacion que sera eliminada.
        when(habitacionRepository.findById(1)).thenReturn(Optional.of(habitacion));

        // Ejecuta el metodo del servicio.
        habitacionService.eliminarHabitacion(1);

        // Verifica que el repositorio elimine la habitacion encontrada.
        verify(habitacionRepository).findById(1);
        verify(habitacionRepository).delete(habitacion);
    }

    @Test
    public void testObtenerHabitacionPorIdInexistente() {
        // Simula que la habitacion solicitada no existe.
        when(habitacionRepository.findById(99)).thenReturn(Optional.empty());

        // Ejecuta el metodo y captura la excepcion esperada.
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> habitacionService.obtenerHabitacionPorId(99)
        );

        // Verifica el mensaje de la excepcion.
        assertEquals("Habitaci\u00f3n no encontrada con id: 99", exception.getMessage());
        verify(habitacionRepository).findById(99);
    }

    private HabitacionModel crearHabitacionModel() {
        return HabitacionModel.builder()
                .idHabitacion(1)
                .idHotel(10)
                .tipoHabitacion("Simple")
                .numeroHabitacion("101")
                .capacidadPersonas(1)
                .cantidadCamas(1)
                .cantidadBanos(1)
                .precioDiario(45000.0)
                .estadoHabitacion("DISPONIBLE")
                .build();
    }

    private HabitacionRequestDTO crearRequest() {
        HabitacionRequestDTO request = new HabitacionRequestDTO();
        request.setIdHotel(10);
        request.setTipoHabitacion("Simple");
        request.setNumeroHabitacion("101");
        request.setCapacidadPersonas(1);
        request.setCantidadCamas(1);
        request.setCantidadBanos(1);
        request.setPrecioDiario(45000.0);
        request.setEstadoHabitacion("DISPONIBLE");
        return request;
    }

    private HabitacionRequestDTO crearRequestActualizado() {
        HabitacionRequestDTO request = new HabitacionRequestDTO();
        request.setIdHotel(11);
        request.setTipoHabitacion("Doble");
        request.setNumeroHabitacion("102");
        request.setCapacidadPersonas(2);
        request.setCantidadCamas(2);
        request.setCantidadBanos(1);
        request.setPrecioDiario(65000.0);
        request.setEstadoHabitacion("OCUPADA");
        return request;
    }

    private HotelResponseDTO crearHotel(Integer idHotel) {
        return HotelResponseDTO.builder()
                .idHotel(idHotel)
                .nombre("Hotel Veranum")
                .ubicacion("Santiago")
                .build();
    }
}
