package com.veranum.estadia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veranum.estadia.dtos.request.EstadiaRequestDTO;
import com.veranum.estadia.dtos.response.EstadiaResponseDTO;
import com.veranum.estadia.services.EstadiaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
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

@WebMvcTest(EstadiaController.class) // Indica que se esta probando el controlador de Estadia
@TestPropertySource(properties = {
        "CLIENTE_SERVICE_URL=http://localhost",
        "HABITACION_SERVICE_URL=http://localhost",
        "RESERVA_SERVICE_URL=http://localhost"
})
public class EstadiaControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite realizar peticiones HTTP simuladas

    @MockBean
    private EstadiaService estadiaService; // Crea un mock del servicio de Estadia

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos Java a JSON

    private EstadiaRequestDTO request;
    private EstadiaResponseDTO response;

    @BeforeEach
    void setUp() {
        // Configura la solicitud y la respuesta de ejemplo antes de cada prueba.
        request = new EstadiaRequestDTO();
        request.setIdCliente(10);
        request.setIdHabitacion(20);
        request.setIdReserva(30);
        request.setFechaCheckin(LocalDateTime.of(2026, 9, 10, 15, 0));
        request.setFechaCheckout(LocalDateTime.of(2026, 9, 15, 11, 0));
        request.setEstadoEstadia("EN_CURSO");

        response = EstadiaResponseDTO.builder()
                .idEstadia(1)
                .idCliente(10)
                .idHabitacion(20)
                .idReserva(30)
                .fechaCheckin(LocalDateTime.of(2026, 9, 10, 15, 0))
                .fechaCheckout(LocalDateTime.of(2026, 9, 15, 11, 0))
                .estadoEstadia("EN_CURSO")
                .build();
    }

    @Test
    public void testGetAllEstadias() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(estadiaService.obtenerEstadias()).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/estadias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEstadia").value(1))
                .andExpect(jsonPath("$[0].idCliente").value(10))
                .andExpect(jsonPath("$[0].estadoEstadia").value("EN_CURSO"));

        verify(estadiaService).obtenerEstadias();
    }

    @Test
    public void testGetEstadiaById() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(estadiaService.obtenerEstadiaPorId(1)).thenReturn(response);

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/estadias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEstadia").value(1))
                .andExpect(jsonPath("$.idHabitacion").value(20))
                .andExpect(jsonPath("$.estadoEstadia").value("EN_CURSO"));

        verify(estadiaService).obtenerEstadiaPorId(1);
    }

    @Test
    public void testGetEstadiasByCliente() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(estadiaService.obtenerEstadiasPorCliente(10)).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/estadias/cliente/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEstadia").value(1))
                .andExpect(jsonPath("$[0].idCliente").value(10));

        verify(estadiaService).obtenerEstadiasPorCliente(10);
    }

    @Test
    public void testGetEstadiasByHabitacion() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(estadiaService.obtenerEstadiasPorHabitacion(20)).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/estadias/habitacion/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEstadia").value(1))
                .andExpect(jsonPath("$[0].idHabitacion").value(20));

        verify(estadiaService).obtenerEstadiasPorHabitacion(20);
    }

    @Test
    public void testGetEstadiasByReserva() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(estadiaService.obtenerEstadiasPorReserva(30)).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/estadias/reserva/30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEstadia").value(1))
                .andExpect(jsonPath("$[0].idReserva").value(30));

        verify(estadiaService).obtenerEstadiasPorReserva(30);
    }

    @Test
    public void testGetEstadiasByEstado() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(estadiaService.obtenerEstadiasPorEstado("EN_CURSO")).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/estadias/estado/EN_CURSO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEstadia").value(1))
                .andExpect(jsonPath("$[0].estadoEstadia").value("EN_CURSO"));

        verify(estadiaService).obtenerEstadiasPorEstado("EN_CURSO");
    }

    @Test
    public void testCreateEstadia() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(estadiaService.crearEstadia(any(EstadiaRequestDTO.class))).thenReturn(response);

        // Realiza la peticion POST y verifica la respuesta.
        mockMvc.perform(post("/api/v1/estadias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEstadia").value(1))
                .andExpect(jsonPath("$.idCliente").value(10))
                .andExpect(jsonPath("$.estadoEstadia").value("EN_CURSO"));

        verify(estadiaService).crearEstadia(any(EstadiaRequestDTO.class));
    }

    @Test
    public void testUpdateEstadia() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(estadiaService.actualizarEstadia(any(Integer.class), any(EstadiaRequestDTO.class)))
                .thenReturn(response);

        // Realiza la peticion PUT y verifica la respuesta.
        mockMvc.perform(put("/api/v1/estadias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEstadia").value(1))
                .andExpect(jsonPath("$.idHabitacion").value(20))
                .andExpect(jsonPath("$.estadoEstadia").value("EN_CURSO"));

        verify(estadiaService).actualizarEstadia(any(Integer.class), any(EstadiaRequestDTO.class));
    }

    @Test
    public void testDeleteEstadia() throws Exception {
        // Define el comportamiento del servicio simulado.
        doNothing().when(estadiaService).eliminarEstadia(1);

        // Realiza la peticion DELETE y verifica la respuesta.
        mockMvc.perform(delete("/api/v1/estadias/1"))
                .andExpect(status().isNoContent());

        verify(estadiaService, times(1)).eliminarEstadia(1);
    }

    @Test
    public void testCreateEstadiaConEstadoInvalido() throws Exception {
        // Configura una solicitud que no cumple la validacion de estado.
        request.setEstadoEstadia("CANCELADA");

        // Realiza la peticion POST y verifica el error de validacion.
        mockMvc.perform(post("/api/v1/estadias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.estadoEstadia")
                        .value("El estado debe ser RESERVADA, EN_CURSO o FINALIZADA"));

        verify(estadiaService, never()).crearEstadia(any(EstadiaRequestDTO.class));
    }
}
