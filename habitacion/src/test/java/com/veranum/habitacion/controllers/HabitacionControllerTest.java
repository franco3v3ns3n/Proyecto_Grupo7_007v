package com.veranum.habitacion.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veranum.habitacion.dtos.request.HabitacionRequestDTO;
import com.veranum.habitacion.dtos.response.HabitacionResponseDTO;
import com.veranum.habitacion.services.HabitacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HabitacionController.class) // Indica que se esta probando el controlador de Habitacion
@TestPropertySource(properties = "HOTEL_SERVICE_URL=http://localhost")
public class HabitacionControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite realizar peticiones HTTP simuladas

    @MockBean
    private HabitacionService habitacionService; // Crea un mock del servicio de Habitacion

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos Java a JSON

    private HabitacionRequestDTO request;
    private HabitacionResponseDTO response;

    @BeforeEach
    void setUp() {
        // Configura la solicitud y la respuesta de ejemplo antes de cada prueba.
        request = new HabitacionRequestDTO();
        request.setIdHotel(10);
        request.setTipoHabitacion("Simple");
        request.setNumeroHabitacion("101");
        request.setCapacidadPersonas(1);
        request.setCantidadCamas(1);
        request.setCantidadBanos(1);
        request.setPrecioDiario(45000.0);
        request.setEstadoHabitacion("DISPONIBLE");

        response = HabitacionResponseDTO.builder()
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

    @Test
    public void testGetAllHabitaciones() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(habitacionService.obtenerHabitaciones()).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/habitaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idHabitacion").value(1))
                .andExpect(jsonPath("$[0].idHotel").value(10))
                .andExpect(jsonPath("$[0].estadoHabitacion").value("DISPONIBLE"));

        verify(habitacionService).obtenerHabitaciones();
    }

    @Test
    public void testGetHabitacionById() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(habitacionService.obtenerHabitacionPorId(1)).thenReturn(response);

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/habitaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idHabitacion").value(1))
                .andExpect(jsonPath("$.numeroHabitacion").value("101"))
                .andExpect(jsonPath("$.estadoHabitacion").value("DISPONIBLE"));

        verify(habitacionService).obtenerHabitacionPorId(1);
    }

    @Test
    public void testGetHabitacionesByHotel() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(habitacionService.obtenerHabitacionesPorHotel(10)).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/habitaciones/hotel/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idHabitacion").value(1))
                .andExpect(jsonPath("$[0].idHotel").value(10));

        verify(habitacionService).obtenerHabitacionesPorHotel(10);
    }

    @Test
    public void testGetHabitacionesByHotelAndTipo() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(habitacionService.obtenerHabitacionesPorHotelYTipo(10, "Simple"))
                .thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/habitaciones/hotel/10/tipo/Simple"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idHabitacion").value(1))
                .andExpect(jsonPath("$[0].tipoHabitacion").value("Simple"));

        verify(habitacionService).obtenerHabitacionesPorHotelYTipo(10, "Simple");
    }

    @Test
    public void testGetHabitacionesByHotelAndEstado() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(habitacionService.obtenerHabitacionesPorHotelYEstado(10, "DISPONIBLE"))
                .thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/habitaciones/hotel/10/estado/DISPONIBLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idHabitacion").value(1))
                .andExpect(jsonPath("$[0].estadoHabitacion").value("DISPONIBLE"));

        verify(habitacionService).obtenerHabitacionesPorHotelYEstado(10, "DISPONIBLE");
    }

    @Test
    public void testCreateHabitacion() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(habitacionService.crearHabitacion(any(HabitacionRequestDTO.class))).thenReturn(response);

        // Realiza la peticion POST y verifica la respuesta.
        mockMvc.perform(post("/api/v1/habitaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idHabitacion").value(1))
                .andExpect(jsonPath("$.idHotel").value(10))
                .andExpect(jsonPath("$.estadoHabitacion").value("DISPONIBLE"));

        verify(habitacionService).crearHabitacion(any(HabitacionRequestDTO.class));
    }

    @Test
    public void testUpdateHabitacion() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(habitacionService.actualizarHabitacion(any(Integer.class), any(HabitacionRequestDTO.class)))
                .thenReturn(response);

        // Realiza la peticion PUT y verifica la respuesta.
        mockMvc.perform(put("/api/v1/habitaciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idHabitacion").value(1))
                .andExpect(jsonPath("$.numeroHabitacion").value("101"))
                .andExpect(jsonPath("$.estadoHabitacion").value("DISPONIBLE"));

        verify(habitacionService).actualizarHabitacion(any(Integer.class), any(HabitacionRequestDTO.class));
    }

    @Test
    public void testDeleteHabitacion() throws Exception {
        // Define el comportamiento del servicio simulado.
        doNothing().when(habitacionService).eliminarHabitacion(1);

        // Realiza la peticion DELETE y verifica la respuesta.
        mockMvc.perform(delete("/api/v1/habitaciones/1"))
                .andExpect(status().isNoContent());

        verify(habitacionService, times(1)).eliminarHabitacion(1);
    }

    @Test
    public void testCreateHabitacionConEstadoInvalido() throws Exception {
        // Configura una solicitud que no cumple la validacion de estado.
        request.setEstadoHabitacion("BLOQUEADA");

        // Realiza la peticion POST y verifica el error de validacion.
        mockMvc.perform(post("/api/v1/habitaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.estadoHabitacion")
                        .value("El estado debe ser DISPONIBLE, OCUPADA o MANTENIMIENTO"));

        verify(habitacionService, never()).crearHabitacion(any(HabitacionRequestDTO.class));
    }
}
