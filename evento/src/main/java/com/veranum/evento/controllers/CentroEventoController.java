package com.veranum.evento.controllers;

import com.veranum.evento.dtos.request.CentroEventoRequestDTO;
import com.veranum.evento.dtos.response.CentroEventoResponseDTO;
import com.veranum.evento.services.CentroEventoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/eventos")
public class CentroEventoController {

    private final CentroEventoService centroEventoService;

    public CentroEventoController(CentroEventoService centroEventoService) {
        this.centroEventoService = centroEventoService;
    }

    @GetMapping
    public ResponseEntity<List<CentroEventoResponseDTO>> obtenerCentrosEventos() {
        return ResponseEntity.ok(centroEventoService.obtenerCentrosEventos());
    }

    @GetMapping("/{idCentroEvento}")
    public ResponseEntity<CentroEventoResponseDTO> obtenerCentroEventoPorId(
            @PathVariable Integer idCentroEvento
    ) {
        return ResponseEntity.ok(
                centroEventoService.obtenerCentroEventoPorId(idCentroEvento)
        );
    }

    @GetMapping("/hotel/{idHotel}")
    public ResponseEntity<List<CentroEventoResponseDTO>> obtenerCentrosEventosPorHotel(
            @PathVariable Integer idHotel
    ) {
        return ResponseEntity.ok(
                centroEventoService.obtenerCentrosEventosPorHotel(idHotel)
        );
    }

    @PostMapping
    public ResponseEntity<CentroEventoResponseDTO> crearCentroEvento(
            @Valid @RequestBody CentroEventoRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(centroEventoService.crearCentroEvento(request));
    }

    @PutMapping("/{idCentroEvento}")
    public ResponseEntity<CentroEventoResponseDTO> actualizarCentroEvento(
            @PathVariable Integer idCentroEvento,
            @Valid @RequestBody CentroEventoRequestDTO request
    ) {
        return ResponseEntity.ok(
                centroEventoService.actualizarCentroEvento(idCentroEvento, request)
        );
    }

    @DeleteMapping("/{idCentroEvento}")
    public ResponseEntity<Void> eliminarCentroEvento(
            @PathVariable Integer idCentroEvento
    ) {
        centroEventoService.eliminarCentroEvento(idCentroEvento);

        return ResponseEntity.noContent().build();
    }
}