package ms_cotizacion.hotel.controller;

import jakarta.validation.Valid;
import ms_cotizacion.hotel.dto.CotizacionRequestDTO;
import ms_cotizacion.hotel.dto.CotizacionResponseDTO;
import ms_cotizacion.hotel.service.CotizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cotizaciones")
public class CotizacionController {

    @Autowired
    private CotizacionService cotizacionService;

    @PostMapping
    public ResponseEntity<CotizacionResponseDTO> crearCotizacion(@Valid @RequestBody CotizacionRequestDTO request) {
        CotizacionResponseDTO response = cotizacionService.procesarCotizacion(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
