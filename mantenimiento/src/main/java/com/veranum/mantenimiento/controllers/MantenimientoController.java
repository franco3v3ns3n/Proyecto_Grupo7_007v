package com.veranum.mantenimiento.controllers;

import com.veranum.mantenimiento.dtos.request.MantenimientoRequestDTO;
import com.veranum.mantenimiento.dtos.response.MantenimientoResponseDTO;
import com.veranum.mantenimiento.services.MantenimientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mantenimientos")
public class MantenimientoController {

    @Autowired
    private MantenimientoService mantenimientoService;

    @GetMapping
    public ResponseEntity<List<MantenimientoResponseDTO>> obtenerMantenimientos() {
        return ResponseEntity.ok(mantenimientoService.obtenerMantenimientos());
    }

    @GetMapping("/{idMantenimiento}")
    public ResponseEntity<MantenimientoResponseDTO> obtenerMantenimientoPorId(
            @PathVariable Integer idMantenimiento
    ) {
        return ResponseEntity.ok(
                mantenimientoService.obtenerMantenimientoPorId(idMantenimiento)
        );
    }

    @GetMapping("/habitacion/{idHabitacion}")
    public ResponseEntity<List<MantenimientoResponseDTO>> obtenerMantenimientosPorHabitacion(
            @PathVariable Integer idHabitacion
    ) {
        return ResponseEntity.ok(
                mantenimientoService.obtenerMantenimientosPorHabitacion(idHabitacion)
        );
    }

    @GetMapping("/estado/{estadoMantenimiento}")
    public ResponseEntity<List<MantenimientoResponseDTO>> obtenerMantenimientosPorEstado(
            @PathVariable String estadoMantenimiento
    ) {
        return ResponseEntity.ok(
                mantenimientoService.obtenerMantenimientosPorEstado(
                        estadoMantenimiento
                )
        );
    }

    @PostMapping
    public ResponseEntity<MantenimientoResponseDTO> crearMantenimiento(
            @Valid @RequestBody MantenimientoRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mantenimientoService.crearMantenimiento(request));
    }

    @PutMapping("/{idMantenimiento}")
    public ResponseEntity<MantenimientoResponseDTO> actualizarMantenimiento(
            @PathVariable Integer idMantenimiento,
            @Valid @RequestBody MantenimientoRequestDTO request
    ) {
        return ResponseEntity.ok(
                mantenimientoService.actualizarMantenimiento(idMantenimiento, request)
        );
    }

    @DeleteMapping("/{idMantenimiento}")
    public ResponseEntity<Void> eliminarMantenimiento(
            @PathVariable Integer idMantenimiento
    ) {
        mantenimientoService.eliminarMantenimiento(idMantenimiento);

        return ResponseEntity.noContent().build();
    }
}
