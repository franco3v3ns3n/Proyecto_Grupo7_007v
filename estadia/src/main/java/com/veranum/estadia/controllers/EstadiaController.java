package com.veranum.estadia.controllers;

import com.veranum.estadia.dtos.request.EstadiaRequestDTO;
import com.veranum.estadia.dtos.response.EstadiaResponseDTO;
import com.veranum.estadia.services.EstadiaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estadias")
public class EstadiaController {

    private final EstadiaService estadiaService;

    public EstadiaController(EstadiaService estadiaService) {
        this.estadiaService = estadiaService;
    }

    @GetMapping
    public ResponseEntity<List<EstadiaResponseDTO>> obtenerEstadias() {
        return ResponseEntity.ok(estadiaService.obtenerEstadias());
    }

    @GetMapping("/{idEstadia}")
    public ResponseEntity<EstadiaResponseDTO> obtenerEstadiaPorId(
            @PathVariable Integer idEstadia
    ) {
        return ResponseEntity.ok(estadiaService.obtenerEstadiaPorId(idEstadia));
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<EstadiaResponseDTO>> obtenerEstadiasPorCliente(
            @PathVariable Integer idCliente
    ) {
        return ResponseEntity.ok(estadiaService.obtenerEstadiasPorCliente(idCliente));
    }

    @GetMapping("/habitacion/{idHabitacion}")
    public ResponseEntity<List<EstadiaResponseDTO>> obtenerEstadiasPorHabitacion(
            @PathVariable Integer idHabitacion
    ) {
        return ResponseEntity.ok(estadiaService.obtenerEstadiasPorHabitacion(idHabitacion));
    }

    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<EstadiaResponseDTO>> obtenerEstadiasPorReserva(
            @PathVariable Integer idReserva
    ) {
        return ResponseEntity.ok(estadiaService.obtenerEstadiasPorReserva(idReserva));
    }

    @PostMapping
    public ResponseEntity<EstadiaResponseDTO> crearEstadia(
            @Valid @RequestBody EstadiaRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(estadiaService.crearEstadia(request));
    }

    @PutMapping("/{idEstadia}")
    public ResponseEntity<EstadiaResponseDTO> actualizarEstadia(
            @PathVariable Integer idEstadia,
            @Valid @RequestBody EstadiaRequestDTO request
    ) {
        return ResponseEntity.ok(estadiaService.actualizarEstadia(idEstadia, request));
    }

    @DeleteMapping("/{idEstadia}")
    public ResponseEntity<Void> eliminarEstadia(
            @PathVariable Integer idEstadia
    ) {
        estadiaService.eliminarEstadia(idEstadia);
        return ResponseEntity.noContent().build();
    }
}
