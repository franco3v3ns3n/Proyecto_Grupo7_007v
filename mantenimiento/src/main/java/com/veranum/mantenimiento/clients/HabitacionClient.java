package com.veranum.mantenimiento.clients;

import com.veranum.mantenimiento.dtos.response.HabitacionResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "habitacion",
        url = "${HABITACION_SERVICE_URL}"
)
public interface HabitacionClient {

    @GetMapping("/api/v1/habitaciones/{idHabitacion}")
    HabitacionResponseDTO obtenerHabitacionPorId(@PathVariable Integer idHabitacion);
}
