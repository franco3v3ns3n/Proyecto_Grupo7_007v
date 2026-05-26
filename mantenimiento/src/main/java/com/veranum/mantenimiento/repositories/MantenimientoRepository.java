package com.veranum.mantenimiento.repositories;

import com.veranum.mantenimiento.models.MantenimientoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<MantenimientoModel, Integer> {

    List<MantenimientoModel> findByIdHabitacion(Integer idHabitacion);

    List<MantenimientoModel> findByEstadoMantenimiento(
            String estadoMantenimiento
    );
}