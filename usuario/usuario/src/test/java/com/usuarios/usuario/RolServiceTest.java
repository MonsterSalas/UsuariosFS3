package com.usuarios.usuario;

import com.usuarios.usuario.models.Rol;
import com.usuarios.usuario.repository.RolRepository;
import com.usuarios.usuario.dto.RolDTO;
import com.usuarios.usuario.services.RolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    private RolDTO rolDTO;
    private Rol rol;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        rolDTO = new RolDTO();
        rolDTO.setNombre("ADMIN");
        rolDTO.setPermisos(Arrays.asList("READ", "WRITE"));

        rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ADMIN");
        rol.setPermisos(Arrays.asList("READ", "WRITE"));
    }

    @Test
    void crearRol_CuandoDatosValidos_RetornaRolCreado() {
        when(rolRepository.findByNombre(anyString())).thenReturn(null);
        when(rolRepository.save(any(Rol.class))).thenReturn(rol);

        Rol resultado = rolService.crearRol(rolDTO);

        assertNotNull(resultado);
        assertEquals("ADMIN", resultado.getNombre());
        assertEquals(2, resultado.getPermisos().size());
        verify(rolRepository).save(any(Rol.class));
    }

    @Test
    void crearRol_CuandoNombreExiste_LanzaExcepcion() {
        when(rolRepository.findByNombre(anyString())).thenReturn(new Rol());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            rolService.crearRol(rolDTO)
        );

        assertEquals("Ya existe un rol con ese nombre", exception.getMessage());
        verify(rolRepository, never()).save(any(Rol.class));
    }

    @Test
    void crearRol_CuandoPermisosNull_LanzaExcepcion() {
        rolDTO.setPermisos(null);
        when(rolRepository.findByNombre(anyString())).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            rolService.crearRol(rolDTO)
        );

        assertEquals("El rol debe tener al menos un permiso", exception.getMessage());
        verify(rolRepository, never()).save(any(Rol.class));
    }

    @Test
    void crearRol_CuandoPermisosVacios_LanzaExcepcion() {
        rolDTO.setPermisos(Collections.emptyList());
        when(rolRepository.findByNombre(anyString())).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            rolService.crearRol(rolDTO)
        );

        assertEquals("El rol debe tener al menos un permiso", exception.getMessage());
        verify(rolRepository, never()).save(any(Rol.class));
    }

    @Test
    void obtenerTodosLosRoles_RetornaListaRoles() {
        List<Rol> roles = Arrays.asList(rol, new Rol());
        when(rolRepository.findAll()).thenReturn(roles);

        List<Rol> resultado = rolService.obtenerTodosLosRoles();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(rolRepository).findAll();
    }

    @Test
    void obtenerRolPorId_CuandoExiste_RetornaRol() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));

        Rol resultado = rolService.obtenerRolPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(rolRepository).findById(1L);
    }

    @Test
    void obtenerRolPorId_CuandoNoExiste_LanzaExcepcion() {
        when(rolRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            rolService.obtenerRolPorId(1L)
        );

        assertEquals("Rol no encontrado", exception.getMessage());
        verify(rolRepository).findById(1L);
    }

    @Test
    void eliminarRol_CuandoExiste_EliminaRol() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        doNothing().when(rolRepository).delete(any(Rol.class));

        rolService.eliminarRol(1L);

        verify(rolRepository).findById(1L);
        verify(rolRepository).delete(rol);
    }

    @Test
    void eliminarRol_CuandoNoExiste_LanzaExcepcion() {
        when(rolRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            rolService.eliminarRol(1L)
        );

        assertEquals("Rol no encontrado", exception.getMessage());
        verify(rolRepository).findById(1L);
        verify(rolRepository, never()).delete(any(Rol.class));
    }

    @Test
    void actualizarRol_CuandoDatosValidos_RetornaRolActualizado() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(rolRepository.findByNombre(anyString())).thenReturn(null);
        when(rolRepository.save(any(Rol.class))).thenReturn(rol);

        Rol resultado = rolService.actualizarRol(1L, rolDTO);

        assertNotNull(resultado);
        assertEquals("ADMIN", resultado.getNombre());
        verify(rolRepository).save(any(Rol.class));
    }

    @Test
    void actualizarRol_CuandoNombreExisteEnOtroRol_LanzaExcepcion() {
        Rol otroRol = new Rol();
        otroRol.setId(2L);
        otroRol.setNombre("ADMIN");

        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(rolRepository.findByNombre(anyString())).thenReturn(otroRol);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            rolService.actualizarRol(1L, rolDTO)
        );

        assertEquals("Ya existe otro rol con ese nombre", exception.getMessage());
        verify(rolRepository, never()).save(any(Rol.class));
    }

    @Test
    void actualizarRol_CuandoRolNoExiste_LanzaExcepcion() {
        when(rolRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            rolService.actualizarRol(1L, rolDTO)
        );

        assertEquals("Rol no encontrado", exception.getMessage());
        verify(rolRepository, never()).save(any(Rol.class));
    }

    @Test
    void actualizarRol_CuandoMismoNombre_ActualizaRol() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(rolRepository.findByNombre(anyString())).thenReturn(rol);
        when(rolRepository.save(any(Rol.class))).thenReturn(rol);

        Rol resultado = rolService.actualizarRol(1L, rolDTO);

        assertNotNull(resultado);
        verify(rolRepository).save(any(Rol.class));
    }
}