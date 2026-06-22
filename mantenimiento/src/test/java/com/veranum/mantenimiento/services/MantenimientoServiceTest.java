package com.veranum.mantenimiento.services;

import com.veranum.mantenimiento.clients.HabitacionClient;
import com.veranum.mantenimiento.dtos.request.MantenimientoRequestDTO;
import com.veranum.mantenimiento.dtos.response.HabitacionResponseDTO;
import com.veranum.mantenimiento.dtos.response.MantenimientoResponseDTO;
import com.veranum.mantenimiento.exceptions.BusinessRuleException;
import com.veranum.mantenimiento.exceptions.ResourceNotFoundException;
import com.veranum.mantenimiento.models.MantenimientoModel;
import com.veranum.mantenimiento.repositories.MantenimientoRepository;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MantenimientoServiceTest {

    private static final LocalDateTime FECHA_INICIO = LocalDateTime.of(2026, 4, 1, 9, 0);
    private static final LocalDateTime FECHA_FIN = LocalDateTime.of(2026, 4, 1, 12, 0);

    @Mock
    private MantenimientoRepository mantenimientoRepository;

    @Mock
    private HabitacionClient habitacionClient;

    @InjectMocks
    private MantenimientoService mantenimientoService;

    private MantenimientoModel mantenimiento;
    private MantenimientoRequestDTO request;

    @BeforeEach
    void setUp() {
        // Configura los objetos de ejemplo antes de cada prueba.
        mantenimiento = crearMantenimientoModel();
        request = crearRequest();
    }

    @Test
    public void testObtenerMantenimientos() {
        // Define el comportamiento del repositorio simulado.
        when(mantenimientoRepository.findAll()).thenReturn(List.of(mantenimiento));

        // Ejecuta el metodo del servicio.
        List<MantenimientoResponseDTO> mantenimientos = mantenimientoService.obtenerMantenimientos();

        // Verifica el resultado obtenido.
        assertNotNull(mantenimientos);
        assertEquals(1, mantenimientos.size());
        assertEquals(1, mantenimientos.get(0).getIdMantenimiento());
        verify(mantenimientoRepository).findAll();
    }

    @Test
    public void testObtenerMantenimientoPorId() {
        // Define el comportamiento del repositorio simulado.
        when(mantenimientoRepository.findById(1)).thenReturn(Optional.of(mantenimiento));

        // Ejecuta el metodo del servicio.
        MantenimientoResponseDTO encontrado = mantenimientoService.obtenerMantenimientoPorId(1);

        // Verifica el resultado obtenido.
        assertNotNull(encontrado);
        assertEquals(1, encontrado.getIdMantenimiento());
        assertEquals("FINALIZADO", encontrado.getEstadoMantenimiento());
        verify(mantenimientoRepository).findById(1);
    }

    @Test
    public void testObtenerMantenimientosPorHabitacion() {
        // Simula la existencia de la habitacion y sus mantenimientos asociados.
        when(habitacionClient.obtenerHabitacionPorId(20)).thenReturn(crearHabitacion(20));
        when(mantenimientoRepository.findByIdHabitacion(20)).thenReturn(List.of(mantenimiento));

        // Ejecuta el metodo del servicio.
        List<MantenimientoResponseDTO> mantenimientos =
                mantenimientoService.obtenerMantenimientosPorHabitacion(20);

        // Verifica el resultado obtenido.
        assertNotNull(mantenimientos);
        assertEquals(1, mantenimientos.size());
        assertEquals(20, mantenimientos.get(0).getIdHabitacion());
        verify(habitacionClient).obtenerHabitacionPorId(20);
        verify(mantenimientoRepository).findByIdHabitacion(20);
    }

    @Test
    public void testObtenerMantenimientosPorEstado() {
        // Define el comportamiento del repositorio simulado.
        when(mantenimientoRepository.findByEstadoMantenimiento("FINALIZADO")).thenReturn(List.of(mantenimiento));

        // Ejecuta el metodo del servicio.
        List<MantenimientoResponseDTO> mantenimientos =
                mantenimientoService.obtenerMantenimientosPorEstado("FINALIZADO");

        // Verifica el resultado obtenido.
        assertNotNull(mantenimientos);
        assertEquals(1, mantenimientos.size());
        assertEquals("FINALIZADO", mantenimientos.get(0).getEstadoMantenimiento());
        verify(mantenimientoRepository).findByEstadoMantenimiento("FINALIZADO");
    }

    @Test
    public void testCrearMantenimiento() {
        // Simula las dependencias necesarias para crear el mantenimiento.
        when(habitacionClient.obtenerHabitacionPorId(20)).thenReturn(crearHabitacion(20));
        when(mantenimientoRepository.save(any(MantenimientoModel.class))).thenAnswer(invocation -> {
            MantenimientoModel mantenimiento = invocation.getArgument(0);
            mantenimiento.setIdMantenimiento(1);
            return mantenimiento;
        });

        // Ejecuta el metodo del servicio.
        MantenimientoResponseDTO guardado = mantenimientoService.crearMantenimiento(request);

        // Verifica el resultado obtenido.
        assertNotNull(guardado);
        assertEquals(1, guardado.getIdMantenimiento());
        assertEquals("Limpieza profunda", guardado.getTipoMantenimiento());
        verify(habitacionClient).obtenerHabitacionPorId(20);
        verify(mantenimientoRepository).save(any(MantenimientoModel.class));
    }

    @Test
    public void testActualizarMantenimiento() {
        // Configura la solicitud actualizada y simula sus dependencias.
        request = crearRequestActualizado();
        when(mantenimientoRepository.findById(1)).thenReturn(Optional.of(mantenimiento));
        when(habitacionClient.obtenerHabitacionPorId(21)).thenReturn(crearHabitacion(21));
        when(mantenimientoRepository.save(mantenimiento)).thenReturn(mantenimiento);

        // Ejecuta el metodo del servicio.
        MantenimientoResponseDTO actualizado = mantenimientoService.actualizarMantenimiento(1, request);

        // Verifica el resultado obtenido.
        assertNotNull(actualizado);
        assertEquals(21, actualizado.getIdHabitacion());
        assertEquals("EN_PROCESO", actualizado.getEstadoMantenimiento());
        verify(mantenimientoRepository).findById(1);
        verify(habitacionClient).obtenerHabitacionPorId(21);
        verify(mantenimientoRepository).save(mantenimiento);
    }

    @Test
    public void testEliminarMantenimiento() {
        // Simula la busqueda del mantenimiento que sera eliminado.
        when(mantenimientoRepository.findById(1)).thenReturn(Optional.of(mantenimiento));

        // Ejecuta el metodo del servicio.
        mantenimientoService.eliminarMantenimiento(1);

        // Verifica que el repositorio elimine el mantenimiento encontrado.
        verify(mantenimientoRepository).findById(1);
        verify(mantenimientoRepository).delete(mantenimiento);
    }

    @Test
    public void testObtenerMantenimientoPorIdInexistente() {
        // Simula que el mantenimiento solicitado no existe.
        when(mantenimientoRepository.findById(99)).thenReturn(Optional.empty());

        // Ejecuta el metodo y captura la excepcion esperada.
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> mantenimientoService.obtenerMantenimientoPorId(99)
        );

        // Verifica el mensaje de la excepcion.
        assertEquals("Mantenimiento no encontrado", exception.getMessage());
        verify(mantenimientoRepository).findById(99);
    }

    @Test
    public void testCrearMantenimientoConFechasIguales() {
        // Configura una solicitud con fechas incoherentes.
        request.setFechaFin(FECHA_INICIO);

        // Ejecuta el metodo y captura la excepcion esperada.
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> mantenimientoService.crearMantenimiento(request)
        );

        // Verifica la regla de negocio y que no se invoquen dependencias posteriores.
        assertEquals("La fecha de fin debe ser posterior a la fecha de inicio", exception.getMessage());
        verify(mantenimientoRepository, never()).save(any(MantenimientoModel.class));
        verify(habitacionClient, never()).obtenerHabitacionPorId(anyInt());
    }

    private MantenimientoModel crearMantenimientoModel() {
        return MantenimientoModel.builder()
                .idMantenimiento(1)
                .idHabitacion(20)
                .tipoMantenimiento("Limpieza profunda")
                .fechaInicio(FECHA_INICIO)
                .fechaFin(FECHA_FIN)
                .estadoMantenimiento("FINALIZADO")
                .build();
    }

    private MantenimientoRequestDTO crearRequest() {
        MantenimientoRequestDTO request = new MantenimientoRequestDTO();
        request.setIdHabitacion(20);
        request.setTipoMantenimiento("Limpieza profunda");
        request.setFechaInicio(FECHA_INICIO);
        request.setFechaFin(FECHA_FIN);
        request.setEstadoMantenimiento("FINALIZADO");
        return request;
    }

    private MantenimientoRequestDTO crearRequestActualizado() {
        MantenimientoRequestDTO request = new MantenimientoRequestDTO();
        request.setIdHabitacion(21);
        request.setTipoMantenimiento("Revision electrica");
        request.setFechaInicio(LocalDateTime.of(2026, 4, 3, 10, 0));
        request.setFechaFin(LocalDateTime.of(2026, 4, 3, 13, 30));
        request.setEstadoMantenimiento("EN_PROCESO");
        return request;
    }

    private HabitacionResponseDTO crearHabitacion(Integer idHabitacion) {
        return HabitacionResponseDTO.builder()
                .idHabitacion(idHabitacion)
                .idHotel(10)
                .tipoHabitacion("Simple")
                .numeroHabitacion("101")
                .estadoHabitacion("DISPONIBLE")
                .build();
    }
}
