package com.usuarios.usuario.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usuarios.usuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

// AuthController.java
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UsuarioRepository usuarioRepository;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        // Opción 1: Solo verificar si existe
        if (usuarioRepository.existsByUsernameAndPassword(username, password)) {
            return ResponseEntity.ok(Map.of("mensaje", "Login exitoso"));
        }
        
        return ResponseEntity.badRequest().body(Map.of("mensaje", "Credenciales inválidas"));
    }
}