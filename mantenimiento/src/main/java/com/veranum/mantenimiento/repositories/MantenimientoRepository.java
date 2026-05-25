package com.veranum.mantenimiento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.veranum.mantenimiento.models.MantenimientoModel;
import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<MantenimientoModel, Integer> {

    List<MantenimientoModel> findById_hotel(Integer idHotel);
}