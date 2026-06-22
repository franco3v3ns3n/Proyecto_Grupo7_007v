package com.veranum.hotel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veranum.hotel.dtos.request.HotelRequestDTO;
import com.veranum.hotel.dtos.response.HotelResponseDTO;
import com.veranum.hotel.services.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelController.class) // Indica que se esta probando el controlador de Hotel
public class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite realizar peticiones HTTP simuladas

    @MockBean
    private HotelService hotelService; // Crea un mock del servicio de Hotel

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos Java a JSON

    private HotelRequestDTO request;
    private HotelResponseDTO response;

    @BeforeEach
    void setUp() {
        // Configura la solicitud y la respuesta de ejemplo antes de cada prueba.
        request = new HotelRequestDTO();
        request.setNombre("Hotel Veranum Santiago");
        request.setUbicacion("Santiago");

        response = HotelResponseDTO.builder()
                .idHotel(1)
                .nombre("Hotel Veranum Santiago")
                .ubicacion("Santiago")
                .build();
    }

    @Test
    public void testGetAllHoteles() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(hotelService.obtenerHoteles()).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/hoteles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idHotel").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Hotel Veranum Santiago"))
                .andExpect(jsonPath("$[0].ubicacion").value("Santiago"));

        verify(hotelService).obtenerHoteles();
    }

    @Test
    public void testGetHotelById() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(hotelService.obtenerHotelPorId(1)).thenReturn(response);

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/hoteles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idHotel").value(1))
                .andExpect(jsonPath("$.nombre").value("Hotel Veranum Santiago"))
                .andExpect(jsonPath("$.ubicacion").value("Santiago"));

        verify(hotelService).obtenerHotelPorId(1);
    }

    @Test
    public void testCreateHotel() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(hotelService.crearHotel(any(HotelRequestDTO.class))).thenReturn(response);

        // Realiza la peticion POST y verifica la respuesta.
        mockMvc.perform(post("/api/v1/hoteles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idHotel").value(1))
                .andExpect(jsonPath("$.nombre").value("Hotel Veranum Santiago"))
                .andExpect(jsonPath("$.ubicacion").value("Santiago"));

        verify(hotelService).crearHotel(any(HotelRequestDTO.class));
    }

    @Test
    public void testUpdateHotel() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(hotelService.actualizarHotel(any(Integer.class), any(HotelRequestDTO.class)))
                .thenReturn(response);

        // Realiza la peticion PUT y verifica la respuesta.
        mockMvc.perform(put("/api/v1/hoteles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idHotel").value(1))
                .andExpect(jsonPath("$.nombre").value("Hotel Veranum Santiago"))
                .andExpect(jsonPath("$.ubicacion").value("Santiago"));

        verify(hotelService).actualizarHotel(any(Integer.class), any(HotelRequestDTO.class));
    }

    @Test
    public void testDeleteHotel() throws Exception {
        // Define el comportamiento del servicio simulado.
        doNothing().when(hotelService).eliminarHotel(1);

        // Realiza la peticion DELETE y verifica la respuesta.
        mockMvc.perform(delete("/api/v1/hoteles/1"))
                .andExpect(status().isNoContent());

        verify(hotelService, times(1)).eliminarHotel(1);
    }
}
