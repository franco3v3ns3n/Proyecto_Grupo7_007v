package com.veranum.reserva.controllers;

import com.veranum.reserva.dtos.request.ReservaRequestDTO;
import com.veranum.reserva.dtos.response.ReservaResponseDTO;
import com.veranum.reserva.services.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
@Tag(name = "Reservas", description = "Operaciones relacionadas con la gestión de reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Operation(summary = "Obtener todas las reservas", description = "Retorna todas las reservas registradas")
    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservas() {
        return ResponseEntity.ok(reservaService.obtenerReservas());
    }

    @Operation(summary = "Obtener reserva por ID", description = "Busca una reserva según su identificador")
    @GetMapping("/{idReserva}")
    public ResponseEntity<ReservaResponseDTO> obtenerReservaPorId(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable Integer idReserva
    ) {
        return ResponseEntity.ok(reservaService.obtenerReservaPorId(idReserva));
    }

    @Operation(summary = "Obtener reservas por cliente", description = "Retorna reservas asociadas a un cliente")
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservasPorCliente(
            @Parameter(description = "ID del cliente", required = true)
            @PathVariable Integer idCliente
    ) {
        return ResponseEntity.ok(reservaService.obtenerReservasPorCliente(idCliente));
    }

    @Operation(summary = "Obtener reservas por habitación", description = "Retorna reservas asociadas a una habitación")
    @GetMapping("/habitacion/{idHabitacion}")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservasPorHabitacion(
            @Parameter(description = "ID de la habitación", required = true)
            @PathVariable Integer idHabitacion
    ) {
        return ResponseEntity.ok(reservaService.obtenerReservasPorHabitacion(idHabitacion));
    }

    @Operation(summary = "Obtener reservas por estado", description = "Filtra reservas por estado")
    @GetMapping("/estado/{estadoReserva}")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservasPorEstado(
            @Parameter(description = "Estado de la reserva", required = true)
            @PathVariable String estadoReserva
    ) {
        return ResponseEntity.ok(
                reservaService.obtenerReservasPorEstado(
                        estadoReserva
                )
        );
    }

    @Operation(summary = "Crear reserva", description = "Crea una reserva validando cliente y habitación")
    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crearReserva(
            @Valid @RequestBody ReservaRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservaService.crearReserva(request));
    }

    @Operation(summary = "Actualizar reserva", description = "Actualiza una reserva existente")
    @PutMapping("/{idReserva}")
    public ResponseEntity<ReservaResponseDTO> actualizarReserva(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable Integer idReserva,
            @Valid @RequestBody ReservaRequestDTO request
    ) {
        return ResponseEntity.ok(reservaService.actualizarReserva(idReserva, request));
    }

    @Operation(summary = "Eliminar reserva", description = "Elimina una reserva existente")
    @DeleteMapping("/{idReserva}")
    public ResponseEntity<Void> eliminarReserva(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable Integer idReserva
    ) {
        reservaService.eliminarReserva(idReserva);
        return ResponseEntity.noContent().build();
    }
}
