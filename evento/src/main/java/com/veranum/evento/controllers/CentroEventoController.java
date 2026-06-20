package com.veranum.evento.controllers;

import com.veranum.evento.dtos.request.CentroEventoRequestDTO;
import com.veranum.evento.dtos.response.CentroEventoResponseDTO;
import com.veranum.evento.exceptions.ErrorResponse;
import com.veranum.evento.services.CentroEventoService;
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
@RequestMapping("/api/v1/eventos")
@Tag(name = "Centros de Eventos", description = "Operaciones relacionadas con la gestión de centros de eventos")
public class CentroEventoController {

    @Autowired
    private CentroEventoService centroEventoService;

    @Operation(summary = "Obtener todos los centros de eventos", description = "Retorna todos los centros de eventos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CentroEventoResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<CentroEventoResponseDTO>> obtenerCentrosEventos() {
        return ResponseEntity.ok(centroEventoService.obtenerCentrosEventos());
    }

    @Operation(summary = "Obtener centro de evento por ID", description = "Busca un centro de evento según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CentroEventoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Centro de evento no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{idCentroEvento}")
    public ResponseEntity<CentroEventoResponseDTO> obtenerCentroEventoPorId(
            @Parameter(description = "ID del centro de evento", required = true)
            @PathVariable Integer idCentroEvento
    ) {
        return ResponseEntity.ok(
                centroEventoService.obtenerCentroEventoPorId(idCentroEvento)
        );
    }

    @Operation(summary = "Obtener centros de eventos por hotel", description = "Retorna centros de eventos asociados a un hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CentroEventoResponseDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Hotel no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con el microservicio hotel",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/hotel/{idHotel}")
    public ResponseEntity<List<CentroEventoResponseDTO>> obtenerCentrosEventosPorHotel(
            @Parameter(description = "ID del hotel", required = true)
            @PathVariable Integer idHotel
    ) {
        return ResponseEntity.ok(
                centroEventoService.obtenerCentrosEventosPorHotel(idHotel)
        );
    }

    @Operation(summary = "Obtener centros de eventos por estado", description = "Filtra centros de eventos según su estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CentroEventoResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/estado/{estadoCentroEvento}")
    public ResponseEntity<List<CentroEventoResponseDTO>> obtenerCentrosEventosPorEstado(
            @Parameter(description = "Estado del centro de evento", required = true)
            @PathVariable String estadoCentroEvento
    ) {
        return ResponseEntity.ok(
                centroEventoService.obtenerCentrosEventosPorEstado(
                        estadoCentroEvento
                )
        );
    }

    @Operation(summary = "Crear centro de evento", description = "Crea un centro de evento validando el hotel asociado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Centro de evento creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CentroEventoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Hotel no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con el microservicio hotel",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<CentroEventoResponseDTO> crearCentroEvento(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un centro de evento",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CentroEventoRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Centro de evento activo",
                                    value = "{\"idHotel\":1,\"nombre\":\"Salón Pacífico\",\"capacidadPersonas\":150,\"precioCentroEvento\":350000,\"estadoCentroEvento\":\"ACTIVO\"}"
                            )
                    )
            )
            @Valid @RequestBody CentroEventoRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(centroEventoService.crearCentroEvento(request));
    }

    @Operation(summary = "Actualizar centro de evento", description = "Actualiza un centro de evento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Centro de evento actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CentroEventoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Centro de evento u hotel no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con el microservicio hotel",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{idCentroEvento}")
    public ResponseEntity<CentroEventoResponseDTO> actualizarCentroEvento(
            @Parameter(description = "ID del centro de evento", required = true)
            @PathVariable Integer idCentroEvento,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para actualizar un centro de evento",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CentroEventoRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Centro de evento actualizado",
                                    value = "{\"idHotel\":2,\"nombre\":\"Salón Vista Mar\",\"capacidadPersonas\":120,\"precioCentroEvento\":280000,\"estadoCentroEvento\":\"ACTIVO\"}"
                            )
                    )
            )
            @Valid @RequestBody CentroEventoRequestDTO request
    ) {
        return ResponseEntity.ok(
                centroEventoService.actualizarCentroEvento(idCentroEvento, request)
        );
    }

    @Operation(summary = "Eliminar centro de evento", description = "Elimina un centro de evento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Centro de evento eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Centro de evento no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{idCentroEvento}")
    public ResponseEntity<Void> eliminarCentroEvento(
            @Parameter(description = "ID del centro de evento", required = true)
            @PathVariable Integer idCentroEvento
    ) {
        centroEventoService.eliminarCentroEvento(idCentroEvento);

        return ResponseEntity.noContent().build();
    }
}
