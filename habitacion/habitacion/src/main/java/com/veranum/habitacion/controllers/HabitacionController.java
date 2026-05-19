package com.veranum.habitacion.controllers;

import com.veranum.habitacion.dtos.request.HabitacionRequestDTO;
import com.veranum.habitacion.dtos.response.HabitacionResponseDTO;
import com.veranum.habitacion.services.HabitacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/habitaciones")
public class HabitacionController {

    private final HabitacionService habitacionService;

    public HabitacionController(HabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    @GetMapping
    public ResponseEntity<List<HabitacionResponseDTO>> obtenerHabitaciones() {
        return ResponseEntity.ok(habitacionService.obtenerHabitaciones());
    }

    @GetMapping("/{idHabitacion}")
    public ResponseEntity<HabitacionResponseDTO> obtenerHabitacionPorId(
            @PathVariable Integer idHabitacion) {
        return ResponseEntity.ok(habitacionService.obtenerHabitacionPorId(idHabitacion));
    }

    // 3. CREAR HABITACIÓN
    @PostMapping
    public ResponseEntity<HabitacionResponseDTO> crearHabitacion(
            @Valid @RequestBody HabitacionRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED) // Código HTTP 201 Created
                .body(habitacionService.crearHabitacion(request));
    }

    // 4. ACTUALIZAR HABITACIÓN
    @PutMapping("/{idHabitacion}")
    public ResponseEntity<HabitacionResponseDTO> actualizarHabitacion(
            @PathVariable Integer idHabitacion,
            @Valid @RequestBody HabitacionRequestDTO request) {
        return ResponseEntity.ok(habitacionService.actualizarHabitacion(idHabitacion, request));
    }

    // 5. ELIMINAR HABITACIÓN
    @DeleteMapping("/{idHabitacion}")
    public ResponseEntity<Void> eliminarHabitacion(
            @PathVariable Integer idHabitacion) {
        habitacionService.eliminarHabitacion(idHabitacion);
        return ResponseEntity.noContent().build(); // Código HTTP 204 No Content
    }
}