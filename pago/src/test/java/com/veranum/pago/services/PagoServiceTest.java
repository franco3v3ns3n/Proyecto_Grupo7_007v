package com.veranum.pago.services;

import com.veranum.pago.clients.EstadiaClient;
import com.veranum.pago.dtos.request.PagoRequestDTO;
import com.veranum.pago.dtos.response.EstadiaResponseDTO;
import com.veranum.pago.dtos.response.PagoResponseDTO;
import com.veranum.pago.exceptions.ResourceNotFoundException;
import com.veranum.pago.models.PagoModel;
import com.veranum.pago.repositories.PagoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest {

    private static final LocalDateTime FECHA_PAGO = LocalDateTime.of(2026, 4, 13, 10, 50);
    private static final LocalDateTime FECHA_CHECKIN = LocalDateTime.of(2026, 4, 10, 15, 0);
    private static final LocalDateTime FECHA_CHECKOUT = LocalDateTime.of(2026, 4, 13, 11, 0);

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private EstadiaClient estadiaClient;

    @InjectMocks
    private PagoService pagoService;

    private PagoModel pago;
    private PagoRequestDTO request;

    @BeforeEach
    void setUp() {
        // Configura los objetos de ejemplo antes de cada prueba.
        pago = crearPagoModel();
        request = crearRequest();
    }

    @Test
    public void testObtenerPagos() {
        // Define el comportamiento del repositorio simulado.
        when(pagoRepository.findAll()).thenReturn(List.of(pago));

        // Ejecuta el metodo del servicio.
        List<PagoResponseDTO> pagos = pagoService.obtenerPagos();

        // Verifica el resultado obtenido.
        assertNotNull(pagos);
        assertEquals(1, pagos.size());
        assertEquals(1, pagos.get(0).getIdPago());
        verify(pagoRepository).findAll();
    }

    @Test
    public void testObtenerPagoPorId() {
        // Define el comportamiento del repositorio simulado.
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));

        // Ejecuta el metodo del servicio.
        PagoResponseDTO encontrado = pagoService.obtenerPagoPorId(1);

        // Verifica el resultado obtenido.
        assertNotNull(encontrado);
        assertEquals(1, encontrado.getIdPago());
        assertEquals("PAGADO", encontrado.getEstadoPago());
        verify(pagoRepository).findById(1);
    }

    @Test
    public void testObtenerPagosPorEstadia() {
        // Simula la existencia de la estadia y sus pagos asociados.
        when(estadiaClient.obtenerEstadiaPorId(10)).thenReturn(crearEstadia(10));
        when(pagoRepository.findByIdEstadia(10)).thenReturn(List.of(pago));

        // Ejecuta el metodo del servicio.
        List<PagoResponseDTO> pagos = pagoService.obtenerPagosPorEstadia(10);

        // Verifica el resultado obtenido.
        assertNotNull(pagos);
        assertEquals(1, pagos.size());
        assertEquals(10, pagos.get(0).getIdEstadia());
        verify(estadiaClient).obtenerEstadiaPorId(10);
        verify(pagoRepository).findByIdEstadia(10);
    }

    @Test
    public void testObtenerPagosPorEstado() {
        // Define el comportamiento del repositorio simulado.
        when(pagoRepository.findByEstadoPago("PAGADO")).thenReturn(List.of(pago));

        // Ejecuta el metodo del servicio.
        List<PagoResponseDTO> pagos = pagoService.obtenerPagosPorEstado("PAGADO");

        // Verifica el resultado obtenido.
        assertNotNull(pagos);
        assertEquals(1, pagos.size());
        assertEquals("PAGADO", pagos.get(0).getEstadoPago());
        verify(pagoRepository).findByEstadoPago("PAGADO");
    }

    @Test
    public void testCrearPago() {
        // Simula las dependencias necesarias para crear el pago.
        when(estadiaClient.obtenerEstadiaPorId(10)).thenReturn(crearEstadia(10));
        when(pagoRepository.save(any(PagoModel.class))).thenAnswer(invocation -> {
            PagoModel pago = invocation.getArgument(0);
            pago.setIdPago(1);
            return pago;
        });

        // Ejecuta el metodo del servicio.
        PagoResponseDTO guardado = pagoService.crearPago(request);

        // Verifica el resultado obtenido.
        assertNotNull(guardado);
        assertEquals(1, guardado.getIdPago());
        assertEquals(240000.0, guardado.getMonto());
        verify(estadiaClient).obtenerEstadiaPorId(10);
        verify(pagoRepository).save(any(PagoModel.class));
    }

    @Test
    public void testActualizarPago() {
        // Configura la solicitud actualizada y simula sus dependencias.
        request = crearRequestActualizado();
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));
        when(estadiaClient.obtenerEstadiaPorId(11)).thenReturn(crearEstadia(11));
        when(pagoRepository.save(pago)).thenReturn(pago);

        // Ejecuta el metodo del servicio.
        PagoResponseDTO actualizado = pagoService.actualizarPago(1, request);

        // Verifica el resultado obtenido.
        assertNotNull(actualizado);
        assertEquals(11, actualizado.getIdEstadia());
        assertEquals("PENDIENTE", actualizado.getEstadoPago());
        verify(pagoRepository).findById(1);
        verify(estadiaClient).obtenerEstadiaPorId(11);
        verify(pagoRepository).save(pago);
    }

    @Test
    public void testEliminarPago() {
        // Simula la busqueda del pago que sera eliminado.
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));

        // Ejecuta el metodo del servicio.
        pagoService.eliminarPago(1);

        // Verifica que el repositorio elimine el pago encontrado.
        verify(pagoRepository).findById(1);
        verify(pagoRepository).delete(pago);
    }

    @Test
    public void testObtenerPagoPorIdInexistente() {
        // Simula que el pago solicitado no existe.
        when(pagoRepository.findById(99)).thenReturn(Optional.empty());

        // Ejecuta el metodo y captura la excepcion esperada.
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> pagoService.obtenerPagoPorId(99)
        );

        // Verifica el mensaje de la excepcion.
        assertEquals("Pago no encontrado", exception.getMessage());
        verify(pagoRepository).findById(99);
    }

    private PagoModel crearPagoModel() {
        return PagoModel.builder()
                .idPago(1)
                .idEstadia(10)
                .monto(240000.0)
                .metodoPago("TARJETA_CREDITO")
                .estadoPago("PAGADO")
                .fechaPago(FECHA_PAGO)
                .build();
    }

    private PagoRequestDTO crearRequest() {
        PagoRequestDTO request = new PagoRequestDTO();
        request.setIdEstadia(10);
        request.setMonto(240000.0);
        request.setMetodoPago("TARJETA_CREDITO");
        request.setEstadoPago("PAGADO");
        request.setFechaPago(FECHA_PAGO);
        return request;
    }

    private PagoRequestDTO crearRequestActualizado() {
        PagoRequestDTO request = new PagoRequestDTO();
        request.setIdEstadia(11);
        request.setMonto(270000.0);
        request.setMetodoPago("TARJETA_DEBITO");
        request.setEstadoPago("PENDIENTE");
        request.setFechaPago(LocalDateTime.of(2026, 4, 18, 11, 15));
        return request;
    }

    private EstadiaResponseDTO crearEstadia(Integer idEstadia) {
        return EstadiaResponseDTO.builder()
                .idEstadia(idEstadia)
                .idCliente(10)
                .idHabitacion(20)
                .idReserva(30)
                .fechaCheckin(FECHA_CHECKIN)
                .fechaCheckout(FECHA_CHECKOUT)
                .estadoEstadia("FINALIZADA")
                .build();
    }
}
