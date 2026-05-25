package com.veranum.pago.clients;

import com.veranum.pago.dtos.response.EstadiaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "estadia",
        url = "${ESTADIA_SERVICE_URL}"
)
public interface EstadiaClient {

    @GetMapping("/api/v1/estadias/{idEstadia}")
    EstadiaResponseDTO obtenerEstadiaPorId(@PathVariable Integer idEstadia);
}
