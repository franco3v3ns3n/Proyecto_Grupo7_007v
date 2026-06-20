package com.veranum.estadia.controllers;

import com.veranum.estadia.dtos.request.EstadiaRequestDTO;
import com.veranum.estadia.dtos.response.EstadiaResponseDTO;
import com.veranum.estadia.exceptions.ErrorResponse;
import com.veranum.estadia.services.EstadiaService;
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
@RequestMapping("/api/v1/estadias")
@Tag(name = "Estadías", description = "Operaciones relacionadas con la gestión de estadías")
public class EstadiaController {

    @Autowired
    private EstadiaService estadiaService;

    @Operation(summary = "Obtener todas las estadías", description = "Retorna todas las estadías registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EstadiaResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<EstadiaResponseDTO>> obtenerEstadias() {
        return ResponseEntity.ok(estadiaService.obtenerEstadias());
    }

    @Operation(summary = "Obtener estadía por ID", description = "Busca una estadía según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstadiaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Estadía no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{idEstadia}")
    public ResponseEntity<EstadiaResponseDTO> obtenerEstadiaPorId(
            @Parameter(description = "ID de la estadía", required = true)
            @PathVariable Integer idEstadia
    ) {
        return ResponseEntity.ok(estadiaService.obtenerEstadiaPorId(idEstadia));
    }

    @Operation(summary = "Obtener estadías por cliente", description = "Retorna estadías asociadas a un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EstadiaResponseDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con el microservicio cliente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<EstadiaResponseDTO>> obtenerEstadiasPorCliente(
            @Parameter(description = "ID del cliente", required = true)
            @PathVariable Integer idCliente
    ) {
        return ResponseEntity.ok(estadiaService.obtenerEstadiasPorCliente(idCliente));
    }

    @Operation(summary = "Obtener estadías por habitación", description = "Retorna estadías asociadas a una habitación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EstadiaResponseDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con el microservicio habitación",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/habitacion/{idHabitacion}")
    public ResponseEntity<List<EstadiaResponseDTO>> obtenerEstadiasPorHabitacion(
            @Parameter(description = "ID de la habitación", required = true)
            @PathVariable Integer idHabitacion
    ) {
        return ResponseEntity.ok(estadiaService.obtenerEstadiasPorHabitacion(idHabitacion));
    }

    @Operation(summary = "Obtener estadías por reserva", description = "Retorna estadías asociadas a una reserva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EstadiaResponseDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con el microservicio reserva",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<EstadiaResponseDTO>> obtenerEstadiasPorReserva(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable Integer idReserva
    ) {
        return ResponseEntity.ok(estadiaService.obtenerEstadiasPorReserva(idReserva));
    }

    @Operation(summary = "Obtener estadías por estado", description = "Filtra estadías por estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EstadiaResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/estado/{estadoEstadia}")
    public ResponseEntity<List<EstadiaResponseDTO>> obtenerEstadiasPorEstado(
            @Parameter(description = "Estado de la estadía", required = true)
            @PathVariable String estadoEstadia
    ) {
        return ResponseEntity.ok(
                estadiaService.obtenerEstadiasPorEstado(
                        estadoEstadia
                )
        );
    }

    @Operation(summary = "Crear estadía", description = "Crea una estadía validando cliente, habitación y reserva si fue informada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estadía creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstadiaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente, habitación o reserva no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con los microservicios cliente, habitación o reserva",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<EstadiaResponseDTO> crearEstadia(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para crear una estadía", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstadiaRequestDTO.class),
                            examples = @ExampleObject(name = "Estadía en curso",
                                    value = "{\"idCliente\":2,\"idHabitacion\":7,\"idReserva\":7,\"fechaCheckin\":\"2026-05-20T15:00:00\",\"fechaCheckout\":null,\"estadoEstadia\":\"EN_CURSO\"}")))
            @Valid @RequestBody EstadiaRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(estadiaService.crearEstadia(request));
    }

    @Operation(summary = "Actualizar estadía", description = "Actualiza una estadía existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadía actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstadiaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Estadía, cliente, habitación o reserva no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con los microservicios cliente, habitación o reserva",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{idEstadia}")
    public ResponseEntity<EstadiaResponseDTO> actualizarEstadia(
            @Parameter(description = "ID de la estadía", required = true)
            @PathVariable Integer idEstadia,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para actualizar una estadía", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstadiaRequestDTO.class),
                            examples = @ExampleObject(name = "Estadía finalizada",
                                    value = "{\"idCliente\":2,\"idHabitacion\":7,\"idReserva\":7,\"fechaCheckin\":\"2026-05-20T15:00:00\",\"fechaCheckout\":\"2026-05-23T11:00:00\",\"estadoEstadia\":\"FINALIZADA\"}")))
            @Valid @RequestBody EstadiaRequestDTO request
    ) {
        return ResponseEntity.ok(estadiaService.actualizarEstadia(idEstadia, request));
    }

    @Operation(summary = "Eliminar estadía", description = "Elimina una estadía existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Estadía eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Estadía no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{idEstadia}")
    public ResponseEntity<Void> eliminarEstadia(
            @Parameter(description = "ID de la estadía", required = true)
            @PathVariable Integer idEstadia
    ) {
        estadiaService.eliminarEstadia(idEstadia);
        return ResponseEntity.noContent().build();
    }
}
