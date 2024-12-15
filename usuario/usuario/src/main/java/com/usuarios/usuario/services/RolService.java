package com.usuarios.usuario.services;

import com.usuarios.usuario.models.Rol;
import com.usuarios.usuario.repository.RolRepository;
import com.usuarios.usuario.dto.RolDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public Rol crearRol(RolDTO rolDTO) {
        // Validar que el nombre del rol no exista
        if (rolRepository.findByNombre(rolDTO.getNombre()) != null) {
            throw new RuntimeException("Ya existe un rol con ese nombre");
        }

        // Validar permisos
        if (rolDTO.getPermisos() == null || rolDTO.getPermisos().isEmpty()) {
            throw new RuntimeException("El rol debe tener al menos un permiso");
        }

        // Crear nuevo rol
        Rol nuevoRol = new Rol();
        nuevoRol.setNombre(rolDTO.getNombre().toUpperCase());
        nuevoRol.setPermisos(rolDTO.getPermisos());

        return rolRepository.save(nuevoRol);
    }

    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }

    public Rol obtenerRolPorId(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }

    public void eliminarRol(Long id) {
        Rol rol = obtenerRolPorId(id);
        rolRepository.delete(rol);
    }

    public Rol actualizarRol(Long id, RolDTO rolDTO) {
        Rol rolExistente = obtenerRolPorId(id);

        // Verificar si el nuevo nombre ya existe en otro rol
        Rol rolConNombre = rolRepository.findByNombre(rolDTO.getNombre());
        if (rolConNombre != null && !rolConNombre.getId().equals(id)) {
            throw new RuntimeException("Ya existe otro rol con ese nombre");
        }

        rolExistente.setNombre(rolDTO.getNombre().toUpperCase());
        rolExistente.setPermisos(rolDTO.getPermisos());

        return rolRepository.save(rolExistente);
    }
}
