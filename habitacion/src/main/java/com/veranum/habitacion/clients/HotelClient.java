package com.veranum.habitacion.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hotel-service", url = "http://localhost:8081/hoteles")
public interface HotelClient {

    @GetMapping("/buscar/{id}")
    Object buscarHotelPorId(@PathVariable("id") Integer id);
}