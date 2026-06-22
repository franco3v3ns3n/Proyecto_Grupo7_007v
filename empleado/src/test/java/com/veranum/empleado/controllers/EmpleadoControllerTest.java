package com.veranum.empleado.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veranum.empleado.dtos.request.EmpleadoRequestDTO;
import com.veranum.empleado.dtos.response.EmpleadoResponseDTO;
import com.veranum.empleado.services.EmpleadoService;
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

@WebMvcTest(EmpleadoController.class) // Indica que se esta probando el controlador de Empleado
@TestPropertySource(properties = "HOTEL_SERVICE_URL=http://localhost")
public class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite realizar peticiones HTTP simuladas

    @MockBean
    private EmpleadoService empleadoService; // Crea un mock del servicio de Empleado

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos Java a JSON

    private EmpleadoRequestDTO request;
    private EmpleadoResponseDTO response;

    @BeforeEach
    void setUp() {
        // Configura la solicitud y la respuesta de ejemplo antes de cada prueba.
        request = new EmpleadoRequestDTO();
        request.setIdHotel(10);
        request.setNombres("Ignacio Tomas");
        request.setApellidos("Lagos Vera");
        request.setRut("21436587-1");
        request.setTipoEmpleado("ADMINISTRADOR");
        request.setEstadoEmpleado("ACTIVO");

        response = EmpleadoResponseDTO.builder()
                .idEmpleado(1)
                .idHotel(10)
                .nombres("Ignacio Tomas")
                .apellidos("Lagos Vera")
                .rut("21436587-1")
                .tipoEmpleado("ADMINISTRADOR")
                .estadoEmpleado("ACTIVO")
                .build();
    }

    @Test
    public void testGetAllEmpleados() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(empleadoService.obtenerEmpleados()).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEmpleado").value(1))
                .andExpect(jsonPath("$[0].idHotel").value(10))
                .andExpect(jsonPath("$[0].estadoEmpleado").value("ACTIVO"));

        verify(empleadoService).obtenerEmpleados();
    }

    @Test
    public void testGetEmpleadoById() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(empleadoService.obtenerEmpleadoPorId(1)).thenReturn(response);

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/empleados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEmpleado").value(1))
                .andExpect(jsonPath("$.rut").value("21436587-1"))
                .andExpect(jsonPath("$.estadoEmpleado").value("ACTIVO"));

        verify(empleadoService).obtenerEmpleadoPorId(1);
    }

    @Test
    public void testGetEmpleadosByHotel() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(empleadoService.obtenerEmpleadosPorHotel(10)).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/empleados/hotel/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEmpleado").value(1))
                .andExpect(jsonPath("$[0].idHotel").value(10));

        verify(empleadoService).obtenerEmpleadosPorHotel(10);
    }

    @Test
    public void testGetEmpleadosByEstado() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(empleadoService.obtenerEmpleadosPorEstado("ACTIVO")).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/empleados/estado/ACTIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEmpleado").value(1))
                .andExpect(jsonPath("$[0].estadoEmpleado").value("ACTIVO"));

        verify(empleadoService).obtenerEmpleadosPorEstado("ACTIVO");
    }

    @Test
    public void testCreateEmpleado() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(empleadoService.crearEmpleado(any(EmpleadoRequestDTO.class))).thenReturn(response);

        // Realiza la peticion POST y verifica la respuesta.
        mockMvc.perform(post("/api/v1/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEmpleado").value(1))
                .andExpect(jsonPath("$.idHotel").value(10))
                .andExpect(jsonPath("$.estadoEmpleado").value("ACTIVO"));

        verify(empleadoService).crearEmpleado(any(EmpleadoRequestDTO.class));
    }

    @Test
    public void testUpdateEmpleado() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(empleadoService.actualizarEmpleado(any(Integer.class), any(EmpleadoRequestDTO.class)))
                .thenReturn(response);

        // Realiza la peticion PUT y verifica la respuesta.
        mockMvc.perform(put("/api/v1/empleados/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEmpleado").value(1))
                .andExpect(jsonPath("$.rut").value("21436587-1"))
                .andExpect(jsonPath("$.estadoEmpleado").value("ACTIVO"));

        verify(empleadoService).actualizarEmpleado(any(Integer.class), any(EmpleadoRequestDTO.class));
    }

    @Test
    public void testDeleteEmpleado() throws Exception {
        // Define el comportamiento del servicio simulado.
        doNothing().when(empleadoService).eliminarEmpleado(1);

        // Realiza la peticion DELETE y verifica la respuesta.
        mockMvc.perform(delete("/api/v1/empleados/1"))
                .andExpect(status().isNoContent());

        verify(empleadoService, times(1)).eliminarEmpleado(1);
    }

    @Test
    public void testCreateEmpleadoConEstadoInvalido() throws Exception {
        // Configura una solicitud que no cumple la validacion de estado.
        request.setEstadoEmpleado("SUSPENDIDO");

        // Realiza la peticion POST y verifica el error de validacion.
        mockMvc.perform(post("/api/v1/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.estadoEmpleado")
                        .value("El estado debe ser ACTIVO o INACTIVO"));

        verify(empleadoService, never()).crearEmpleado(any(EmpleadoRequestDTO.class));
    }
}
