package com.hotel.habitacion.Service;

import com.hotel.habitacion.Model.ModelHabitacion;
import com.hotel.habitacion.Repository.HabitacionRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HabitacionService {

    @Autowired
    private HabitacionRepositoryImpl habitacionRepository;

    // Método para registrar una habitación
    public void registrarHabitacion(ModelHabitacion habitacion) {
        habitacionRepository.guardar(habitacion);
    }

    // Método para obtener la lista completa
    public List<ModelHabitacion> listarTodas() {
        return habitacionRepository.obtenerTodas();
    }

    // Fíjate que empiece con minúscula: buscarPorId
    public ModelHabitacion buscarPorId(Long id_habitacion) {
        return habitacionRepository.buscarPorId(id_habitacion);
    }

    // Fíjate que sea: eliminarHabitacion
    public void eliminarHabitacion(Long id_habitacion) {
        habitacionRepository.eliminar(id_habitacion);
    }

    // Método para eliminar
    public void borrar(Long id_habitacion) {
        habitacionRepository.eliminar(id_habitacion);
    }
}