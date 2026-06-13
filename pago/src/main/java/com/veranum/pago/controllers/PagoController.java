package com.veranum.pago.controllers;

import com.veranum.pago.dtos.request.PagoRequestDTO;
import com.veranum.pago.dtos.response.PagoResponseDTO;
import com.veranum.pago.services.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> obtenerPagos() {
        return ResponseEntity.ok(pagoService.obtenerPagos());
    }

    @GetMapping("/{idPago}")
    public ResponseEntity<PagoResponseDTO> obtenerPagoPorId(
            @PathVariable Integer idPago
    ) {
        return ResponseEntity.ok(pagoService.obtenerPagoPorId(idPago));
    }

    @GetMapping("/estadia/{idEstadia}")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPagosPorEstadia(
            @PathVariable Integer idEstadia
    ) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorEstadia(idEstadia));
    }

    @GetMapping("/estado/{estadoPago}")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPagosPorEstado(
            @PathVariable String estadoPago
    ) {
        return ResponseEntity.ok(
                pagoService.obtenerPagosPorEstado(
                        estadoPago
                )
        );
    }

    @PostMapping
    public ResponseEntity<PagoResponseDTO> crearPago(
            @Valid @RequestBody PagoRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pagoService.crearPago(request));
    }

    @PutMapping("/{idPago}")
    public ResponseEntity<PagoResponseDTO> actualizarPago(
            @PathVariable Integer idPago,
            @Valid @RequestBody PagoRequestDTO request
    ) {
        return ResponseEntity.ok(
                pagoService.actualizarPago(idPago, request)
        );
    }

    @DeleteMapping("/{idPago}")
    public ResponseEntity<Void> eliminarPago(
            @PathVariable Integer idPago
    ) {
        pagoService.eliminarPago(idPago);

        return ResponseEntity.noContent().build();
    }
}
