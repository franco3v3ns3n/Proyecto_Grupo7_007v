package com.veranum.pago.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veranum.pago.dtos.request.PagoRequestDTO;
import com.veranum.pago.dtos.response.PagoResponseDTO;
import com.veranum.pago.services.PagoService;
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

@WebMvcTest(PagoController.class) // Indica que se esta probando el controlador de Pago
@TestPropertySource(properties = "ESTADIA_SERVICE_URL=http://localhost")
public class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite realizar peticiones HTTP simuladas

    @MockBean
    private PagoService pagoService; // Crea un mock del servicio de Pago

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos Java a JSON

    private PagoRequestDTO request;
    private PagoResponseDTO response;

    @BeforeEach
    void setUp() {
        // Configura la solicitud y la respuesta de ejemplo antes de cada prueba.
        request = new PagoRequestDTO();
        request.setIdEstadia(10);
        request.setMonto(240000.0);
        request.setMetodoPago("TARJETA_CREDITO");
        request.setEstadoPago("PAGADO");
        request.setFechaPago(LocalDateTime.of(2026, 4, 13, 10, 50));

        response = PagoResponseDTO.builder()
                .idPago(1)
                .idEstadia(10)
                .monto(240000.0)
                .metodoPago("TARJETA_CREDITO")
                .estadoPago("PAGADO")
                .fechaPago(LocalDateTime.of(2026, 4, 13, 10, 50))
                .build();
    }

    @Test
    public void testGetAllPagos() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(pagoService.obtenerPagos()).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPago").value(1))
                .andExpect(jsonPath("$[0].idEstadia").value(10))
                .andExpect(jsonPath("$[0].estadoPago").value("PAGADO"));

        verify(pagoService).obtenerPagos();
    }

    @Test
    public void testGetPagoById() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(pagoService.obtenerPagoPorId(1)).thenReturn(response);

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPago").value(1))
                .andExpect(jsonPath("$.metodoPago").value("TARJETA_CREDITO"))
                .andExpect(jsonPath("$.estadoPago").value("PAGADO"));

        verify(pagoService).obtenerPagoPorId(1);
    }

    @Test
    public void testGetPagosByEstadia() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(pagoService.obtenerPagosPorEstadia(10)).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/pagos/estadia/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPago").value(1))
                .andExpect(jsonPath("$[0].idEstadia").value(10));

        verify(pagoService).obtenerPagosPorEstadia(10);
    }

    @Test
    public void testGetPagosByEstado() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(pagoService.obtenerPagosPorEstado("PAGADO")).thenReturn(List.of(response));

        // Realiza la peticion GET y verifica la respuesta.
        mockMvc.perform(get("/api/v1/pagos/estado/PAGADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPago").value(1))
                .andExpect(jsonPath("$[0].estadoPago").value("PAGADO"));

        verify(pagoService).obtenerPagosPorEstado("PAGADO");
    }

    @Test
    public void testCreatePago() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(pagoService.crearPago(any(PagoRequestDTO.class))).thenReturn(response);

        // Realiza la peticion POST y verifica la respuesta.
        mockMvc.perform(post("/api/v1/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPago").value(1))
                .andExpect(jsonPath("$.idEstadia").value(10))
                .andExpect(jsonPath("$.estadoPago").value("PAGADO"));

        verify(pagoService).crearPago(any(PagoRequestDTO.class));
    }

    @Test
    public void testUpdatePago() throws Exception {
        // Define el comportamiento del servicio simulado.
        when(pagoService.actualizarPago(any(Integer.class), any(PagoRequestDTO.class)))
                .thenReturn(response);

        // Realiza la peticion PUT y verifica la respuesta.
        mockMvc.perform(put("/api/v1/pagos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPago").value(1))
                .andExpect(jsonPath("$.metodoPago").value("TARJETA_CREDITO"))
                .andExpect(jsonPath("$.estadoPago").value("PAGADO"));

        verify(pagoService).actualizarPago(any(Integer.class), any(PagoRequestDTO.class));
    }

    @Test
    public void testDeletePago() throws Exception {
        // Define el comportamiento del servicio simulado.
        doNothing().when(pagoService).eliminarPago(1);

        // Realiza la peticion DELETE y verifica la respuesta.
        mockMvc.perform(delete("/api/v1/pagos/1"))
                .andExpect(status().isNoContent());

        verify(pagoService, times(1)).eliminarPago(1);
    }

    @Test
    public void testCreatePagoConEstadoInvalido() throws Exception {
        // Configura una solicitud que no cumple la validacion de estado.
        request.setEstadoPago("RECHAZADO");

        // Realiza la peticion POST y verifica el error de validacion.
        mockMvc.perform(post("/api/v1/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.estadoPago")
                        .value("El estado debe ser PENDIENTE, PAGADO o ANULADO"));

        verify(pagoService, never()).crearPago(any(PagoRequestDTO.class));
    }
}
