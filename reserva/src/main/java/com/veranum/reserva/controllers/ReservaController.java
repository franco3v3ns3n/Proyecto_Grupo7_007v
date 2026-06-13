package com.veranum.reserva.controllers;

import com.veranum.reserva.dtos.request.ReservaRequestDTO;
import com.veranum.reserva.dtos.response.ReservaResponseDTO;
import com.veranum.reserva.services.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservas() {
        return ResponseEntity.ok(reservaService.obtenerReservas());
    }

    @GetMapping("/{idReserva}")
    public ResponseEntity<ReservaResponseDTO> obtenerReservaPorId(
            @PathVariable Integer idReserva
    ) {
        return ResponseEntity.ok(reservaService.obtenerReservaPorId(idReserva));
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservasPorCliente(
            @PathVariable Integer idCliente
    ) {
        return ResponseEntity.ok(reservaService.obtenerReservasPorCliente(idCliente));
    }

    @GetMapping("/habitacion/{idHabitacion}")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservasPorHabitacion(
            @PathVariable Integer idHabitacion
    ) {
        return ResponseEntity.ok(reservaService.obtenerReservasPorHabitacion(idHabitacion));
    }

    @GetMapping("/estado/{estadoReserva}")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservasPorEstado(
            @PathVariable String estadoReserva
    ) {
        return ResponseEntity.ok(
                reservaService.obtenerReservasPorEstado(
                        estadoReserva
                )
        );
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crearReserva(
            @Valid @RequestBody ReservaRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservaService.crearReserva(request));
    }

    @PutMapping("/{idReserva}")
    public ResponseEntity<ReservaResponseDTO> actualizarReserva(
            @PathVariable Integer idReserva,
            @Valid @RequestBody ReservaRequestDTO request
    ) {
        return ResponseEntity.ok(reservaService.actualizarReserva(idReserva, request));
    }

    @DeleteMapping("/{idReserva}")
    public ResponseEntity<Void> eliminarReserva(
            @PathVariable Integer idReserva
    ) {
        reservaService.eliminarReserva(idReserva);
        return ResponseEntity.noContent().build();
    }
}
