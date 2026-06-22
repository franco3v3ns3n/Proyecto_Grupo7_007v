package com.veranum.evento.services;

import com.veranum.evento.clients.HotelClient;
import com.veranum.evento.dtos.request.CentroEventoRequestDTO;
import com.veranum.evento.dtos.response.CentroEventoResponseDTO;
import com.veranum.evento.dtos.response.HotelResponseDTO;
import com.veranum.evento.exceptions.ResourceNotFoundException;
import com.veranum.evento.models.CentroEventoModel;
import com.veranum.evento.repositories.CentroEventoRepository;
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
public class CentroEventoServiceTest {

    @Mock
    private CentroEventoRepository centroEventoRepository;

    @Mock
    private HotelClient hotelClient;

    @InjectMocks
    private CentroEventoService centroEventoService;

    private CentroEventoModel centroEvento;
    private CentroEventoRequestDTO request;

    @BeforeEach
    void setUp() {
        // Configura los objetos de ejemplo antes de cada prueba.
        centroEvento = crearCentroEventoModel();
        request = crearRequest();
    }

    @Test
    public void testObtenerCentrosEventos() {
        // Define el comportamiento del repositorio simulado.
        when(centroEventoRepository.findAll()).thenReturn(List.of(centroEvento));

        // Ejecuta el metodo del servicio.
        List<CentroEventoResponseDTO> centrosEventos = centroEventoService.obtenerCentrosEventos();

        // Verifica el resultado obtenido.
        assertNotNull(centrosEventos);
        assertEquals(1, centrosEventos.size());
        assertEquals(1, centrosEventos.get(0).getIdCentroEvento());
        verify(centroEventoRepository).findAll();
    }

    @Test
    public void testObtenerCentroEventoPorId() {
        // Define el comportamiento del repositorio simulado.
        when(centroEventoRepository.findById(1)).thenReturn(Optional.of(centroEvento));

        // Ejecuta el metodo del servicio.
        CentroEventoResponseDTO encontrado = centroEventoService.obtenerCentroEventoPorId(1);

        // Verifica el resultado obtenido.
        assertNotNull(encontrado);
        assertEquals(1, encontrado.getIdCentroEvento());
        assertEquals("ACTIVO", encontrado.getEstadoCentroEvento());
        verify(centroEventoRepository).findById(1);
    }

    @Test
    public void testObtenerCentrosEventosPorHotel() {
        // Simula la existencia del hotel y los centros de eventos asociados.
        when(hotelClient.obtenerHotelPorId(10)).thenReturn(crearHotel(10));
        when(centroEventoRepository.findByIdHotel(10)).thenReturn(List.of(centroEvento));

        // Ejecuta el metodo del servicio.
        List<CentroEventoResponseDTO> centrosEventos = centroEventoService.obtenerCentrosEventosPorHotel(10);

        // Verifica el resultado obtenido.
        assertNotNull(centrosEventos);
        assertEquals(1, centrosEventos.size());
        assertEquals(10, centrosEventos.get(0).getIdHotel());
        verify(hotelClient).obtenerHotelPorId(10);
        verify(centroEventoRepository).findByIdHotel(10);
    }

    @Test
    public void testObtenerCentrosEventosPorEstado() {
        // Define el comportamiento del repositorio simulado.
        when(centroEventoRepository.findByEstadoCentroEvento("ACTIVO")).thenReturn(List.of(centroEvento));

        // Ejecuta el metodo del servicio.
        List<CentroEventoResponseDTO> centrosEventos = centroEventoService.obtenerCentrosEventosPorEstado("ACTIVO");

        // Verifica el resultado obtenido.
        assertNotNull(centrosEventos);
        assertEquals(1, centrosEventos.size());
        assertEquals("ACTIVO", centrosEventos.get(0).getEstadoCentroEvento());
        verify(centroEventoRepository).findByEstadoCentroEvento("ACTIVO");
    }

    @Test
    public void testCrearCentroEvento() {
        // Simula las dependencias necesarias para crear el centro de evento.
        when(hotelClient.obtenerHotelPorId(10)).thenReturn(crearHotel(10));
        when(centroEventoRepository.save(any(CentroEventoModel.class))).thenAnswer(invocation -> {
            CentroEventoModel centroEvento = invocation.getArgument(0);
            centroEvento.setIdCentroEvento(1);
            return centroEvento;
        });

        // Ejecuta el metodo del servicio.
        CentroEventoResponseDTO guardado = centroEventoService.crearCentroEvento(request);

        // Verifica el resultado obtenido.
        assertNotNull(guardado);
        assertEquals(1, guardado.getIdCentroEvento());
        assertEquals("Salon Pacifico", guardado.getNombre());
        verify(hotelClient).obtenerHotelPorId(10);
        verify(centroEventoRepository).save(any(CentroEventoModel.class));
    }

    @Test
    public void testActualizarCentroEvento() {
        // Configura la solicitud actualizada y simula sus dependencias.
        request = crearRequestActualizado();
        when(centroEventoRepository.findById(1)).thenReturn(Optional.of(centroEvento));
        when(hotelClient.obtenerHotelPorId(11)).thenReturn(crearHotel(11));
        when(centroEventoRepository.save(centroEvento)).thenReturn(centroEvento);

        // Ejecuta el metodo del servicio.
        CentroEventoResponseDTO actualizado = centroEventoService.actualizarCentroEvento(1, request);

        // Verifica el resultado obtenido.
        assertNotNull(actualizado);
        assertEquals(11, actualizado.getIdHotel());
        assertEquals("INACTIVO", actualizado.getEstadoCentroEvento());
        verify(centroEventoRepository).findById(1);
        verify(hotelClient).obtenerHotelPorId(11);
        verify(centroEventoRepository).save(centroEvento);
    }

    @Test
    public void testEliminarCentroEvento() {
        // Simula la busqueda del centro de evento que sera eliminado.
        when(centroEventoRepository.findById(1)).thenReturn(Optional.of(centroEvento));

        // Ejecuta el metodo del servicio.
        centroEventoService.eliminarCentroEvento(1);

        // Verifica que el repositorio elimine el centro de evento encontrado.
        verify(centroEventoRepository).findById(1);
        verify(centroEventoRepository).delete(centroEvento);
    }

    @Test
    public void testObtenerCentroEventoPorIdInexistente() {
        // Simula que el centro de evento solicitado no existe.
        when(centroEventoRepository.findById(99)).thenReturn(Optional.empty());

        // Ejecuta el metodo y captura la excepcion esperada.
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> centroEventoService.obtenerCentroEventoPorId(99)
        );

        // Verifica el mensaje de la excepcion.
        assertEquals("Centro de evento no encontrado", exception.getMessage());
        verify(centroEventoRepository).findById(99);
    }

    private CentroEventoModel crearCentroEventoModel() {
        return CentroEventoModel.builder()
                .idCentroEvento(1)
                .idHotel(10)
                .nombre("Salon Pacifico")
                .capacidadPersonas(150)
                .precioCentroEvento(350000.0)
                .estadoCentroEvento("ACTIVO")
                .build();
    }

    private CentroEventoRequestDTO crearRequest() {
        CentroEventoRequestDTO request = new CentroEventoRequestDTO();
        request.setIdHotel(10);
        request.setNombre("Salon Pacifico");
        request.setCapacidadPersonas(150);
        request.setPrecioCentroEvento(350000.0);
        request.setEstadoCentroEvento("ACTIVO");
        return request;
    }

    private CentroEventoRequestDTO crearRequestActualizado() {
        CentroEventoRequestDTO request = new CentroEventoRequestDTO();
        request.setIdHotel(11);
        request.setNombre("Salon Vista Mar");
        request.setCapacidadPersonas(120);
        request.setPrecioCentroEvento(280000.0);
        request.setEstadoCentroEvento("INACTIVO");
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
