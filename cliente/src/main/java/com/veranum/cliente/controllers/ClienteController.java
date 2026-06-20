package com.veranum.cliente.controllers;

import com.veranum.cliente.dtos.request.ClienteRequestDTO;
import com.veranum.cliente.dtos.response.ClienteResponseDTO;
import com.veranum.cliente.exceptions.ErrorResponse;
import com.veranum.cliente.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "Operaciones relacionadas con la gestión de clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Obtener todos los clientes", description = "Retorna todos los clientes registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ClienteResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> obtenerClientes() {
        return ResponseEntity.ok(clienteService.obtenerClientes());
    }

    @Operation(summary = "Obtener cliente por ID", description = "Busca un cliente según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{idCliente}")
    public ResponseEntity<ClienteResponseDTO> obtenerClientePorId(
            @Parameter(description = "ID del cliente", required = true)
            @PathVariable Integer idCliente
    ) {
        return ResponseEntity.ok(clienteService.obtenerClientePorId(idCliente));
    }

    @Operation(summary = "Crear cliente", description = "Crea un cliente con sus datos personales y de contacto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crearCliente(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para crear un cliente", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteRequestDTO.class),
                            examples = @ExampleObject(name = "Cliente Martina Sofia",
                                    value = "{\"nombres\":\"Martina Sofia\",\"apellidos\":\"Silva Torres\",\"rut\":\"20123456-5\",\"telefono\":\"987654321\",\"correo\":\"martina.silva@example.cl\",\"direccion\":\"Las Condes\"}")))
            @Valid @RequestBody ClienteRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteService.crearCliente(request));
    }

    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{idCliente}")
    public ResponseEntity<ClienteResponseDTO> actualizarCliente(
            @Parameter(description = "ID del cliente", required = true)
            @PathVariable Integer idCliente,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para actualizar un cliente", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteRequestDTO.class),
                            examples = @ExampleObject(name = "Cliente actualizado",
                                    value = "{\"nombres\":\"Nicolas Andres\",\"apellidos\":\"Reyes Mora\",\"rut\":\"20987654-K\",\"telefono\":\"976543210\",\"correo\":\"nicolas.reyes@example.cl\",\"direccion\":\"La Florida\"}")))
            @Valid @RequestBody ClienteRequestDTO request
    ) {
        return ResponseEntity.ok(clienteService.actualizarCliente(idCliente, request));
    }

    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{idCliente}")
    public ResponseEntity<Void> eliminarCliente(
            @Parameter(description = "ID del cliente", required = true)
            @PathVariable Integer idCliente
    ) {
        clienteService.eliminarCliente(idCliente);
        return ResponseEntity.noContent().build();
    }
}
