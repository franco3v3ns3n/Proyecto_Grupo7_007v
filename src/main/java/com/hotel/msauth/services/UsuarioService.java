package com.hotel.msauth.services;

import com.hotel.msauth.models.UsuarioModel;
import com.hotel.msauth.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    
    public List<UsuarioModel> obtenerTodos() {
        log.info("Obteniendo todos los usuarios");
        return usuarioRepository.findAll();
    }

    
    public UsuarioModel guardar(UsuarioModel usuario) {
        log.info("Guardando usuario: {}", usuario.getUsername());
        return usuarioRepository.save(usuario);
    }

    
    public UsuarioModel buscarPorUsername(String username) {
        log.info("Buscando usuario por username: {}", username);
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado: {}", username);
                    return new RuntimeException("Usuario no encontrado: " + username);
                });
    }
}
