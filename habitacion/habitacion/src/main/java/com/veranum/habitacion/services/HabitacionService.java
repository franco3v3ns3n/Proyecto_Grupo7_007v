package com.veranum.habitacion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veranum.habitacion.models.HabitacionModel;
import com.veranum.habitacion.repositories.HabitacionRepositoryImpl;

import java.util.List;

@Service
public class HabitacionService {

    @Autowired
    private HabitacionRepositoryImpl habitacionRepository;

    public void registrarHabitacion(HabitacionModel habitacion) {
        habitacionRepository.guardar(habitacion);
    }

    public List<HabitacionModel> listarTodas() {
        return habitacionRepository.obtenerTodas();
    }

    public HabitacionModel buscarPorId(Long id_habitacion) {
        return habitacionRepository.buscarPorId(id_habitacion);
    }

    public void eliminarHabitacion(Long id_habitacion) {
        habitacionRepository.eliminar(id_habitacion);
    }

    public void borrar(Long id_habitacion) {
        habitacionRepository.eliminar(id_habitacion);
    }
}