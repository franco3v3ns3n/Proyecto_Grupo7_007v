package com.hotel.msauth.repositories;

import com.hotel.msauth.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    
    // Este método nos servirá para el Login
    Optional<UsuarioModel> findByUsername(String username);
}
