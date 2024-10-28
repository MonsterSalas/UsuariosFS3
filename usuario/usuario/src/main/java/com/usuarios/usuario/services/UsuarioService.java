package com.usuarios.usuario.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usuarios.usuario.dto.UsuarioDTO;
import com.usuarios.usuario.models.Rol;
import com.usuarios.usuario.models.Usuario;
import com.usuarios.usuario.repository.RolRepository;
import com.usuarios.usuario.repository.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    public Usuario crearUsuario(UsuarioDTO usuarioDTO) {
        // Validar que el username no exista
        if (usuarioRepository.findByUsername(usuarioDTO.getUsername()) != null) {
            throw new RuntimeException("El username ya está en uso");
        }
        
        // Validar y obtener el rol
        Rol rol = rolRepository.findById(usuarioDTO.getRolId())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            
        // Crear nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(usuarioDTO.getUsername());
        nuevoUsuario.setPassword(usuarioDTO.getPassword()); // Aquí deberías agregar encriptación
        nuevoUsuario.setRol(rol);
        
        return usuarioRepository.save(nuevoUsuario);
    }
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
    
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
    
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
}