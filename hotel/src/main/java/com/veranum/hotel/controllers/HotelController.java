package com.veranum.hotel.controllers;

import com.veranum.hotel.dtos.request.HotelRequestDTO;
import com.veranum.hotel.dtos.response.HotelResponseDTO;
import com.veranum.hotel.exceptions.ErrorResponse;
import com.veranum.hotel.services.HotelService;
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
@RequestMapping("/api/v1/hoteles")
@Tag(name = "Hoteles", description = "Operaciones relacionadas con la gestión de hoteles")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Operation(summary = "Obtener todos los hoteles", description = "Retorna todos los hoteles registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HotelResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<HotelResponseDTO>> obtenerHoteles() {

        return ResponseEntity.ok(hotelService.obtenerHoteles());
    }

    @Operation(summary = "Obtener hotel por ID", description = "Busca un hotel según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Hotel no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{idHotel}")
    public ResponseEntity<HotelResponseDTO> obtenerHotelPorId(
            @Parameter(description = "ID del hotel", required = true)
            @PathVariable Integer idHotel
    ) {

        return ResponseEntity.ok(hotelService.obtenerHotelPorId(idHotel));
    }

    @Operation(summary = "Crear hotel", description = "Crea un hotel con nombre y ubicación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<HotelResponseDTO> crearHotel(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para crear un hotel", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelRequestDTO.class),
                            examples = @ExampleObject(name = "Hotel Santiago",
                                    value = "{\"nombre\":\"Hotel Veranum Santiago\",\"ubicacion\":\"Santiago\"}")))
            @Valid @RequestBody HotelRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(hotelService.crearHotel(request));
    }

    @Operation(summary = "Actualizar hotel", description = "Actualiza los datos de un hotel existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Hotel no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{idHotel}")
    public ResponseEntity<HotelResponseDTO> actualizarHotel(
            @Parameter(description = "ID del hotel", required = true)
            @PathVariable Integer idHotel,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para actualizar un hotel", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelRequestDTO.class),
                            examples = @ExampleObject(name = "Hotel actualizado",
                                    value = "{\"nombre\":\"Hotel Veranum Valparaiso\",\"ubicacion\":\"Valparaiso\"}")))
            @Valid @RequestBody HotelRequestDTO request
    ) {

        return ResponseEntity.ok(
                hotelService.actualizarHotel(idHotel, request)
        );
    }

    @Operation(summary = "Eliminar hotel", description = "Elimina un hotel existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Hotel eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Hotel no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{idHotel}")
    public ResponseEntity<Void> eliminarHotel(
            @Parameter(description = "ID del hotel", required = true)
            @PathVariable Integer idHotel
    ) {

        hotelService.eliminarHotel(idHotel);

        return ResponseEntity.noContent().build();
    }
}
