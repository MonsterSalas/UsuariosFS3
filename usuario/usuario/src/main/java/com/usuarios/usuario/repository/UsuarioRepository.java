package com.usuarios.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usuarios.usuario.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
    // Query simple para verificar si existe el usuario con username y password
    boolean existsByUsernameAndPassword(String username, String password);

    boolean existsByNombreCompleto(String nombreCompleto);

}