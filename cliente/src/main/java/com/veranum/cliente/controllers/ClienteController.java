package com.veranum.cliente.controllers;

import com.veranum.cliente.dtos.request.ClienteRequestDTO;
import com.veranum.cliente.dtos.response.ClienteResponseDTO;
import com.veranum.cliente.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> obtenerClientes() {
        return ResponseEntity.ok(clienteService.obtenerClientes());
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<ClienteResponseDTO> obtenerClientePorId(
            @PathVariable Integer idCliente
    ) {
        return ResponseEntity.ok(clienteService.obtenerClientePorId(idCliente));
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crearCliente(
            @Valid @RequestBody ClienteRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteService.crearCliente(request));
    }

    @PutMapping("/{idCliente}")
    public ResponseEntity<ClienteResponseDTO> actualizarCliente(
            @PathVariable Integer idCliente,
            @Valid @RequestBody ClienteRequestDTO request
    ) {
        return ResponseEntity.ok(clienteService.actualizarCliente(idCliente, request));
    }

    @DeleteMapping("/{idCliente}")
    public ResponseEntity<Void> eliminarCliente(
            @PathVariable Integer idCliente
    ) {
        clienteService.eliminarCliente(idCliente);
        return ResponseEntity.noContent().build();
    }
}
