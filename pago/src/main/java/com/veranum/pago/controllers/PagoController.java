package com.veranum.pago.controllers;

import com.veranum.pago.dtos.request.PagoRequestDTO;
import com.veranum.pago.dtos.response.PagoResponseDTO;
import com.veranum.pago.exceptions.ErrorResponse;
import com.veranum.pago.services.PagoService;
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
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pagos", description = "Operaciones relacionadas con la gestión de pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Operation(summary = "Obtener todos los pagos", description = "Retorna todos los pagos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PagoResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> obtenerPagos() {
        return ResponseEntity.ok(pagoService.obtenerPagos());
    }

    @Operation(summary = "Obtener pago por ID", description = "Busca un pago según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{idPago}")
    public ResponseEntity<PagoResponseDTO> obtenerPagoPorId(
            @Parameter(description = "ID del pago", required = true)
            @PathVariable Integer idPago
    ) {
        return ResponseEntity.ok(pagoService.obtenerPagoPorId(idPago));
    }

    @Operation(summary = "Obtener pagos por estadía", description = "Retorna pagos asociados a una estadía")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PagoResponseDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Estadía no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con el microservicio estadía",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/estadia/{idEstadia}")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPagosPorEstadia(
            @Parameter(description = "ID de la estadía", required = true)
            @PathVariable Integer idEstadia
    ) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorEstadia(idEstadia));
    }

    @Operation(summary = "Obtener pagos por estado", description = "Filtra pagos según su estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PagoResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/estado/{estadoPago}")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPagosPorEstado(
            @Parameter(description = "Estado del pago", required = true)
            @PathVariable String estadoPago
    ) {
        return ResponseEntity.ok(
                pagoService.obtenerPagosPorEstado(
                        estadoPago
                )
        );
    }

    @Operation(summary = "Crear pago", description = "Crea un pago validando la estadía asociada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Estadía no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con el microservicio estadía",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<PagoResponseDTO> crearPago(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un pago",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagoRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Pago con tarjeta de crédito",
                                    value = "{\"idEstadia\":1,\"monto\":240000,\"metodoPago\":\"TARJETA_CREDITO\",\"estadoPago\":\"PAGADO\",\"fechaPago\":\"2026-04-13T10:50:00\"}"
                            )
                    )
            )
            @Valid @RequestBody PagoRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pagoService.crearPago(request));
    }

    @Operation(summary = "Actualizar pago", description = "Actualiza un pago existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pago o estadía no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con el microservicio estadía",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{idPago}")
    public ResponseEntity<PagoResponseDTO> actualizarPago(
            @Parameter(description = "ID del pago", required = true)
            @PathVariable Integer idPago,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para actualizar un pago",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagoRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Pago actualizado",
                                    value = "{\"idEstadia\":2,\"monto\":270000,\"metodoPago\":\"TARJETA_DEBITO\",\"estadoPago\":\"PAGADO\",\"fechaPago\":\"2026-04-18T11:15:00\"}"
                            )
                    )
            )
            @Valid @RequestBody PagoRequestDTO request
    ) {
        return ResponseEntity.ok(
                pagoService.actualizarPago(idPago, request)
        );
    }

    @Operation(summary = "Eliminar pago", description = "Elimina un pago existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pago eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{idPago}")
    public ResponseEntity<Void> eliminarPago(
            @Parameter(description = "ID del pago", required = true)
            @PathVariable Integer idPago
    ) {
        pagoService.eliminarPago(idPago);

        return ResponseEntity.noContent().build();
    }
}
