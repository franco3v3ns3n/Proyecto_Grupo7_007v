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
            @PathVariable Integer idHabitacion
    ) {
        return ResponseEntity.ok(habitacionService.obtenerHabitacionPorId(idHabitacion));
    }

    @GetMapping("/hotel/{idHotel}")
    public ResponseEntity<List<HabitacionResponseDTO>> obtenerHabitacionesPorHotel(
            @PathVariable Integer idHotel
    ) {
        return ResponseEntity.ok(habitacionService.obtenerHabitacionesPorHotel(idHotel));
    }

    @GetMapping("/hotel/{idHotel}/tipo/{tipoHabitacion}")
    public ResponseEntity<List<HabitacionResponseDTO>> obtenerHabitacionesPorHotelYTipo(
            @PathVariable Integer idHotel,
            @PathVariable String tipoHabitacion
    ) {
        return ResponseEntity.ok(
                habitacionService.obtenerHabitacionesPorHotelYTipo(
                        idHotel,
                        tipoHabitacion
                )
        );
    }

    @GetMapping("/hotel/{idHotel}/estado/{estadoHabitacion}")
    public ResponseEntity<List<HabitacionResponseDTO>> obtenerHabitacionesPorHotelYEstado(
            @PathVariable Integer idHotel,
            @PathVariable String estadoHabitacion
    ) {
        return ResponseEntity.ok(
                habitacionService.obtenerHabitacionesPorHotelYEstado(
                        idHotel,
                        estadoHabitacion
                )
        );
    }

    @PostMapping
    public ResponseEntity<HabitacionResponseDTO> crearHabitacion(
            @Valid @RequestBody HabitacionRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(habitacionService.crearHabitacion(request));
    }

    @PutMapping("/{idHabitacion}")
    public ResponseEntity<HabitacionResponseDTO> actualizarHabitacion(
            @PathVariable Integer idHabitacion,
            @Valid @RequestBody HabitacionRequestDTO request
    ) {
        return ResponseEntity.ok(habitacionService.actualizarHabitacion(idHabitacion, request));
    }

    @DeleteMapping("/{idHabitacion}")
    public ResponseEntity<Void> eliminarHabitacion(
            @PathVariable Integer idHabitacion
    ) {
        habitacionService.eliminarHabitacion(idHabitacion);
        return ResponseEntity.noContent().build();
    }
}
