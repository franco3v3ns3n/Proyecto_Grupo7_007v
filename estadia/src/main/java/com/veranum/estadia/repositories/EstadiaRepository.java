package com.veranum.estadia.repositories;

import com.veranum.estadia.models.EstadiaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadiaRepository extends JpaRepository<EstadiaModel, Integer> {

    List<EstadiaModel> findByIdCliente(Integer idCliente);

    List<EstadiaModel> findByIdHabitacion(Integer idHabitacion);

    List<EstadiaModel> findByIdReserva(Integer idReserva);
}
