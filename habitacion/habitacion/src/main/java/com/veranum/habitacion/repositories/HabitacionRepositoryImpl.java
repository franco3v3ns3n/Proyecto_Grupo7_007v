package com.veranum.habitacion.repositories;

import org.springframework.stereotype.Repository;

import com.veranum.habitacion.models.HabitacionModel;

import java.util.ArrayList;
import java.util.List;

interface HabitacionRepository {
    void guardar(HabitacionModel habitacion);

    List<HabitacionModel> obtenerTodas();

    HabitacionModel buscarPorId(Long id_habitacion); // Asegúrate que sea id_habitacion

    void eliminar(Long id_habitacion);
}

@Repository
public class HabitacionRepositoryImpl implements HabitacionRepository {

    private List<HabitacionModel> baseDeDatosSimulada = new ArrayList<>();

    @Override
    public void guardar(HabitacionModel habitacion) {
        baseDeDatosSimulada.add(habitacion);
    }

    @Override
    public List<HabitacionModel> obtenerTodas() {
        return baseDeDatosSimulada;
    }

    @Override
    public HabitacionModel buscarPorId(Long id_habitacion) {
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