package com.veranum.habitacion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.veranum.habitacion.models.HabitacionModel;
import com.veranum.habitacion.services.HabitacionService;

import java.util.List;

@RestController
@RequestMapping("/habitaciones") // Esta será la URL base
public class HabitacionController {

    @Autowired
    private HabitacionService habitacionService;

    // 1. Obtener todas las habitaciones (GET)
    @GetMapping("/listar")
    public List<HabitacionModel> getAll() {
        return habitacionService.listarTodas();
    }

    // 2. Crear una nueva habitación (POST)
    @PostMapping("/guardar")
    public String save(@RequestBody HabitacionModel habitacion) {
        habitacionService.registrarHabitacion(habitacion);
        return "Habitación " + habitacion.getNumero_habitacion() + " guardada con éxito";
    }

    // 3. Buscar por ID (GET)
    @GetMapping("/buscar/{id}")
    public HabitacionModel getById(@PathVariable Long id) {
        // Cambiado a minúscula para que coincida con el Service
        return habitacionService.buscarPorId(id);
    }

    // 4. Eliminar (DELETE)
    @DeleteMapping("/eliminar/{id}")
    public String delete(@PathVariable Long id) {
        // Asegúrate de que el nombre sea eliminarHabitacion
        habitacionService.eliminarHabitacion(id);
        return "Habitación eliminada correctamente";
    }
}