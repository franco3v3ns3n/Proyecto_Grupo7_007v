package com.veranum.reserva.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veranum.reserva.dtos.request.ReservaRequestDTO;
import com.veranum.reserva.dtos.response.ReservaResponseDTO;
import com.veranum.reserva.services.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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

@WebMvcTest(ReservaController.class) // Indica que se esta probando el controlador de Reserva
@TestPropertySource(properties = {
        "CLIENTE_SERVICE_URL=http://localhost",
        "HABITACION_SERVICE_URL=http://localhost"
})
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite realizar peticiones HTTP simuladas

    @MockBean
    private ReservaService reservaService; // Crea un mock del servicio de Reserva

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos Java a JSON

    private ReservaRequestDTO request;
    private ReservaResponseDTO response;

    @BeforeEach
    void setUp() {
        // Configura la solicitud y la respuesta de ejemplo antes de cada prueba.
        request = new ReservaRequestDTO();
        request.setIdCliente(10);
        request.setIdHabitacion(20);
        request.setFechaInicio(LocalDate.of(2026, 7, 20));
        request.setFechaFin(LocalDate.of(2026, 7, 25));
        request.setEstadoReserva("CONFIRMADA");

        response = ReservaResponseDTO.builder()
                .idReserva(1)
                .idCliente(10)
                .idHabitacion(20)
                .fechaInicio(LocalDate.of(2026, 7, 20))
                .fechaFin(LocalDate.of(2026, 7, 25))
                .estadoReserva("CONFIRMADA")
                .fechaCreacion(LocalDateTime.of(2026, 6, 20, 10, 30))
                .build();
    }

    @Test
    public void testGetAllReservas() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(reservaService.obtenerReservas()).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReserva").value(1))
                .andExpect(jsonPath("$[0].idCliente").value(10))
                .andExpect(jsonPath("$[0].estadoReserva").value("CONFIRMADA"));

        verify(reservaService).obtenerReservas();
    }

    @Test
    public void testGetReservaById() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(reservaService.obtenerReservaPorId(1)).thenReturn(response);

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReserva").value(1))
                .andExpect(jsonPath("$.idHabitacion").value(20))
                .andExpect(jsonPath("$.estadoReserva").value("CONFIRMADA"));

        verify(reservaService).obtenerReservaPorId(1);
    }

    @Test
    public void testGetReservasByCliente() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(reservaService.obtenerReservasPorCliente(10)).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/reservas/cliente/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReserva").value(1))
                .andExpect(jsonPath("$[0].idCliente").value(10));

        verify(reservaService).obtenerReservasPorCliente(10);
    }

    @Test
    public void testGetReservasByHabitacion() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(reservaService.obtenerReservasPorHabitacion(20)).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/reservas/habitacion/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReserva").value(1))
                .andExpect(jsonPath("$[0].idHabitacion").value(20));

        verify(reservaService).obtenerReservasPorHabitacion(20);
    }

    @Test
    public void testGetReservasByEstado() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(reservaService.obtenerReservasPorEstado("CONFIRMADA"))
                .thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/reservas/estado/CONFIRMADA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReserva").value(1))
                .andExpect(jsonPath("$[0].estadoReserva").value("CONFIRMADA"));

        verify(reservaService).obtenerReservasPorEstado("CONFIRMADA");
    }

    @Test
    public void testCreateReserva() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(reservaService.crearReserva(any(ReservaRequestDTO.class))).thenReturn(response);

        // Realiza la peticion POST y verifica la respuesta.
        mockMvc.perform(post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idReserva").value(1))
                .andExpect(jsonPath("$.idCliente").value(10))
                .andExpect(jsonPath("$.estadoReserva").value("CONFIRMADA"));

        verify(reservaService).crearReserva(any(ReservaRequestDTO.class));
    }

    @Test
    public void testUpdateReserva() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(reservaService.actualizarReserva(any(Integer.class), any(ReservaRequestDTO.class)))
                .thenReturn(response);

        // Realiza la peticion PUT y verifica la respuesta.
        mockMvc.perform(put("/api/v1/reservas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReserva").value(1))
                .andExpect(jsonPath("$.idHabitacion").value(20))
                .andExpect(jsonPath("$.estadoReserva").value("CONFIRMADA"));

        verify(reservaService).actualizarReserva(any(Integer.class), any(ReservaRequestDTO.class));
    }

    @Test
    public void testDeleteReserva() throws Exception {
        // Define el comportamiento del servicio simulado.
        doNothing().when(reservaService).eliminarReserva(1);

        // Realiza la peticion DELETE y verifica la respuesta.
        mockMvc.perform(delete("/api/v1/reservas/1"))
                .andExpect(status().isNoContent());

        verify(reservaService, times(1)).eliminarReserva(1);
    }

    @Test
    public void testCreateReservaConEstadoInvalido() throws Exception {
        // Configura una solicitud que no cumple la validacion de estado.
        request.setEstadoReserva("PENDIENTE");

        // Realiza la peticion POST y verifica el error de validacion.
        mockMvc.perform(post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.estadoReserva")
                        .value("El estado debe ser CONFIRMADA, CANCELADA, FINALIZADA o NO_SHOW"));

        verify(reservaService, never()).crearReserva(any(ReservaRequestDTO.class));
    }
}
