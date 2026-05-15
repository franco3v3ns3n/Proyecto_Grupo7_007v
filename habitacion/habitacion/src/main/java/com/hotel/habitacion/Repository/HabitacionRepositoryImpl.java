package com.hotel.habitacion.Repository;

import com.hotel.habitacion.Model.ModelHabitacion;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

interface HabitacionRepository {
    void guardar(ModelHabitacion habitacion);

    List<ModelHabitacion> obtenerTodas();

    ModelHabitacion buscarPorId(Long id_habitacion); // Asegúrate que sea id_habitacion

    void eliminar(Long id_habitacion);
}

@Repository
public class HabitacionRepositoryImpl implements HabitacionRepository {

    private List<ModelHabitacion> baseDeDatosSimulada = new ArrayList<>();

    @Override
    public void guardar(ModelHabitacion habitacion) {
        baseDeDatosSimulada.add(habitacion);
    }

    @Override
    public List<ModelHabitacion> obtenerTodas() {
        return baseDeDatosSimulada;
    }

    @Override
    public ModelHabitacion buscarPorId(Long id_habitacion) {
        return baseDeDatosSimulada.stream()
                .filter(h -> h.getId_habitacion().equals(id_habitacion))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void eliminar(Long id_habitacion) {
        baseDeDatosSimulada.removeIf(h -> h.getId_habitacion().equals(id_habitacion));
    }
}