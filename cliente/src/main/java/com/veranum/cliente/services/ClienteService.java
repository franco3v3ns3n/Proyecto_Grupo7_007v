package com.veranum.cliente.services;

import com.veranum.cliente.dtos.request.ClienteRequestDTO;
import com.veranum.cliente.dtos.response.ClienteResponseDTO;
import com.veranum.cliente.exceptions.ResourceNotFoundException;
import com.veranum.cliente.models.ClienteModel;
import com.veranum.cliente.repositories.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteResponseDTO> obtenerClientes() {
        log.info("Obteniendo clientes");

        return clienteRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ClienteResponseDTO obtenerClientePorId(Integer idCliente) {
        log.info("Buscando cliente con id: {}", idCliente);

        ClienteModel cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        return mapToResponse(cliente);
    }

    public ClienteResponseDTO crearCliente(ClienteRequestDTO request) {
        log.info("Creando cliente: {}", request.getRut());

        ClienteModel cliente = ClienteModel.builder()
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .rut(request.getRut())
                .telefono(request.getTelefono())
                .correo(request.getCorreo())
                .direccion(request.getDireccion())
                .build();

        ClienteModel clienteGuardado = clienteRepository.save(cliente);

        return mapToResponse(clienteGuardado);
    }

    public ClienteResponseDTO actualizarCliente(Integer idCliente, ClienteRequestDTO request) {
        log.info("Actualizando cliente con id: {}", idCliente);

        ClienteModel cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        cliente.setNombres(request.getNombres());
        cliente.setApellidos(request.getApellidos());
        cliente.setRut(request.getRut());
        cliente.setTelefono(request.getTelefono());
        cliente.setCorreo(request.getCorreo());
        cliente.setDireccion(request.getDireccion());

        ClienteModel clienteActualizado = clienteRepository.save(cliente);

        return mapToResponse(clienteActualizado);
    }

    public void eliminarCliente(Integer idCliente) {
        log.info("Eliminando cliente con id: {}", idCliente);

        ClienteModel cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        clienteRepository.delete(cliente);
    }

    private ClienteResponseDTO mapToResponse(ClienteModel cliente) {
        return ClienteResponseDTO.builder()
                .idCliente(cliente.getIdCliente())
                .nombres(cliente.getNombres())
                .apellidos(cliente.getApellidos())
                .rut(cliente.getRut())
                .telefono(cliente.getTelefono())
                .correo(cliente.getCorreo())
                .direccion(cliente.getDireccion())
                .build();
    }
}
