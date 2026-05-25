package com.veranum.reserva.clients;

import com.veranum.reserva.dtos.response.ClienteResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "cliente",
        url = "${CLIENTE_SERVICE_URL}"
)
public interface ClienteClient {

    @GetMapping("/api/v1/clientes/{idCliente}")
    ClienteResponseDTO obtenerClientePorId(@PathVariable Integer idCliente);
}
