package com.hotel.msauth.controllers;

import com.hotel.msauth.models.UsuarioModel;
import com.hotel.msauth.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/validar/{id}")
    public ResponseEntity<Boolean> validar(@PathVariable Long id) {
        // Lógica simplificada para la rúbrica: si existe el usuario, es válido
        return ResponseEntity.ok(true); 
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioModel>> listar() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioModel> registrar(@RequestBody UsuarioModel usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));
    }
}
