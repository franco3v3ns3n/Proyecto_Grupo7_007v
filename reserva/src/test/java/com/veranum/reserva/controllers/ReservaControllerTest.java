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

// Carga solo la capa web asociada a ReservaController; la lógica de negocio queda aislada.
@WebMvcTest(ReservaController.class)
// Proporciona valores para resolver placeholders de Feign Clients; no simula llamadas HTTP.
@TestPropertySource(properties = {
        "CLIENTE_SERVICE_URL=http://localhost",
        "HABITACION_SERVICE_URL=http://localhost"
})
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc; // Ejecuta solicitudes HTTP simuladas sin levantar un servidor real.

    @MockBean
    private ReservaService reservaService; // Sustituye el servicio real para aislar el controlador.

    @Autowired
    private ObjectMapper objectMapper; // Serializa el RequestDTO como JSON para las peticiones.

    private ReservaRequestDTO request;
    private ReservaResponseDTO response;

    @BeforeEach
    void setUp() {
        // Prepara datos válidos reutilizables antes de cada prueba del controlador.
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
        // Preparación del mock: el servicio devuelve una lista con una reserva.
        when(reservaService.obtenerReservas()).thenReturn(List.of(response));

        // Ejecución de la petición y verificación del estado HTTP y del contenido JSON.
        mockMvc.perform(get("/api/v1/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReserva").value(1))
                .andExpect(jsonPath("$[0].idCliente").value(10))
                .andExpect(jsonPath("$[0].estadoReserva").value("CONFIRMADA"));

        // Verificación de la interacción: el controlador delega la consulta al servicio.
        verify(reservaService).obtenerReservas();
    }

    @Test
    public void testGetReservaById() throws Exception {
        // Preparación del mock: el servicio encuentra la reserva solicitada.
        when(reservaService.obtenerReservaPorId(1)).thenReturn(response);

        // Ejecución de la petición y verificación del estado HTTP y del contenido JSON.
        mockMvc.perform(get("/api/v1/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReserva").value(1))
                .andExpect(jsonPath("$.idHabitacion").value(20))
                .andExpect(jsonPath("$.estadoReserva").value("CONFIRMADA"));

        // Verificación de la interacción: se consulta el ID recibido en la ruta.
        verify(reservaService).obtenerReservaPorId(1);
    }

    @Test
    public void testGetReservasByCliente() throws Exception {
        // Preparación del mock: el servicio devuelve las reservas del cliente.
        when(reservaService.obtenerReservasPorCliente(10)).thenReturn(List.of(response));

        // Ejecución de la petición y verificación del estado HTTP y del contenido JSON.
        mockMvc.perform(get("/api/v1/reservas/cliente/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReserva").value(1))
                .andExpect(jsonPath("$[0].idCliente").value(10));

        // Verificación de la interacción: el controlador delega la búsqueda por cliente.
        verify(reservaService).obtenerReservasPorCliente(10);
    }

    @Test
    public void testGetReservasByHabitacion() throws Exception {
        // Preparación del mock: el servicio devuelve las reservas de la habitación.
        when(reservaService.obtenerReservasPorHabitacion(20)).thenReturn(List.of(response));

        // Ejecución de la petición y verificación del estado HTTP y del contenido JSON.
        mockMvc.perform(get("/api/v1/reservas/habitacion/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReserva").value(1))
                .andExpect(jsonPath("$[0].idHabitacion").value(20));

        // Verificación de la interacción: el controlador delega la búsqueda por habitación.
        verify(reservaService).obtenerReservasPorHabitacion(20);
    }

    @Test
    public void testGetReservasByEstado() throws Exception {
        // Preparación del mock: el servicio devuelve las reservas con estado CONFIRMADA.
        when(reservaService.obtenerReservasPorEstado("CONFIRMADA"))
                .thenReturn(List.of(response));

        // Ejecución de la petición y verificación del estado HTTP y del contenido JSON.
        mockMvc.perform(get("/api/v1/reservas/estado/CONFIRMADA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReserva").value(1))
                .andExpect(jsonPath("$[0].estadoReserva").value("CONFIRMADA"));

        // Verificación de la interacción: el estado de la ruta se entrega al servicio.
        verify(reservaService).obtenerReservasPorEstado("CONFIRMADA");
    }

    @Test
    public void testCreateReserva() throws Exception {
        // Preparación del mock: el servicio responde con la reserva creada.
        when(reservaService.crearReserva(any(ReservaRequestDTO.class))).thenReturn(response);

        // Ejecución del POST con el DTO serializado y verificación del estado HTTP y JSON.
        mockMvc.perform(post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idReserva").value(1))
                .andExpect(jsonPath("$.idCliente").value(10))
                .andExpect(jsonPath("$.estadoReserva").value("CONFIRMADA"));

        // Verificación de la interacción: una solicitud válida se delega a ReservaService.
        verify(reservaService).crearReserva(any(ReservaRequestDTO.class));
    }

    @Test
    public void testUpdateReserva() throws Exception {
        // Preparación del mock: el servicio responde con la reserva actualizada.
        when(reservaService.actualizarReserva(any(Integer.class), any(ReservaRequestDTO.class)))
                .thenReturn(response);

        // Ejecución del PUT con JSON y verificación del estado HTTP y contenido actualizado.
        mockMvc.perform(put("/api/v1/reservas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReserva").value(1))
                .andExpect(jsonPath("$.idHabitacion").value(20))
                .andExpect(jsonPath("$.estadoReserva").value("CONFIRMADA"));

        // Verificación de la interacción: el controlador delega la actualización al servicio.
        verify(reservaService).actualizarReserva(any(Integer.class), any(ReservaRequestDTO.class));
    }

    @Test
    public void testDeleteReserva() throws Exception {
        // Preparación del mock: eliminarReserva no devuelve cuerpo.
        doNothing().when(reservaService).eliminarReserva(1);

        // Ejecución del DELETE y verificación del estado HTTP 204 No Content.
        mockMvc.perform(delete("/api/v1/reservas/1"))
                .andExpect(status().isNoContent());

        // Verificación de la interacción: se solicita una única eliminación al servicio.
        verify(reservaService, times(1)).eliminarReserva(1);
    }

    @Test
    public void testCreateReservaConEstadoInvalido() throws Exception {
        // El DTO contiene un estado no permitido por Bean Validation.
        request.setEstadoReserva("PENDIENTE");

        // Bean Validation rechaza el POST con 400 Bad Request y detalla el campo inválido.
        mockMvc.perform(post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.estadoReserva")
                        .value("El estado debe ser CONFIRMADA, CANCELADA, FINALIZADA o NO_SHOW"));

        // never() comprueba que la creación se detuvo antes de llegar a la lógica de negocio.
        verify(reservaService, never()).crearReserva(any(ReservaRequestDTO.class));
    }
}
