package com.veranum.pago.repositories;

import com.veranum.pago.models.PagoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<PagoModel, Integer> {

    List<PagoModel> findByIdEstadia(Integer idEstadia);

    List<PagoModel> findByEstadoPago(
            String estadoPago
    );
}
