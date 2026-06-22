package com.veranum.servicio.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veranum.servicio.dtos.request.ServicioRequestDTO;
import com.veranum.servicio.dtos.response.ServicioResponseDTO;
import com.veranum.servicio.services.ServicioService;
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

@WebMvcTest(ServicioController.class) // Indica que se esta probando el controlador de Servicio
@TestPropertySource(properties = "HOTEL_SERVICE_URL=http://localhost")
public class ServicioControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite realizar peticiones HTTP simuladas

    @MockBean
    private ServicioService servicioService; // Crea un mock del servicio de Servicio

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos Java a JSON

    private ServicioRequestDTO request;
    private ServicioResponseDTO response;

    @BeforeEach
    void setUp() {
        // Configura la solicitud y la respuesta de ejemplo antes de cada prueba.
        request = new ServicioRequestDTO();
        request.setIdHotel(10);
        request.setNombre("WiFi Premium");
        request.setValorDiario(5000.0);
        request.setEstadoServicio("ACTIVO");

        response = ServicioResponseDTO.builder()
                .idServicio(1)
                .idHotel(10)
                .nombre("WiFi Premium")
                .valorDiario(5000.0)
                .estadoServicio("ACTIVO")
                .build();
    }

    @Test
    public void testGetAllServicios() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(servicioService.obtenerServicios()).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/servicios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idServicio").value(1))
                .andExpect(jsonPath("$[0].idHotel").value(10))
                .andExpect(jsonPath("$[0].estadoServicio").value("ACTIVO"));

        verify(servicioService).obtenerServicios();
    }

    @Test
    public void testGetServicioById() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(servicioService.obtenerServicioPorId(1)).thenReturn(response);

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/servicios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idServicio").value(1))
                .andExpect(jsonPath("$.nombre").value("WiFi Premium"))
                .andExpect(jsonPath("$.estadoServicio").value("ACTIVO"));

        verify(servicioService).obtenerServicioPorId(1);
    }

    @Test
    public void testGetServiciosByHotel() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(servicioService.obtenerServiciosPorHotel(10)).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/servicios/hotel/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idServicio").value(1))
                .andExpect(jsonPath("$[0].idHotel").value(10));

        verify(servicioService).obtenerServiciosPorHotel(10);
    }

    @Test
    public void testGetServiciosByEstado() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(servicioService.obtenerServiciosPorEstado("ACTIVO")).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/servicios/estado/ACTIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idServicio").value(1))
                .andExpect(jsonPath("$[0].estadoServicio").value("ACTIVO"));

        verify(servicioService).obtenerServiciosPorEstado("ACTIVO");
    }

    @Test
    public void testCreateServicio() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(servicioService.crearServicio(any(ServicioRequestDTO.class))).thenReturn(response);

        // Realiza la peticion POST y verifica la respuesta.
        mockMvc.perform(post("/api/v1/servicios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idServicio").value(1))
                .andExpect(jsonPath("$.idHotel").value(10))
                .andExpect(jsonPath("$.estadoServicio").value("ACTIVO"));

        verify(servicioService).crearServicio(any(ServicioRequestDTO.class));
    }

    @Test
    public void testUpdateServicio() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(servicioService.actualizarServicio(any(Integer.class), any(ServicioRequestDTO.class)))
                .thenReturn(response);

        // Realiza la peticion PUT y verifica la respuesta.
        mockMvc.perform(put("/api/v1/servicios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idServicio").value(1))
                .andExpect(jsonPath("$.nombre").value("WiFi Premium"))
                .andExpect(jsonPath("$.estadoServicio").value("ACTIVO"));

        verify(servicioService).actualizarServicio(any(Integer.class), any(ServicioRequestDTO.class));
    }

    @Test
    public void testDeleteServicio() throws Exception {
        // Define el comportamiento del servicio simulado.
        doNothing().when(servicioService).eliminarServicio(1);

        // Realiza la peticion DELETE y verifica la respuesta.
        mockMvc.perform(delete("/api/v1/servicios/1"))
                .andExpect(status().isNoContent());

        verify(servicioService, times(1)).eliminarServicio(1);
    }

    @Test
    public void testCreateServicioConEstadoInvalido() throws Exception {
        // Configura una solicitud que no cumple la validacion de estado.
        request.setEstadoServicio("PENDIENTE");

        // Realiza la peticion POST y verifica el error de validacion.
        mockMvc.perform(post("/api/v1/servicios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.estadoServicio")
                        .value("El estado debe ser ACTIVO o INACTIVO"));

        verify(servicioService, never()).crearServicio(any(ServicioRequestDTO.class));
    }
}
