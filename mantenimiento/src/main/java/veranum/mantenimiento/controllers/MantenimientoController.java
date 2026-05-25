package veranum.mantenimiento.controllers;

import veranum.mantenimiento.dtos.request.MantenimientoRequestDTO;
import veranum.mantenimiento.dtos.response.MantenimientoResponseDTO;
import veranum.mantenimiento.services.MantenimientoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mantenimientos")
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;

    public MantenimientoController(MantenimientoService mantenimientoService) {
        this.mantenimientoService = mantenimientoService;
    }

    @GetMapping
    public ResponseEntity<List<MantenimientoResponseDTO>> obtenerMantenimientos() {
        return ResponseEntity.ok(mantenimientoService.obtenerMantenimientos());
    }

    @GetMapping("/{idMantenimiento}")
    public ResponseEntity<MantenimientoResponseDTO> obtenerMantenimientoPorId(
            @PathVariable Integer idMantenimiento) {
        return ResponseEntity.ok(mantenimientoService.obtenerMantenimientoPorId(idMantenimiento));
    }

    @GetMapping("/hotel/{idHotel}")
    public ResponseEntity<List<MantenimientoResponseDTO>> obtenerMantenimientosPorHotel(
            @PathVariable Integer idHotel) {
        return ResponseEntity.ok(mantenimientoService.obtenerMantenimientosPorHotel(idHotel));
    }

    @PostMapping
    public ResponseEntity<MantenimientoResponseDTO> crearMantenimiento(
            @Valid @RequestBody MantenimientoRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mantenimientoService.crearMantenimiento(request));
    }

    @PutMapping("/{idMantenimiento}")
    public ResponseEntity<MantenimientoResponseDTO> actualizarMantenimiento(
            @PathVariable Integer idMantenimiento,
            @Valid @RequestBody MantenimientoRequestDTO request) {
        return ResponseEntity.ok(mantenimientoService.actualizarMantenimiento(idMantenimiento, request));
    }

    @DeleteMapping("/{idMantenimiento}")
    public ResponseEntity<Void> eliminarMantenimiento(
            @PathVariable Integer idMantenimiento) {
        mantenimientoService.eliminarMantenimiento(idMantenimiento);
        return ResponseEntity.noContent().build();
    }
}