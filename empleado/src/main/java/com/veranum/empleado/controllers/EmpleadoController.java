package com.veranum.empleado.controllers;

import com.veranum.empleado.dtos.request.EmpleadoRequestDTO;
import com.veranum.empleado.dtos.response.EmpleadoResponseDTO;
import com.veranum.empleado.services.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoResponseDTO>> obtenerEmpleados() {
        return ResponseEntity.ok(empleadoService.obtenerEmpleados());
    }

    @GetMapping("/{idEmpleado}")
    public ResponseEntity<EmpleadoResponseDTO> obtenerEmpleadoPorId(
            @PathVariable Integer idEmpleado
    ) {
        return ResponseEntity.ok(empleadoService.obtenerEmpleadoPorId(idEmpleado));
    }

    @GetMapping("/hotel/{idHotel}")
    public ResponseEntity<List<EmpleadoResponseDTO>> obtenerEmpleadosPorHotel(
            @PathVariable Integer idHotel
    ) {
        return ResponseEntity.ok(empleadoService.obtenerEmpleadosPorHotel(idHotel));
    }

    @GetMapping("/estado/{estadoEmpleado}")
    public ResponseEntity<List<EmpleadoResponseDTO>> obtenerEmpleadosPorEstado(
            @PathVariable String estadoEmpleado
    ) {
        return ResponseEntity.ok(
                empleadoService.obtenerEmpleadosPorEstado(
                        estadoEmpleado
                )
        );
    }    

    @PostMapping
    public ResponseEntity<EmpleadoResponseDTO> crearEmpleado(
            @Valid @RequestBody EmpleadoRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empleadoService.crearEmpleado(request));
    }

    @PutMapping("/{idEmpleado}")
    public ResponseEntity<EmpleadoResponseDTO> actualizarEmpleado(
            @PathVariable Integer idEmpleado,
            @Valid @RequestBody EmpleadoRequestDTO request
    ) {
        return ResponseEntity.ok(
                empleadoService.actualizarEmpleado(idEmpleado, request)
        );
    }

    @DeleteMapping("/{idEmpleado}")
    public ResponseEntity<Void> eliminarEmpleado(
            @PathVariable Integer idEmpleado
    ) {
        empleadoService.eliminarEmpleado(idEmpleado);

        return ResponseEntity.noContent().build();
    }
}
