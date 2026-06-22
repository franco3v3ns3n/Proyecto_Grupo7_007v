package com.veranum.evento.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veranum.evento.dtos.request.CentroEventoRequestDTO;
import com.veranum.evento.dtos.response.CentroEventoResponseDTO;
import com.veranum.evento.services.CentroEventoService;
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

@WebMvcTest(CentroEventoController.class) // Indica que se esta probando el controlador de CentroEvento
@TestPropertySource(properties = "HOTEL_SERVICE_URL=http://localhost")
public class CentroEventoControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite realizar peticiones HTTP simuladas

    @MockBean
    private CentroEventoService centroEventoService; // Crea un mock del servicio de CentroEvento

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos Java a JSON

    private CentroEventoRequestDTO request;
    private CentroEventoResponseDTO response;

    @BeforeEach
    void setUp() {
        // Configura la solicitud y la respuesta de ejemplo antes de cada prueba.
        request = new CentroEventoRequestDTO();
        request.setIdHotel(10);
        request.setNombre("Salon Pacifico");
        request.setCapacidadPersonas(150);
        request.setPrecioCentroEvento(350000.0);
        request.setEstadoCentroEvento("ACTIVO");

        response = CentroEventoResponseDTO.builder()
                .idCentroEvento(1)
                .idHotel(10)
                .nombre("Salon Pacifico")
                .capacidadPersonas(150)
                .precioCentroEvento(350000.0)
                .estadoCentroEvento("ACTIVO")
                .build();
    }

    @Test
    public void testGetAllCentrosEventos() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(centroEventoService.obtenerCentrosEventos()).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/eventos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCentroEvento").value(1))
                .andExpect(jsonPath("$[0].idHotel").value(10))
                .andExpect(jsonPath("$[0].estadoCentroEvento").value("ACTIVO"));

        verify(centroEventoService).obtenerCentrosEventos();
    }

    @Test
    public void testGetCentroEventoById() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(centroEventoService.obtenerCentroEventoPorId(1)).thenReturn(response);

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/eventos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCentroEvento").value(1))
                .andExpect(jsonPath("$.nombre").value("Salon Pacifico"))
                .andExpect(jsonPath("$.estadoCentroEvento").value("ACTIVO"));

        verify(centroEventoService).obtenerCentroEventoPorId(1);
    }

    @Test
    public void testGetCentrosEventosByHotel() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(centroEventoService.obtenerCentrosEventosPorHotel(10)).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/eventos/hotel/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCentroEvento").value(1))
                .andExpect(jsonPath("$[0].idHotel").value(10));

        verify(centroEventoService).obtenerCentrosEventosPorHotel(10);
    }

    @Test
    public void testGetCentrosEventosByEstado() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(centroEventoService.obtenerCentrosEventosPorEstado("ACTIVO")).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/eventos/estado/ACTIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCentroEvento").value(1))
                .andExpect(jsonPath("$[0].estadoCentroEvento").value("ACTIVO"));

        verify(centroEventoService).obtenerCentrosEventosPorEstado("ACTIVO");
    }

    @Test
    public void testCreateCentroEvento() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(centroEventoService.crearCentroEvento(any(CentroEventoRequestDTO.class))).thenReturn(response);

        // Realiza la peticion POST y verifica la respuesta.
        mockMvc.perform(post("/api/v1/eventos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCentroEvento").value(1))
                .andExpect(jsonPath("$.idHotel").value(10))
                .andExpect(jsonPath("$.estadoCentroEvento").value("ACTIVO"));

        verify(centroEventoService).crearCentroEvento(any(CentroEventoRequestDTO.class));
    }

    @Test
    public void testUpdateCentroEvento() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(centroEventoService.actualizarCentroEvento(any(Integer.class), any(CentroEventoRequestDTO.class)))
                .thenReturn(response);

        // Realiza la peticion PUT y verifica la respuesta.
        mockMvc.perform(put("/api/v1/eventos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCentroEvento").value(1))
                .andExpect(jsonPath("$.nombre").value("Salon Pacifico"))
                .andExpect(jsonPath("$.estadoCentroEvento").value("ACTIVO"));

        verify(centroEventoService).actualizarCentroEvento(any(Integer.class), any(CentroEventoRequestDTO.class));
    }

    @Test
    public void testDeleteCentroEvento() throws Exception {
        // Define el comportamiento del servicio simulado.
        doNothing().when(centroEventoService).eliminarCentroEvento(1);

        // Realiza la peticion DELETE y verifica la respuesta.
        mockMvc.perform(delete("/api/v1/eventos/1"))
                .andExpect(status().isNoContent());

        verify(centroEventoService, times(1)).eliminarCentroEvento(1);
    }

    @Test
    public void testCreateCentroEventoConEstadoInvalido() throws Exception {
        // Configura una solicitud que no cumple la validacion de estado.
        request.setEstadoCentroEvento("PENDIENTE");

        // Realiza la peticion POST y verifica el error de validacion.
        mockMvc.perform(post("/api/v1/eventos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.estadoCentroEvento")
                        .value("El estado debe ser ACTIVO o INACTIVO"));

        verify(centroEventoService, never()).crearCentroEvento(any(CentroEventoRequestDTO.class));
    }
}
