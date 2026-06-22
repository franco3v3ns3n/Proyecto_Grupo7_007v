package com.veranum.cliente.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veranum.cliente.dtos.request.ClienteRequestDTO;
import com.veranum.cliente.dtos.response.ClienteResponseDTO;
import com.veranum.cliente.services.ClienteService;
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

@WebMvcTest(ClienteController.class) // Indica que se esta probando el controlador de Cliente
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite realizar peticiones HTTP simuladas

    @MockBean
    private ClienteService clienteService; // Crea un mock del servicio de Cliente

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos Java a JSON

    private ClienteRequestDTO request;
    private ClienteResponseDTO response;

    @BeforeEach
    void setUp() {
        // Configura la solicitud y la respuesta de ejemplo antes de cada prueba.
        request = new ClienteRequestDTO();
        request.setNombres("Martina Sofia");
        request.setApellidos("Silva Torres");
        request.setRut("20123456-5");
        request.setTelefono("987654321");
        request.setCorreo("martina.silva@example.cl");
        request.setDireccion("Las Condes");

        response = ClienteResponseDTO.builder()
                .idCliente(1)
                .nombres("Martina Sofia")
                .apellidos("Silva Torres")
                .rut("20123456-5")
                .telefono("987654321")
                .correo("martina.silva@example.cl")
                .direccion("Las Condes")
                .build();
    }

    @Test
    public void testGetAllClientes() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(clienteService.obtenerClientes()).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCliente").value(1))
                .andExpect(jsonPath("$[0].nombres").value("Martina Sofia"))
                .andExpect(jsonPath("$[0].rut").value("20123456-5"));

        verify(clienteService).obtenerClientes();
    }

    @Test
    public void testGetClienteById() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(clienteService.obtenerClientePorId(1)).thenReturn(response);

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCliente").value(1))
                .andExpect(jsonPath("$.nombres").value("Martina Sofia"))
                .andExpect(jsonPath("$.rut").value("20123456-5"));

        verify(clienteService).obtenerClientePorId(1);
    }

    @Test
    public void testCreateCliente() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(clienteService.crearCliente(any(ClienteRequestDTO.class))).thenReturn(response);

        // Realiza la peticion POST y verifica la respuesta.
        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCliente").value(1))
                .andExpect(jsonPath("$.nombres").value("Martina Sofia"))
                .andExpect(jsonPath("$.rut").value("20123456-5"));

        verify(clienteService).crearCliente(any(ClienteRequestDTO.class));
    }

    @Test
    public void testUpdateCliente() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(clienteService.actualizarCliente(any(Integer.class), any(ClienteRequestDTO.class)))
                .thenReturn(response);

        // Realiza la peticion PUT y verifica la respuesta.
        mockMvc.perform(put("/api/v1/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCliente").value(1))
                .andExpect(jsonPath("$.nombres").value("Martina Sofia"))
                .andExpect(jsonPath("$.rut").value("20123456-5"));

        verify(clienteService).actualizarCliente(any(Integer.class), any(ClienteRequestDTO.class));
    }

    @Test
    public void testDeleteCliente() throws Exception {
        // Define el comportamiento del servicio simulado.
        doNothing().when(clienteService).eliminarCliente(1);

        // Realiza la peticion DELETE y verifica la respuesta.
        mockMvc.perform(delete("/api/v1/clientes/1"))
                .andExpect(status().isNoContent());

        verify(clienteService, times(1)).eliminarCliente(1);
    }
}
