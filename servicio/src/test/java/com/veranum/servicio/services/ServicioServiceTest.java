package com.veranum.servicio.services;

import com.veranum.servicio.clients.HotelClient;
import com.veranum.servicio.dtos.request.ServicioRequestDTO;
import com.veranum.servicio.dtos.response.HotelResponseDTO;
import com.veranum.servicio.dtos.response.ServicioResponseDTO;
import com.veranum.servicio.exceptions.ResourceNotFoundException;
import com.veranum.servicio.models.ServicioModel;
import com.veranum.servicio.repositories.ServicioRepository;
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
public class ServicioServiceTest {

    @Mock
    private ServicioRepository servicioRepository;

    @Mock
    private HotelClient hotelClient;

    @InjectMocks
    private ServicioService servicioService;

    private ServicioModel servicio;
    private ServicioRequestDTO request;

    @BeforeEach
    void setUp() {
        // Configura los objetos de ejemplo antes de cada prueba.
        servicio = crearServicioModel();
        request = crearRequest();
    }

    @Test
    public void testObtenerServicios() {
        // Define el comportamiento del repositorio simulado.
        when(servicioRepository.findAll()).thenReturn(List.of(servicio));

        // Ejecuta el metodo del servicio.
        List<ServicioResponseDTO> servicios = servicioService.obtenerServicios();

        // Verifica el resultado obtenido.
        assertNotNull(servicios);
        assertEquals(1, servicios.size());
        assertEquals(1, servicios.get(0).getIdServicio());
        verify(servicioRepository).findAll();
    }

    @Test
    public void testObtenerServicioPorId() {
        // Define el comportamiento del repositorio simulado.
        when(servicioRepository.findById(1)).thenReturn(Optional.of(servicio));

        // Ejecuta el metodo del servicio.
        ServicioResponseDTO encontrado = servicioService.obtenerServicioPorId(1);

        // Verifica el resultado obtenido.
        assertNotNull(encontrado);
        assertEquals(1, encontrado.getIdServicio());
        assertEquals("ACTIVO", encontrado.getEstadoServicio());
        verify(servicioRepository).findById(1);
    }

    @Test
    public void testObtenerServiciosPorHotel() {
        // Simula la existencia del hotel y los servicios asociados.
        when(hotelClient.obtenerHotelPorId(10)).thenReturn(crearHotel(10));
        when(servicioRepository.findByIdHotel(10)).thenReturn(List.of(servicio));

        // Ejecuta el metodo del servicio.
        List<ServicioResponseDTO> servicios = servicioService.obtenerServiciosPorHotel(10);

        // Verifica el resultado obtenido.
        assertNotNull(servicios);
        assertEquals(1, servicios.size());
        assertEquals(10, servicios.get(0).getIdHotel());
        verify(hotelClient).obtenerHotelPorId(10);
        verify(servicioRepository).findByIdHotel(10);
    }

    @Test
    public void testObtenerServiciosPorEstado() {
        // Define el comportamiento del repositorio simulado.
        when(servicioRepository.findByEstadoServicio("ACTIVO")).thenReturn(List.of(servicio));

        // Ejecuta el metodo del servicio.
        List<ServicioResponseDTO> servicios = servicioService.obtenerServiciosPorEstado("ACTIVO");

        // Verifica el resultado obtenido.
        assertNotNull(servicios);
        assertEquals(1, servicios.size());
        assertEquals("ACTIVO", servicios.get(0).getEstadoServicio());
        verify(servicioRepository).findByEstadoServicio("ACTIVO");
    }

    @Test
    public void testCrearServicio() {
        // Simula las dependencias necesarias para crear el servicio.
        when(hotelClient.obtenerHotelPorId(10)).thenReturn(crearHotel(10));
        when(servicioRepository.save(any(ServicioModel.class))).thenAnswer(invocation -> {
            ServicioModel servicio = invocation.getArgument(0);
            servicio.setIdServicio(1);
            return servicio;
        });

        // Ejecuta el metodo del servicio.
        ServicioResponseDTO guardado = servicioService.crearServicio(request);

        // Verifica el resultado obtenido.
        assertNotNull(guardado);
        assertEquals(1, guardado.getIdServicio());
        assertEquals("WiFi Premium", guardado.getNombre());
        verify(hotelClient).obtenerHotelPorId(10);
        verify(servicioRepository).save(any(ServicioModel.class));
    }

    @Test
    public void testActualizarServicio() {
        // Configura la solicitud actualizada y simula sus dependencias.
        request = crearRequestActualizado();
        when(servicioRepository.findById(1)).thenReturn(Optional.of(servicio));
        when(hotelClient.obtenerHotelPorId(11)).thenReturn(crearHotel(11));
        when(servicioRepository.save(servicio)).thenReturn(servicio);

        // Ejecuta el metodo del servicio.
        ServicioResponseDTO actualizado = servicioService.actualizarServicio(1, request);

        // Verifica el resultado obtenido.
        assertNotNull(actualizado);
        assertEquals(11, actualizado.getIdHotel());
        assertEquals("INACTIVO", actualizado.getEstadoServicio());
        verify(servicioRepository).findById(1);
        verify(hotelClient).obtenerHotelPorId(11);
        verify(servicioRepository).save(servicio);
    }

    @Test
    public void testEliminarServicio() {
        // Simula la busqueda del servicio que sera eliminado.
        when(servicioRepository.findById(1)).thenReturn(Optional.of(servicio));

        // Ejecuta el metodo del servicio.
        servicioService.eliminarServicio(1);

        // Verifica que el repositorio elimine el servicio encontrado.
        verify(servicioRepository).findById(1);
        verify(servicioRepository).delete(servicio);
    }

    @Test
    public void testObtenerServicioPorIdInexistente() {
        // Simula que el servicio solicitado no existe.
        when(servicioRepository.findById(99)).thenReturn(Optional.empty());

        // Ejecuta el metodo y captura la excepcion esperada.
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> servicioService.obtenerServicioPorId(99)
        );

        // Verifica el mensaje de la excepcion.
        assertEquals("Servicio no encontrado", exception.getMessage());
        verify(servicioRepository).findById(99);
    }

    private ServicioModel crearServicioModel() {
        return ServicioModel.builder()
                .idServicio(1)
                .idHotel(10)
                .nombre("WiFi Premium")
                .valorDiario(5000.0)
                .estadoServicio("ACTIVO")
                .build();
    }

    private ServicioRequestDTO crearRequest() {
        ServicioRequestDTO request = new ServicioRequestDTO();
        request.setIdHotel(10);
        request.setNombre("WiFi Premium");
        request.setValorDiario(5000.0);
        request.setEstadoServicio("ACTIVO");
        return request;
    }

    private ServicioRequestDTO crearRequestActualizado() {
        ServicioRequestDTO request = new ServicioRequestDTO();
        request.setIdHotel(11);
        request.setNombre("Lavanderia");
        request.setValorDiario(8000.0);
        request.setEstadoServicio("INACTIVO");
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
