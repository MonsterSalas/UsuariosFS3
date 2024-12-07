package com.usuarios.usuario.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usuarios.usuario.dto.ResponseDTO;
import com.usuarios.usuario.dto.UsuarioDTO;
import com.usuarios.usuario.models.Usuario;
import com.usuarios.usuario.services.UsuarioService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/crear")
    public ResponseEntity<ResponseDTO> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            log.debug("Recibiendo solicitud de creación de usuario: {}", usuarioDTO);
            
            // Validaciones básicas
            if (usuarioDTO.getUsername() == null || usuarioDTO.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ResponseDTO("El username es requerido", null, false));
            }
            
            if (usuarioDTO.getPassword() == null || usuarioDTO.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ResponseDTO("La contraseña es requerida", null, false));
            }
            
            if (usuarioDTO.getNombreCompleto() == null || usuarioDTO.getNombreCompleto().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ResponseDTO("El nombre completo es requerido", null, false));
            }
            
            if (usuarioDTO.getRolId() == null) {
                return ResponseEntity.badRequest()
                    .body(new ResponseDTO("El rol es requerido", null, false));
            }
            
            Usuario usuarioCreado = usuarioService.crearUsuario(usuarioDTO);
            
            return ResponseEntity.ok(new ResponseDTO(
                "Usuario creado exitosamente",
                usuarioCreado,
                true
            ));
            
        } catch (RuntimeException e) {
            log.error("Error al crear usuario", e);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ResponseDTO(e.getMessage(), null, false));
        } catch (Exception e) {
            log.error("Error inesperado al crear usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO(
                    "Error al crear el usuario: " + e.getMessage(),
                    null,
                    false
                ));
        }
    }
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario existingUsuario = usuarioService.findById(id);
        if (existingUsuario == null) {
            return ResponseEntity.notFound().build();
        }
        usuario.setId(id);
        return ResponseEntity.ok(usuarioService.save(usuario));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        Usuario existingUsuario = usuarioService.findById(id);
        if (existingUsuario == null) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}