package com.veranum.habitacion.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.veranum.habitacion.models.HabitacionModel;

import java.util.List;

@Repository
public interface HabitacionRepository extends JpaRepository<HabitacionModel, Integer> {

    List<HabitacionModel> findByIdHotel(Integer idHotel);
}