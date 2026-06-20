package com.veranum.mantenimiento.controllers;

import com.veranum.mantenimiento.dtos.request.MantenimientoRequestDTO;
import com.veranum.mantenimiento.dtos.response.MantenimientoResponseDTO;
import com.veranum.mantenimiento.exceptions.ErrorResponse;
import com.veranum.mantenimiento.services.MantenimientoService;
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
@RequestMapping("/api/v1/mantenimientos")
@Tag(name = "Mantenimientos", description = "Operaciones relacionadas con la gestión de mantenimientos")
public class MantenimientoController {

    @Autowired
    private MantenimientoService mantenimientoService;

    @Operation(summary = "Obtener todos los mantenimientos", description = "Retorna todos los mantenimientos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MantenimientoResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<MantenimientoResponseDTO>> obtenerMantenimientos() {
        return ResponseEntity.ok(mantenimientoService.obtenerMantenimientos());
    }

    @Operation(summary = "Obtener mantenimiento por ID", description = "Busca un mantenimiento según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MantenimientoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{idMantenimiento}")
    public ResponseEntity<MantenimientoResponseDTO> obtenerMantenimientoPorId(
            @Parameter(description = "ID del mantenimiento", required = true)
            @PathVariable Integer idMantenimiento
    ) {
        return ResponseEntity.ok(
                mantenimientoService.obtenerMantenimientoPorId(idMantenimiento)
        );
    }

    @Operation(summary = "Obtener mantenimientos por habitación", description = "Retorna mantenimientos asociados a una habitación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MantenimientoResponseDTO.class)))),
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
    public ResponseEntity<List<MantenimientoResponseDTO>> obtenerMantenimientosPorHabitacion(
            @Parameter(description = "ID de la habitación", required = true)
            @PathVariable Integer idHabitacion
    ) {
        return ResponseEntity.ok(
                mantenimientoService.obtenerMantenimientosPorHabitacion(idHabitacion)
        );
    }

    @Operation(summary = "Obtener mantenimientos por estado", description = "Filtra mantenimientos según su estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MantenimientoResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/estado/{estadoMantenimiento}")
    public ResponseEntity<List<MantenimientoResponseDTO>> obtenerMantenimientosPorEstado(
            @Parameter(description = "Estado del mantenimiento", required = true)
            @PathVariable String estadoMantenimiento
    ) {
        return ResponseEntity.ok(
                mantenimientoService.obtenerMantenimientosPorEstado(
                        estadoMantenimiento
                )
        );
    }

    @Operation(summary = "Crear mantenimiento", description = "Crea un mantenimiento validando la habitación asociada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mantenimiento creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MantenimientoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
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
    @PostMapping
    public ResponseEntity<MantenimientoResponseDTO> crearMantenimiento(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un mantenimiento",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MantenimientoRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Mantenimiento finalizado",
                                    value = "{\"idHabitacion\":1,\"tipoMantenimiento\":\"Limpieza profunda\",\"fechaInicio\":\"2026-04-01T09:00:00\",\"fechaFin\":\"2026-04-01T12:00:00\",\"estadoMantenimiento\":\"FINALIZADO\"}"
                            )
                    )
            )
            @Valid @RequestBody MantenimientoRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mantenimientoService.crearMantenimiento(request));
    }

    @Operation(summary = "Actualizar mantenimiento", description = "Actualiza un mantenimiento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mantenimiento actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MantenimientoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Mantenimiento o habitación no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con el microservicio habitación",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{idMantenimiento}")
    public ResponseEntity<MantenimientoResponseDTO> actualizarMantenimiento(
            @Parameter(description = "ID del mantenimiento", required = true)
            @PathVariable Integer idMantenimiento,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para actualizar un mantenimiento",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MantenimientoRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Mantenimiento actualizado",
                                    value = "{\"idHabitacion\":2,\"tipoMantenimiento\":\"Revisión eléctrica\",\"fechaInicio\":\"2026-04-03T10:00:00\",\"fechaFin\":\"2026-04-03T13:30:00\",\"estadoMantenimiento\":\"FINALIZADO\"}"
                            )
                    )
            )
            @Valid @RequestBody MantenimientoRequestDTO request
    ) {
        return ResponseEntity.ok(
                mantenimientoService.actualizarMantenimiento(idMantenimiento, request)
        );
    }

    @Operation(summary = "Eliminar mantenimiento", description = "Elimina un mantenimiento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Mantenimiento eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{idMantenimiento}")
    public ResponseEntity<Void> eliminarMantenimiento(
            @Parameter(description = "ID del mantenimiento", required = true)
            @PathVariable Integer idMantenimiento
    ) {
        mantenimientoService.eliminarMantenimiento(idMantenimiento);

        return ResponseEntity.noContent().build();
    }
}
