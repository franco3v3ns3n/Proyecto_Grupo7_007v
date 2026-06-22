package com.veranum.cliente.services;

import com.veranum.cliente.dtos.request.ClienteRequestDTO;
import com.veranum.cliente.dtos.response.ClienteResponseDTO;
import com.veranum.cliente.exceptions.ResourceNotFoundException;
import com.veranum.cliente.models.ClienteModel;
import com.veranum.cliente.repositories.ClienteRepository;
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
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteModel cliente;
    private ClienteRequestDTO request;

    @BeforeEach
    void setUp() {
        // Configura los objetos de ejemplo antes de cada prueba.
        cliente = crearClienteModel();
        request = crearRequest();
    }

    @Test
    public void testObtenerClientes() {
        // Define el comportamiento del repositorio simulado.
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        // Ejecuta el metodo del servicio.
        List<ClienteResponseDTO> clientes = clienteService.obtenerClientes();

        // Verifica el resultado obtenido.
        assertNotNull(clientes);
        assertEquals(1, clientes.size());
        assertEquals(1, clientes.get(0).getIdCliente());
        verify(clienteRepository).findAll();
    }

    @Test
    public void testObtenerClientePorId() {
        // Define el comportamiento del repositorio simulado.
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        // Ejecuta el metodo del servicio.
        ClienteResponseDTO encontrado = clienteService.obtenerClientePorId(1);

        // Verifica el resultado obtenido.
        assertNotNull(encontrado);
        assertEquals(1, encontrado.getIdCliente());
        assertEquals("20123456-5", encontrado.getRut());
        verify(clienteRepository).findById(1);
    }

    @Test
    public void testCrearCliente() {
        // Simula el guardado del cliente con id generado.
        when(clienteRepository.save(any(ClienteModel.class))).thenAnswer(invocation -> {
            ClienteModel cliente = invocation.getArgument(0);
            cliente.setIdCliente(1);
            return cliente;
        });

        // Ejecuta el metodo del servicio.
        ClienteResponseDTO guardado = clienteService.crearCliente(request);

        // Verifica el resultado obtenido.
        assertNotNull(guardado);
        assertEquals(1, guardado.getIdCliente());
        assertEquals("Martina Sofia", guardado.getNombres());
        verify(clienteRepository).save(any(ClienteModel.class));
    }

    @Test
    public void testActualizarCliente() {
        // Configura la solicitud actualizada y simula sus dependencias.
        request = crearRequestActualizado();
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        // Ejecuta el metodo del servicio.
        ClienteResponseDTO actualizado = clienteService.actualizarCliente(1, request);

        // Verifica el resultado obtenido.
        assertNotNull(actualizado);
        assertEquals("Nicolas Andres", actualizado.getNombres());
        assertEquals("20987654-K", actualizado.getRut());
        verify(clienteRepository).findById(1);
        verify(clienteRepository).save(cliente);
    }

    @Test
    public void testEliminarCliente() {
        // Simula la busqueda del cliente que sera eliminado.
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        // Ejecuta el metodo del servicio.
        clienteService.eliminarCliente(1);

        // Verifica que el repositorio elimine el cliente encontrado.
        verify(clienteRepository).findById(1);
        verify(clienteRepository).delete(cliente);
    }

    @Test
    public void testObtenerClientePorIdInexistente() {
        // Simula que el cliente solicitado no existe.
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        // Ejecuta el metodo y captura la excepcion esperada.
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> clienteService.obtenerClientePorId(99)
        );

        // Verifica el mensaje de la excepcion.
        assertEquals("Cliente no encontrado", exception.getMessage());
        verify(clienteRepository).findById(99);
    }

    private ClienteModel crearClienteModel() {
        return ClienteModel.builder()
                .idCliente(1)
                .nombres("Martina Sofia")
                .apellidos("Silva Torres")
                .rut("20123456-5")
                .telefono("987654321")
                .correo("martina.silva@example.cl")
                .direccion("Las Condes")
                .build();
    }

    private ClienteRequestDTO crearRequest() {
        ClienteRequestDTO request = new ClienteRequestDTO();
        request.setNombres("Martina Sofia");
        request.setApellidos("Silva Torres");
        request.setRut("20123456-5");
        request.setTelefono("987654321");
        request.setCorreo("martina.silva@example.cl");
        request.setDireccion("Las Condes");
        return request;
    }

    private ClienteRequestDTO crearRequestActualizado() {
        ClienteRequestDTO request = new ClienteRequestDTO();
        request.setNombres("Nicolas Andres");
        request.setApellidos("Reyes Mora");
        request.setRut("20987654-K");
        request.setTelefono("976543210");
        request.setCorreo("nicolas.reyes@example.cl");
        request.setDireccion("La Florida");
        return request;
    }
}
