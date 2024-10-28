package com.usuarios.usuario.controllers;
import com.usuarios.usuario.dto.RolDTO;
import com.usuarios.usuario.dto.ResponseDTO;
import com.usuarios.usuario.services.RolService;
import com.usuarios.usuario.models.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RolController {
    
    @Autowired
    private RolService rolService;
    
    @PostMapping("/crear")
    public ResponseEntity<ResponseDTO> crearRol(@RequestBody RolDTO rolDTO) {
        try {
            // Validaciones básicas
            if (rolDTO.getNombre() == null || rolDTO.getNombre().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ResponseDTO("El nombre del rol es requerido", null, false));
            }
            
            Rol rolCreado = rolService.crearRol(rolDTO);
            return ResponseEntity.ok(new ResponseDTO(
                "Rol creado exitosamente",
                rolCreado,
                true
            ));
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ResponseDTO(e.getMessage(), null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO("Error al crear el rol: " + e.getMessage(), null, false));
        }
    }
    
    @GetMapping
    public ResponseEntity<ResponseDTO> obtenerTodosLosRoles() {
        try {
            List<Rol> roles = rolService.obtenerTodosLosRoles();
            return ResponseEntity.ok(new ResponseDTO(
                "Roles obtenidos exitosamente",
                roles,
                true
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO("Error al obtener los roles", null, false));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> obtenerRolPorId(@PathVariable Long id) {
        try {
            Rol rol = rolService.obtenerRolPorId(id);
            return ResponseEntity.ok(new ResponseDTO(
                "Rol encontrado",
                rol,
                true
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(e.getMessage(), null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO("Error al obtener el rol", null, false));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> actualizarRol(@PathVariable Long id, @RequestBody RolDTO rolDTO) {
        try {
            Rol rolActualizado = rolService.actualizarRol(id, rolDTO);
            return ResponseEntity.ok(new ResponseDTO(
                "Rol actualizado exitosamente",
                rolActualizado,
                true
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ResponseDTO(e.getMessage(), null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO("Error al actualizar el rol", null, false));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> eliminarRol(@PathVariable Long id) {
        try {
            rolService.eliminarRol(id);
            return ResponseEntity.ok(new ResponseDTO(
                "Rol eliminado exitosamente",
                null,
                true
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(e.getMessage(), null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO("Error al eliminar el rol", null, false));
        }
    }
}