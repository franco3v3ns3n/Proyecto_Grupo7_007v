package com.veranum.habitacion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.veranum.habitacion.models.HabitacionModel;
import com.veranum.habitacion.services.HabitacionService;

import java.util.List;

@RestController
@RequestMapping("/habitaciones")
public class HabitacionController {

    @Autowired
    private HabitacionService habitacionService;

    @GetMapping("/listar")
    public List<HabitacionModel> getAll() {
        return habitacionService.listarTodas();
    }

    @PostMapping("/guardar")
    public String save(@RequestBody HabitacionModel habitacion) {
        habitacionService.registrarHabitacion(habitacion);
        return "Habitación " + habitacion.getNumero_habitacion() + " guardada con éxito";
    }

    @GetMapping("/buscar/{id}")
    public HabitacionModel getById(@PathVariable Long id) {
        return habitacionService.buscarPorId(id);
    }

    @DeleteMapping("/eliminar/{id}")
    public String delete(@PathVariable Long id) {
        habitacionService.eliminarHabitacion(id);
        return "Habitación eliminada correctamente";
    }
}