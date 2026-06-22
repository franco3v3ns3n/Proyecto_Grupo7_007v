package com.veranum.empleado.services;

import com.veranum.empleado.clients.HotelClient;
import com.veranum.empleado.dtos.request.EmpleadoRequestDTO;
import com.veranum.empleado.dtos.response.EmpleadoResponseDTO;
import com.veranum.empleado.dtos.response.HotelResponseDTO;
import com.veranum.empleado.exceptions.ResourceNotFoundException;
import com.veranum.empleado.models.EmpleadoModel;
import com.veranum.empleado.repositories.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private HotelClient hotelClient;

    @InjectMocks
    private EmpleadoService empleadoService;

    private EmpleadoModel empleado;
    private EmpleadoRequestDTO request;

    @BeforeEach
    void setUp() {
        // Configura los objetos de ejemplo antes de cada prueba.
        empleado = crearEmpleadoModel();
        request = crearRequest();
    }

    @Test
    public void testObtenerEmpleados() {
        // Define el comportamiento del repositorio simulado.
        when(empleadoRepository.findAll()).thenReturn(List.of(empleado));

        // Ejecuta el metodo del servicio.
        List<EmpleadoResponseDTO> empleados = empleadoService.obtenerEmpleados();

        // Verifica el resultado obtenido.
        assertNotNull(empleados);
        assertEquals(1, empleados.size());
        assertEquals(1, empleados.get(0).getIdEmpleado());
        verify(empleadoRepository).findAll();
    }

    @Test
    public void testObtenerEmpleadoPorId() {
        // Define el comportamiento del repositorio simulado.
        when(empleadoRepository.findById(1)).thenReturn(Optional.of(empleado));

        // Ejecuta el metodo del servicio.
        EmpleadoResponseDTO encontrado = empleadoService.obtenerEmpleadoPorId(1);

        // Verifica el resultado obtenido.
        assertNotNull(encontrado);
        assertEquals(1, encontrado.getIdEmpleado());
        assertEquals("ACTIVO", encontrado.getEstadoEmpleado());
        verify(empleadoRepository).findById(1);
    }

    @Test
    public void testObtenerEmpleadosPorHotel() {
        // Simula la existencia del hotel y los empleados asociados.
        when(hotelClient.obtenerHotelPorId(10)).thenReturn(crearHotel(10));
        when(empleadoRepository.findByIdHotel(10)).thenReturn(List.of(empleado));

        // Ejecuta el metodo del servicio.
        List<EmpleadoResponseDTO> empleados = empleadoService.obtenerEmpleadosPorHotel(10);

        // Verifica el resultado obtenido.
        assertNotNull(empleados);
        assertEquals(1, empleados.size());
        assertEquals(10, empleados.get(0).getIdHotel());
        verify(hotelClient).obtenerHotelPorId(10);
        verify(empleadoRepository).findByIdHotel(10);
    }

    @Test
    public void testObtenerEmpleadosPorEstado() {
        // Define el comportamiento del repositorio simulado.
        when(empleadoRepository.findByEstadoEmpleado("ACTIVO")).thenReturn(List.of(empleado));

        // Ejecuta el metodo del servicio.
        List<EmpleadoResponseDTO> empleados = empleadoService.obtenerEmpleadosPorEstado("ACTIVO");

        // Verifica el resultado obtenido.
        assertNotNull(empleados);
        assertEquals(1, empleados.size());
        assertEquals("ACTIVO", empleados.get(0).getEstadoEmpleado());
        verify(empleadoRepository).findByEstadoEmpleado("ACTIVO");
    }

    @Test
    public void testCrearEmpleado() {
        // Simula las dependencias necesarias para crear el empleado.
        when(hotelClient.obtenerHotelPorId(10)).thenReturn(crearHotel(10));
        when(empleadoRepository.save(any(EmpleadoModel.class))).thenAnswer(invocation -> {
            EmpleadoModel empleado = invocation.getArgument(0);
            empleado.setIdEmpleado(1);
            return empleado;
        });

        // Ejecuta el metodo del servicio.
        EmpleadoResponseDTO guardado = empleadoService.crearEmpleado(request);

        // Verifica el resultado obtenido.
        assertNotNull(guardado);
        assertEquals(1, guardado.getIdEmpleado());
        assertEquals("21436587-1", guardado.getRut());
        verify(hotelClient).obtenerHotelPorId(10);
        verify(empleadoRepository).save(any(EmpleadoModel.class));
    }

    @Test
    public void testActualizarEmpleado() {
        // Configura la solicitud actualizada y simula sus dependencias.
        request = crearRequestActualizado();
        when(empleadoRepository.findById(1)).thenReturn(Optional.of(empleado));
        when(hotelClient.obtenerHotelPorId(11)).thenReturn(crearHotel(11));
        when(empleadoRepository.save(empleado)).thenReturn(empleado);

        // Ejecuta el metodo del servicio.
        EmpleadoResponseDTO actualizado = empleadoService.actualizarEmpleado(1, request);

        // Verifica el resultado obtenido.
        assertNotNull(actualizado);
        assertEquals(11, actualizado.getIdHotel());
        assertEquals("INACTIVO", actualizado.getEstadoEmpleado());
        verify(empleadoRepository).findById(1);
        verify(hotelClient).obtenerHotelPorId(11);
        verify(empleadoRepository).save(empleado);
    }

    @Test
    public void testEliminarEmpleado() {
        // Simula la busqueda del empleado que sera eliminado.
        when(empleadoRepository.findById(1)).thenReturn(Optional.of(empleado));

        // Ejecuta el metodo del servicio.
        empleadoService.eliminarEmpleado(1);

        // Verifica que el repositorio elimine el empleado encontrado.
        verify(empleadoRepository).findById(1);
        verify(empleadoRepository).delete(empleado);
    }

    @Test
    public void testObtenerEmpleadoPorIdInexistente() {
        // Simula que el empleado solicitado no existe.
        when(empleadoRepository.findById(99)).thenReturn(Optional.empty());

        // Ejecuta el metodo y captura la excepcion esperada.
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> empleadoService.obtenerEmpleadoPorId(99)
        );

        // Verifica el mensaje de la excepcion.
        assertEquals("Empleado no encontrado", exception.getMessage());
        verify(empleadoRepository).findById(99);
    }

    private EmpleadoModel crearEmpleadoModel() {
        return EmpleadoModel.builder()
                .idEmpleado(1)
                .idHotel(10)
                .nombres("Ignacio Tomas")
                .apellidos("Lagos Vera")
                .rut("21436587-1")
                .tipoEmpleado("ADMINISTRADOR")
                .estadoEmpleado("ACTIVO")
                .build();
    }

    private EmpleadoRequestDTO crearRequest() {
        EmpleadoRequestDTO request = new EmpleadoRequestDTO();
        request.setIdHotel(10);
        request.setNombres("Ignacio Tomas");
        request.setApellidos("Lagos Vera");
        request.setRut("21436587-1");
        request.setTipoEmpleado("ADMINISTRADOR");
        request.setEstadoEmpleado("ACTIVO");
        return request;
    }

    private EmpleadoRequestDTO crearRequestActualizado() {
        EmpleadoRequestDTO request = new EmpleadoRequestDTO();
        request.setIdHotel(11);
        request.setNombres("Paula Andrea");
        request.setApellidos("Mora Diaz");
        request.setRut("22678901-4");
        request.setTipoEmpleado("RECEPCIONISTA");
        request.setEstadoEmpleado("INACTIVO");
        return request;
    }

    private HotelResponseDTO crearHotel(Integer idHotel) {
        return HotelResponseDTO.builder()
                .idHotel(idHotel)
                .nombre("Hotel Veranum")
                .ubicacion("Santiago")
                .build();
    }
}
