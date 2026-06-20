package com.veranum.empleado.controllers;

import com.veranum.empleado.dtos.request.EmpleadoRequestDTO;
import com.veranum.empleado.dtos.response.EmpleadoResponseDTO;
import com.veranum.empleado.exceptions.ErrorResponse;
import com.veranum.empleado.services.EmpleadoService;
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
@RequestMapping("/api/v1/empleados")
@Tag(name = "Empleados", description = "Operaciones relacionadas con la gestión de empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Operation(summary = "Obtener todos los empleados", description = "Retorna todos los empleados registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmpleadoResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<EmpleadoResponseDTO>> obtenerEmpleados() {
        return ResponseEntity.ok(empleadoService.obtenerEmpleados());
    }

    @Operation(summary = "Obtener empleado por ID", description = "Busca un empleado según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmpleadoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{idEmpleado}")
    public ResponseEntity<EmpleadoResponseDTO> obtenerEmpleadoPorId(
            @Parameter(description = "ID del empleado", required = true)
            @PathVariable Integer idEmpleado
    ) {
        return ResponseEntity.ok(empleadoService.obtenerEmpleadoPorId(idEmpleado));
    }

    @Operation(summary = "Obtener empleados por hotel", description = "Retorna empleados asociados a un hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmpleadoResponseDTO.class)))),
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
    public ResponseEntity<List<EmpleadoResponseDTO>> obtenerEmpleadosPorHotel(
            @Parameter(description = "ID del hotel", required = true)
            @PathVariable Integer idHotel
    ) {
        return ResponseEntity.ok(empleadoService.obtenerEmpleadosPorHotel(idHotel));
    }

    @Operation(summary = "Obtener empleados por estado", description = "Filtra empleados según su estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmpleadoResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/estado/{estadoEmpleado}")
    public ResponseEntity<List<EmpleadoResponseDTO>> obtenerEmpleadosPorEstado(
            @Parameter(description = "Estado del empleado", required = true)
            @PathVariable String estadoEmpleado
    ) {
        return ResponseEntity.ok(
                empleadoService.obtenerEmpleadosPorEstado(
                        estadoEmpleado
                )
        );
    }

    @Operation(summary = "Crear empleado", description = "Crea un empleado validando el hotel asociado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleado creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmpleadoResponseDTO.class))),
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
    public ResponseEntity<EmpleadoResponseDTO> crearEmpleado(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un empleado",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmpleadoRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Empleado administrador",
                                    value = "{\"idHotel\":1,\"nombres\":\"Carlos Andrés\",\"apellidos\":\"Muñoz Rojas\",\"rut\":\"11111111-1\",\"tipoEmpleado\":\"ADMINISTRADOR\",\"estadoEmpleado\":\"ACTIVO\"}"
                            )
                    )
            )
            @Valid @RequestBody EmpleadoRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empleadoService.crearEmpleado(request));
    }

    @Operation(summary = "Actualizar empleado", description = "Actualiza un empleado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmpleadoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Empleado u hotel no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error al comunicarse con el microservicio hotel",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{idEmpleado}")
    public ResponseEntity<EmpleadoResponseDTO> actualizarEmpleado(
            @Parameter(description = "ID del empleado", required = true)
            @PathVariable Integer idEmpleado,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para actualizar un empleado",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmpleadoRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Empleado actualizado",
                                    value = "{\"idHotel\":1,\"nombres\":\"Fernanda Paz\",\"apellidos\":\"Soto Valdés\",\"rut\":\"12222222-2\",\"tipoEmpleado\":\"RECEPCIONISTA\",\"estadoEmpleado\":\"ACTIVO\"}"
                            )
                    )
            )
            @Valid @RequestBody EmpleadoRequestDTO request
    ) {
        return ResponseEntity.ok(
                empleadoService.actualizarEmpleado(idEmpleado, request)
        );
    }

    @Operation(summary = "Eliminar empleado", description = "Elimina un empleado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empleado eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{idEmpleado}")
    public ResponseEntity<Void> eliminarEmpleado(
            @Parameter(description = "ID del empleado", required = true)
            @PathVariable Integer idEmpleado
    ) {
        empleadoService.eliminarEmpleado(idEmpleado);

        return ResponseEntity.noContent().build();
    }
}
