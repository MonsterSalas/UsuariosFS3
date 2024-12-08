package com.usuarios.usuario.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usuarios.usuario.models.Usuario;
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

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsernameAndPassword(username, password);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Login exitoso");
            response.put("rol", usuario.getRol().getNombre());
            response.put("username", usuario.getUsername());

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(Map.of("mensaje", "Credenciales inválidas"));
    }
}