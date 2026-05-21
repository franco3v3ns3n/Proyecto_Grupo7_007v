package com.veranum.servicio.controllers;

import com.veranum.servicio.dtos.request.ServicioRequestDTO;
import com.veranum.servicio.dtos.response.ServicioResponseDTO;
import com.veranum.servicio.services.ServicioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    public ResponseEntity<List<ServicioResponseDTO>> obtenerServicios() {
        return ResponseEntity.ok(servicioService.obtenerServicios());
    }

    @GetMapping("/{idServicio}")
    public ResponseEntity<ServicioResponseDTO> obtenerServicioPorId(
            @PathVariable Integer idServicio
    ) {
        return ResponseEntity.ok(servicioService.obtenerServicioPorId(idServicio));
    }

    @GetMapping("/hotel/{idHotel}")
    public ResponseEntity<List<ServicioResponseDTO>> obtenerServiciosPorHotel(
            @PathVariable Integer idHotel
    ) {
        return ResponseEntity.ok(servicioService.obtenerServiciosPorHotel(idHotel));
    }

    @PostMapping
    public ResponseEntity<ServicioResponseDTO> crearServicio(
            @Valid @RequestBody ServicioRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(servicioService.crearServicio(request));
    }

    @PutMapping("/{idServicio}")
    public ResponseEntity<ServicioResponseDTO> actualizarServicio(
            @PathVariable Integer idServicio,
            @Valid @RequestBody ServicioRequestDTO request
    ) {
        return ResponseEntity.ok(servicioService.actualizarServicio(idServicio, request));
    }

    @DeleteMapping("/{idServicio}")
    public ResponseEntity<Void> eliminarServicio(
            @PathVariable Integer idServicio
    ) {
        servicioService.eliminarServicio(idServicio);
        return ResponseEntity.noContent().build();
    }
}