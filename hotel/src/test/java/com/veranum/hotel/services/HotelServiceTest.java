package com.veranum.hotel.services;

import com.veranum.hotel.dtos.request.HotelRequestDTO;
import com.veranum.hotel.dtos.response.HotelResponseDTO;
import com.veranum.hotel.exceptions.ResourceNotFoundException;
import com.veranum.hotel.models.HotelModel;
import com.veranum.hotel.repositories.HotelRepository;
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
public class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    private HotelModel hotel;
    private HotelRequestDTO request;

    @BeforeEach
    void setUp() {
        // Configura los objetos de ejemplo antes de cada prueba.
        hotel = crearHotelModel();
        request = crearRequest();
    }

    @Test
    public void testObtenerHoteles() {
        // Define el comportamiento del repositorio simulado.
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));

        // Ejecuta el metodo del servicio.
        List<HotelResponseDTO> hoteles = hotelService.obtenerHoteles();

        // Verifica el resultado obtenido.
        assertNotNull(hoteles);
        assertEquals(1, hoteles.size());
        assertEquals(1, hoteles.get(0).getIdHotel());
        verify(hotelRepository).findAll();
    }

    @Test
    public void testObtenerHotelPorId() {
        // Define el comportamiento del repositorio simulado.
        when(hotelRepository.findById(1)).thenReturn(Optional.of(hotel));

        // Ejecuta el metodo del servicio.
        HotelResponseDTO encontrado = hotelService.obtenerHotelPorId(1);

        // Verifica el resultado obtenido.
        assertNotNull(encontrado);
        assertEquals(1, encontrado.getIdHotel());
        assertEquals("Hotel Veranum Santiago", encontrado.getNombre());
        verify(hotelRepository).findById(1);
    }

    @Test
    public void testCrearHotel() {
        // Simula el guardado del hotel con id generado.
        when(hotelRepository.save(any(HotelModel.class))).thenAnswer(invocation -> {
            HotelModel hotel = invocation.getArgument(0);
            hotel.setIdHotel(1);
            return hotel;
        });

        // Ejecuta el metodo del servicio.
        HotelResponseDTO guardado = hotelService.crearHotel(request);

        // Verifica el resultado obtenido.
        assertNotNull(guardado);
        assertEquals(1, guardado.getIdHotel());
        assertEquals("Hotel Veranum Santiago", guardado.getNombre());
        verify(hotelRepository).save(any(HotelModel.class));
    }

    @Test
    public void testActualizarHotel() {
        // Configura la solicitud actualizada y simula sus dependencias.
        request = crearRequestActualizado();
        when(hotelRepository.findById(1)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        // Ejecuta el metodo del servicio.
        HotelResponseDTO actualizado = hotelService.actualizarHotel(1, request);

        // Verifica el resultado obtenido.
        assertNotNull(actualizado);
        assertEquals("Hotel Veranum Valparaiso", actualizado.getNombre());
        assertEquals("Valparaiso", actualizado.getUbicacion());
        verify(hotelRepository).findById(1);
        verify(hotelRepository).save(hotel);
    }

    @Test
    public void testEliminarHotel() {
        // Simula la busqueda del hotel que sera eliminado.
        when(hotelRepository.findById(1)).thenReturn(Optional.of(hotel));

        // Ejecuta el metodo del servicio.
        hotelService.eliminarHotel(1);

        // Verifica que el repositorio elimine el hotel encontrado.
        verify(hotelRepository).findById(1);
        verify(hotelRepository).delete(hotel);
    }

    @Test
    public void testObtenerHotelPorIdInexistente() {
        // Simula que el hotel solicitado no existe.
        when(hotelRepository.findById(99)).thenReturn(Optional.empty());

        // Ejecuta el metodo y captura la excepcion esperada.
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> hotelService.obtenerHotelPorId(99)
        );

        // Verifica el mensaje de la excepcion.
        assertEquals("Hotel no encontrado", exception.getMessage());
        verify(hotelRepository).findById(99);
    }

    private HotelModel crearHotelModel() {
        return HotelModel.builder()
                .idHotel(1)
                .nombre("Hotel Veranum Santiago")
                .ubicacion("Santiago")
                .build();
    }

    private HotelRequestDTO crearRequest() {
        HotelRequestDTO request = new HotelRequestDTO();
        request.setNombre("Hotel Veranum Santiago");
        request.setUbicacion("Santiago");
        return request;
    }

    private HotelRequestDTO crearRequestActualizado() {
        HotelRequestDTO request = new HotelRequestDTO();
        request.setNombre("Hotel Veranum Valparaiso");
        request.setUbicacion("Valparaiso");
        return request;
    }
}
