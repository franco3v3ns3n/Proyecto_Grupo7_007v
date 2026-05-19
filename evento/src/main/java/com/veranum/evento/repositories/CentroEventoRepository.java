package com.veranum.evento.repositories;

import com.veranum.evento.models.CentroEventoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CentroEventoRepository extends JpaRepository<CentroEventoModel, Integer> {

    List<CentroEventoModel> findByIdHotel(Integer idHotel);
}