package com.usuarios.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usuarios.usuario.models.Rol;


public interface RolRepository extends JpaRepository<Rol, Long> {
    Rol findByNombre(String nombre);
}
