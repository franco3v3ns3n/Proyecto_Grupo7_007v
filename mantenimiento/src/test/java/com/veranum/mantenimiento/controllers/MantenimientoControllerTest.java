package com.veranum.mantenimiento.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veranum.mantenimiento.dtos.request.MantenimientoRequestDTO;
import com.veranum.mantenimiento.dtos.response.MantenimientoResponseDTO;
import com.veranum.mantenimiento.services.MantenimientoService;
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

@WebMvcTest(MantenimientoController.class) // Indica que se esta probando el controlador de Mantenimiento
@TestPropertySource(properties = "HABITACION_SERVICE_URL=http://localhost")
public class MantenimientoControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite realizar peticiones HTTP simuladas

    @MockBean
    private MantenimientoService mantenimientoService; // Crea un mock del servicio de Mantenimiento

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos Java a JSON

    private MantenimientoRequestDTO request;
    private MantenimientoResponseDTO response;

    @BeforeEach
    void setUp() {
        // Configura la solicitud y la respuesta de ejemplo antes de cada prueba.
        request = new MantenimientoRequestDTO();
        request.setIdHabitacion(20);
        request.setTipoMantenimiento("Limpieza profunda");
        request.setFechaInicio(LocalDateTime.of(2026, 4, 1, 9, 0));
        request.setFechaFin(LocalDateTime.of(2026, 4, 1, 12, 0));
        request.setEstadoMantenimiento("FINALIZADO");

        response = MantenimientoResponseDTO.builder()
                .idMantenimiento(1)
                .idHabitacion(20)
                .tipoMantenimiento("Limpieza profunda")
                .fechaInicio(LocalDateTime.of(2026, 4, 1, 9, 0))
                .fechaFin(LocalDateTime.of(2026, 4, 1, 12, 0))
                .estadoMantenimiento("FINALIZADO")
                .build();
    }

    @Test
    public void testGetAllMantenimientos() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(mantenimientoService.obtenerMantenimientos()).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/mantenimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMantenimiento").value(1))
                .andExpect(jsonPath("$[0].idHabitacion").value(20))
                .andExpect(jsonPath("$[0].estadoMantenimiento").value("FINALIZADO"));

        verify(mantenimientoService).obtenerMantenimientos();
    }

    @Test
    public void testGetMantenimientoById() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(mantenimientoService.obtenerMantenimientoPorId(1)).thenReturn(response);

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/mantenimientos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMantenimiento").value(1))
                .andExpect(jsonPath("$.tipoMantenimiento").value("Limpieza profunda"))
                .andExpect(jsonPath("$.estadoMantenimiento").value("FINALIZADO"));

        verify(mantenimientoService).obtenerMantenimientoPorId(1);
    }

    @Test
    public void testGetMantenimientosByHabitacion() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(mantenimientoService.obtenerMantenimientosPorHabitacion(20)).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/mantenimientos/habitacion/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMantenimiento").value(1))
                .andExpect(jsonPath("$[0].idHabitacion").value(20));

        verify(mantenimientoService).obtenerMantenimientosPorHabitacion(20);
    }

    @Test
    public void testGetMantenimientosByEstado() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(mantenimientoService.obtenerMantenimientosPorEstado("FINALIZADO")).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/mantenimientos/estado/FINALIZADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMantenimiento").value(1))
                .andExpect(jsonPath("$[0].estadoMantenimiento").value("FINALIZADO"));

        verify(mantenimientoService).obtenerMantenimientosPorEstado("FINALIZADO");
    }

    @Test
    public void testCreateMantenimiento() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(mantenimientoService.crearMantenimiento(any(MantenimientoRequestDTO.class))).thenReturn(response);

        // Realiza la peticion POST y verifica la respuesta.
        mockMvc.perform(post("/api/v1/mantenimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idMantenimiento").value(1))
                .andExpect(jsonPath("$.idHabitacion").value(20))
                .andExpect(jsonPath("$.estadoMantenimiento").value("FINALIZADO"));

        verify(mantenimientoService).crearMantenimiento(any(MantenimientoRequestDTO.class));
    }

    @Test
    public void testUpdateMantenimiento() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(mantenimientoService.actualizarMantenimiento(any(Integer.class), any(MantenimientoRequestDTO.class)))
                .thenReturn(response);

        // Realiza la peticion PUT y verifica la respuesta.
        mockMvc.perform(put("/api/v1/mantenimientos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMantenimiento").value(1))
                .andExpect(jsonPath("$.tipoMantenimiento").value("Limpieza profunda"))
                .andExpect(jsonPath("$.estadoMantenimiento").value("FINALIZADO"));

        verify(mantenimientoService).actualizarMantenimiento(any(Integer.class), any(MantenimientoRequestDTO.class));
    }

    @Test
    public void testDeleteMantenimiento() throws Exception {
        // Define el comportamiento del servicio simulado.
        doNothing().when(mantenimientoService).eliminarMantenimiento(1);

        // Realiza la peticion DELETE y verifica la respuesta.
        mockMvc.perform(delete("/api/v1/mantenimientos/1"))
                .andExpect(status().isNoContent());

        verify(mantenimientoService, times(1)).eliminarMantenimiento(1);
    }

    @Test
    public void testCreateMantenimientoConEstadoInvalido() throws Exception {
        // Configura una solicitud que no cumple la validacion de estado.
        request.setEstadoMantenimiento("CANCELADO");

        // Realiza la peticion POST y verifica el error de validacion.
        mockMvc.perform(post("/api/v1/mantenimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.estadoMantenimiento")
                        .value("El estado debe ser PROGRAMADO, EN_PROCESO o FINALIZADO"));

        verify(mantenimientoService, never()).crearMantenimiento(any(MantenimientoRequestDTO.class));
    }
}
