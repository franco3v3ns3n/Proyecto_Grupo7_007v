package com.veranum.servicio.controllers;

import com.veranum.servicio.dtos.request.ServicioRequestDTO;
import com.veranum.servicio.dtos.response.ServicioResponseDTO;
import com.veranum.servicio.exceptions.ErrorResponse;
import com.veranum.servicio.services.ServicioService;
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
@RequestMapping("/api/v1/servicios")
@Tag(name = "Servicios", description = "Operaciones relacionadas con la gestión de servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @Operation(summary = "Obtener todos los servicios", description = "Retorna todos los servicios registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ServicioResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ServicioResponseDTO>> obtenerServicios() {
        return ResponseEntity.ok(servicioService.obtenerServicios());
    }

    @Operation(summary = "Obtener servicio por ID", description = "Busca un servicio según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServicioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{idServicio}")
    public ResponseEntity<ServicioResponseDTO> obtenerServicioPorId(
            @Parameter(description = "ID del servicio", required = true)
            @PathVariable Integer idServicio
    ) {
        return ResponseEntity.ok(servicioService.obtenerServicioPorId(idServicio));
    }

    @Operation(summary = "Obtener servicios por hotel", description = "Retorna servicios asociados a un hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ServicioResponseDTO.class)))),
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
    public ResponseEntity<List<ServicioResponseDTO>> obtenerServiciosPorHotel(
            @Parameter(description = "ID del hotel", required = true)
            @PathVariable Integer idHotel
    ) {
        return ResponseEntity.ok(servicioService.obtenerServiciosPorHotel(idHotel));
    }

    @Operation(summary = "Obtener servicios por estado", description = "Filtra servicios según su estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ServicioResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/estado/{estadoServicio}")
    public ResponseEntity<List<ServicioResponseDTO>> obtenerServiciosPorEstado(
            @Parameter(description = "Estado del servicio", required = true)
            @PathVariable String estadoServicio
    ) {
        return ResponseEntity.ok(
                servicioService.obtenerServiciosPorEstado(
                        estadoServicio
                )
        );
    }

    @Operation(summary = "Crear servicio", description = "Crea un servicio validando el hotel asociado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Servicio creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServicioResponseDTO.class))),
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
    public ResponseEntity<ServicioResponseDTO> crearServicio(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un servicio",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServicioRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Servicio WiFi Premium",
                                    value = "{\"idHotel\":1,\"nombre\":\"WiFi Premium\",\"valorDiario\":5000,\"estadoServicio\":\"ACTIVO\"}"
                            )
                    )
            )
            @Valid @RequestBody ServicioRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(servicioService.crearServicio(request));
    }

    @Operation(summary = "Actualizar servicio", description = "Actualiza un servicio existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Servicio actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServicioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Servicio u hotel no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con el microservicio hotel",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{idServicio}")
    public ResponseEntity<ServicioResponseDTO> actualizarServicio(
            @Parameter(description = "ID del servicio", required = true)
            @PathVariable Integer idServicio,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para actualizar un servicio",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServicioRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Servicio actualizado",
                                    value = "{\"idHotel\":1,\"nombre\":\"Lavandería\",\"valorDiario\":8000,\"estadoServicio\":\"ACTIVO\"}"
                            )
                    )
            )
            @Valid @RequestBody ServicioRequestDTO request
    ) {
        return ResponseEntity.ok(servicioService.actualizarServicio(idServicio, request));
    }

    @Operation(summary = "Eliminar servicio", description = "Elimina un servicio existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Servicio eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{idServicio}")
    public ResponseEntity<Void> eliminarServicio(
            @Parameter(description = "ID del servicio", required = true)
            @PathVariable Integer idServicio
    ) {
        servicioService.eliminarServicio(idServicio);
        return ResponseEntity.noContent().build();
    }
}
