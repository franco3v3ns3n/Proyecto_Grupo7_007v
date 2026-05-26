package com.veranum.reserva.repositories;

import com.veranum.reserva.models.ReservaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaModel, Integer> {

    List<ReservaModel> findByIdCliente(Integer idCliente);

    List<ReservaModel> findByIdHabitacion(Integer idHabitacion);

    List<ReservaModel> findByEstadoReserva(
            String estadoReserva
    );
}
