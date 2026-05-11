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

    // Listar todos los usuarios (Útil para el Admin del hotel)
    public List<UsuarioModel> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    // Guardar un usuario (Registro)
    public UsuarioModel guardar(UsuarioModel usuario) {
        // Aquí luego agregaremos el cifrado de contraseña por seguridad
        return usuarioRepository.save(usuario);
    }

    // Buscar por username (Para el Login)
    public UsuarioModel buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
    }
}
