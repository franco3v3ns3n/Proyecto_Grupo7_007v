package com.veranum.estadia.clients;

import com.veranum.estadia.dtos.response.ReservaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "reserva",
        url = "${RESERVA_SERVICE_URL}"
)
public interface ReservaClient {

    @GetMapping("/api/v1/reservas/{idReserva}")
    ReservaResponseDTO obtenerReservaPorId(@PathVariable Integer idReserva);
}
