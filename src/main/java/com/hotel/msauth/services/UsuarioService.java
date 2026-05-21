package com.hotel.msauth.services;

import com.hotel.msauth.models.UsuarioModel;
import com.hotel.msauth.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    
    public List<UsuarioModel> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    
    public UsuarioModel guardar(UsuarioModel usuario) {
        
        return usuarioRepository.save(usuario);
    }

    
    public UsuarioModel buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
    }
}
