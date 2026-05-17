package com.veranum.hotel.controllers;

import com.veranum.hotel.dtos.request.HotelRequestDTO;
import com.veranum.hotel.dtos.response.HotelResponseDTO;
import com.veranum.hotel.services.HotelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hoteles")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<List<HotelResponseDTO>> obtenerHoteles() {

        return ResponseEntity.ok(hotelService.obtenerHoteles());
    }

    @GetMapping("/{idHotel}")
    public ResponseEntity<HotelResponseDTO> obtenerHotelPorId(
            @PathVariable Integer idHotel
    ) {

        return ResponseEntity.ok(hotelService.obtenerHotelPorId(idHotel));
    }

    @PostMapping
    public ResponseEntity<HotelResponseDTO> crearHotel(
            @Valid @RequestBody HotelRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(hotelService.crearHotel(request));
    }

    @PutMapping("/{idHotel}")
    public ResponseEntity<HotelResponseDTO> actualizarHotel(
            @PathVariable Integer idHotel,
            @Valid @RequestBody HotelRequestDTO request
    ) {

        return ResponseEntity.ok(
                hotelService.actualizarHotel(idHotel, request)
        );
    }

    @DeleteMapping("/{idHotel}")
    public ResponseEntity<Void> eliminarHotel(
            @PathVariable Integer idHotel
    ) {

        hotelService.eliminarHotel(idHotel);

        return ResponseEntity.noContent().build();
    }
}