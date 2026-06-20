package com.veranum.habitacion.controllers;

import com.veranum.habitacion.dtos.request.HabitacionRequestDTO;
import com.veranum.habitacion.dtos.response.HabitacionResponseDTO;
import com.veranum.habitacion.exceptions.ErrorResponse;
import com.veranum.habitacion.services.HabitacionService;
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
@RequestMapping("/api/v1/habitaciones")
@Tag(name = "Habitaciones", description = "Operaciones relacionadas con la gestión de habitaciones")
public class HabitacionController {

    @Autowired
    private HabitacionService habitacionService;

    @Operation(summary = "Obtener todas las habitaciones", description = "Retorna todas las habitaciones registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HabitacionResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<HabitacionResponseDTO>> obtenerHabitaciones() {
        return ResponseEntity.ok(habitacionService.obtenerHabitaciones());
    }

    @Operation(summary = "Obtener habitación por ID", description = "Busca una habitación según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HabitacionResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{idHabitacion}")
    public ResponseEntity<HabitacionResponseDTO> obtenerHabitacionPorId(
            @Parameter(description = "ID de la habitación", required = true)
            @PathVariable Integer idHabitacion
    ) {
        return ResponseEntity.ok(habitacionService.obtenerHabitacionPorId(idHabitacion));
    }

    @Operation(summary = "Obtener habitaciones por hotel", description = "Retorna habitaciones asociadas a un hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HabitacionResponseDTO.class)))),
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
    public ResponseEntity<List<HabitacionResponseDTO>> obtenerHabitacionesPorHotel(
            @Parameter(description = "ID del hotel", required = true)
            @PathVariable Integer idHotel
    ) {
        return ResponseEntity.ok(habitacionService.obtenerHabitacionesPorHotel(idHotel));
    }

    @Operation(summary = "Obtener habitaciones por hotel y tipo", description = "Filtra habitaciones por hotel y tipo de habitación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HabitacionResponseDTO.class)))),
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
    @GetMapping("/hotel/{idHotel}/tipo/{tipoHabitacion}")
    public ResponseEntity<List<HabitacionResponseDTO>> obtenerHabitacionesPorHotelYTipo(
            @Parameter(description = "ID del hotel", required = true)
            @PathVariable Integer idHotel,
            @Parameter(description = "Tipo de habitación", required = true)
            @PathVariable String tipoHabitacion
    ) {
        return ResponseEntity.ok(
                habitacionService.obtenerHabitacionesPorHotelYTipo(
                        idHotel,
                        tipoHabitacion
                )
        );
    }

    @Operation(summary = "Obtener habitaciones por hotel y estado", description = "Filtra habitaciones por hotel y estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HabitacionResponseDTO.class)))),
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
    @GetMapping("/hotel/{idHotel}/estado/{estadoHabitacion}")
    public ResponseEntity<List<HabitacionResponseDTO>> obtenerHabitacionesPorHotelYEstado(
            @Parameter(description = "ID del hotel", required = true)
            @PathVariable Integer idHotel,
            @Parameter(description = "Estado de la habitación", required = true)
            @PathVariable String estadoHabitacion
    ) {
        return ResponseEntity.ok(
                habitacionService.obtenerHabitacionesPorHotelYEstado(
                        idHotel,
                        estadoHabitacion
                )
        );
    }

    @Operation(summary = "Crear habitación", description = "Crea una habitación validando el hotel asociado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Habitación creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HabitacionResponseDTO.class))),
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
    public ResponseEntity<HabitacionResponseDTO> crearHabitacion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para crear una habitación", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HabitacionRequestDTO.class),
                            examples = @ExampleObject(name = "Habitación simple",
                                    value = "{\"idHotel\":1,\"tipoHabitacion\":\"Simple\",\"numeroHabitacion\":\"101\",\"capacidadPersonas\":1,\"cantidadCamas\":1,\"cantidadBanos\":1,\"precioDiario\":45000,\"estadoHabitacion\":\"DISPONIBLE\"}")))
            @Valid @RequestBody HabitacionRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(habitacionService.crearHabitacion(request));
    }

    @Operation(summary = "Actualizar habitación", description = "Actualiza una habitación existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habitación actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HabitacionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Habitación u hotel no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con el microservicio hotel",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{idHabitacion}")
    public ResponseEntity<HabitacionResponseDTO> actualizarHabitacion(
            @Parameter(description = "ID de la habitación", required = true)
            @PathVariable Integer idHabitacion,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para actualizar una habitación", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HabitacionRequestDTO.class),
                            examples = @ExampleObject(name = "Habitación actualizada",
                                    value = "{\"idHotel\":1,\"tipoHabitacion\":\"Doble\",\"numeroHabitacion\":\"102\",\"capacidadPersonas\":2,\"cantidadCamas\":2,\"cantidadBanos\":1,\"precioDiario\":65000,\"estadoHabitacion\":\"DISPONIBLE\"}")))
            @Valid @RequestBody HabitacionRequestDTO request
    ) {
        return ResponseEntity.ok(habitacionService.actualizarHabitacion(idHabitacion, request));
    }

    @Operation(summary = "Eliminar habitación", description = "Elimina una habitación existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Habitación eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{idHabitacion}")
    public ResponseEntity<Void> eliminarHabitacion(
            @Parameter(description = "ID de la habitación", required = true)
            @PathVariable Integer idHabitacion
    ) {
        habitacionService.eliminarHabitacion(idHabitacion);
        return ResponseEntity.noContent().build();
    }
}
