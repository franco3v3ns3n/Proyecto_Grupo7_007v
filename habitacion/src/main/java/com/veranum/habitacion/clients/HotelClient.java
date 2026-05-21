package com.veranum.habitacion.clients;

import com.veranum.habitacion.dtos.response.HotelResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "hotel",
        url = "${HOTEL_SERVICE_URL}"
)
public interface HotelClient {

    @GetMapping("/api/v1/hoteles/{idHotel}")
    HotelResponseDTO obtenerHotelPorId(@PathVariable Integer idHotel);
}
