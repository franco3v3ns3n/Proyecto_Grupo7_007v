package com.veranum.reserva.controllers;

import com.veranum.reserva.dtos.request.ReservaRequestDTO;
import com.veranum.reserva.dtos.response.ReservaResponseDTO;
import com.veranum.reserva.exceptions.ErrorResponse;
import com.veranum.reserva.services.ReservaService;
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
@RequestMapping("/api/v1/reservas")
// @Tag agrupa los endpoints de Reserva en una sección de Swagger UI.
@Tag(name = "Reservas", description = "Operaciones relacionadas con la gestión de reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // @Operation documenta la finalidad del endpoint mediante resumen y descripción.
    @Operation(summary = "Obtener todas las reservas", description = "Retorna todas las reservas registradas")
    // @ApiResponses agrupa las respuestas posibles del endpoint.
    // @ApiResponse documenta un código HTTP concreto.
    // @Content define el tipo y estructura del contenido de respuesta.
    // @ArraySchema representa una lista y @Schema(implementation = ...) indica el DTO de respuesta.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ReservaResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservas() {
        return ResponseEntity.ok(reservaService.obtenerReservas());
    }

    @Operation(summary = "Obtener reserva por ID", description = "Busca una reserva según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReservaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{idReserva}")
    public ResponseEntity<ReservaResponseDTO> obtenerReservaPorId(
            // @Parameter describe un parámetro de ruta recibido por el endpoint.
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable Integer idReserva
    ) {
        return ResponseEntity.ok(reservaService.obtenerReservaPorId(idReserva));
    }

    @Operation(summary = "Obtener reservas por cliente", description = "Retorna reservas asociadas a un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ReservaResponseDTO.class)))),
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
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservasPorCliente(
            @Parameter(description = "ID del cliente", required = true)
            @PathVariable Integer idCliente
    ) {
        return ResponseEntity.ok(reservaService.obtenerReservasPorCliente(idCliente));
    }

    @Operation(summary = "Obtener reservas por habitación", description = "Retorna reservas asociadas a una habitación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ReservaResponseDTO.class)))),
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
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservasPorHabitacion(
            @Parameter(description = "ID de la habitación", required = true)
            @PathVariable Integer idHabitacion
    ) {
        return ResponseEntity.ok(reservaService.obtenerReservasPorHabitacion(idHabitacion));
    }

    @Operation(summary = "Obtener reservas por estado", description = "Filtra reservas por estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ReservaResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/estado/{estadoReserva}")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservasPorEstado(
            @Parameter(description = "Estado de la reserva", required = true)
            @PathVariable String estadoReserva
    ) {
        return ResponseEntity.ok(
                reservaService.obtenerReservasPorEstado(
                        estadoReserva
                )
        );
    }

    @Operation(summary = "Crear reserva", description = "Crea una reserva validando cliente y habitación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReservaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente o habitación no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con los microservicios cliente o habitación",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crearReserva(
            // @RequestBody de OpenAPI solo documenta el cuerpo de la solicitud en Swagger.
            // @Content define que se envía JSON; @Schema indica el DTO de entrada.
            // @ExampleObject proporciona un JSON de ejemplo para probar el endpoint.
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para crear una reserva", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReservaRequestDTO.class),
                            examples = @ExampleObject(name = "Reserva confirmada",
                                    value = "{\"idCliente\":1,\"idHabitacion\":1,\"fechaInicio\":\"2026-07-20\",\"fechaFin\":\"2026-07-25\",\"estadoReserva\":\"CONFIRMADA\"}")))
            // @RequestBody de Spring recibe y deserializa realmente el JSON enviado.
            @Valid @RequestBody ReservaRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservaService.crearReserva(request));
    }

    @Operation(summary = "Actualizar reserva", description = "Actualiza una reserva existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReservaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reserva, cliente o habitación no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con los microservicios cliente o habitación",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{idReserva}")
    public ResponseEntity<ReservaResponseDTO> actualizarReserva(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable Integer idReserva,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para actualizar una reserva", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReservaRequestDTO.class),
                            examples = @ExampleObject(name = "Reserva actualizada",
                                    value = "{\"idCliente\":1,\"idHabitacion\":2,\"fechaInicio\":\"2026-08-01\",\"fechaFin\":\"2026-08-05\",\"estadoReserva\":\"CONFIRMADA\"}")))
            @Valid @RequestBody ReservaRequestDTO request
    ) {
        return ResponseEntity.ok(reservaService.actualizarReserva(idReserva, request));
    }

    @Operation(summary = "Eliminar reserva", description = "Elimina una reserva existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{idReserva}")
    public ResponseEntity<Void> eliminarReserva(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable Integer idReserva
    ) {
        reservaService.eliminarReserva(idReserva);
        return ResponseEntity.noContent().build();
    }
}
