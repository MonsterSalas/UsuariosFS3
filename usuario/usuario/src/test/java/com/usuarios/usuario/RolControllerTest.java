package com.usuarios.usuario;


import com.usuarios.usuario.dto.RolDTO;
import com.usuarios.usuario.controllers.RolController;
import com.usuarios.usuario.dto.ResponseDTO;
import com.usuarios.usuario.models.Rol;
import com.usuarios.usuario.services.RolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class RolControllerTest {

    @Mock
    private RolService rolService;

    @InjectMocks
    private RolController rolController;

    private RolDTO rolDTO;
    private Rol rol;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        rolDTO = new RolDTO();
        rolDTO.setNombre("ADMIN");
        rolDTO.setPermisos(Arrays.asList("READ", "WRITE"));

        rol = Rol.builder()
                .id(1L)
                .nombre("ADMIN")
                .permisos(Arrays.asList("READ", "WRITE"))
                .build();
    }

    @Test
    void crearRol_ConDatosValidos_RetornaRolCreado() {
        when(rolService.crearRol(any(RolDTO.class))).thenReturn(rol);
        
        ResponseEntity<ResponseDTO> response = rolController.crearRol(rolDTO);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().isExito());
        assertEquals("Rol creado exitosamente", response.getBody().getMensaje());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void crearRol_ConNombreNulo_RetornaBadRequest() {
        rolDTO.setNombre(null);
        
        ResponseEntity<ResponseDTO> response = rolController.crearRol(rolDTO);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().isExito());
        assertEquals("El nombre del rol es requerido", response.getBody().getMensaje());
    }

    @Test
    void crearRol_ConNombreVacio_RetornaBadRequest() {
        rolDTO.setNombre("  ");
        
        ResponseEntity<ResponseDTO> response = rolController.crearRol(rolDTO);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().isExito());
        assertEquals("El nombre del rol es requerido", response.getBody().getMensaje());
    }

    @Test
    void crearRol_CuandoOcurreRuntimeException_RetornaConflict() {
        when(rolService.crearRol(any(RolDTO.class)))
            .thenThrow(new RuntimeException("Rol duplicado"));
        
        ResponseEntity<ResponseDTO> response = rolController.crearRol(rolDTO);
        
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertFalse((Boolean) response.getBody().isExito());
        assertEquals("Rol duplicado", response.getBody().getMensaje());
    }

    @Test
    void obtenerTodosLosRoles_CuandoHayRoles_RetornaListaRoles() {
        List<Rol> roles = Arrays.asList(rol, 
            Rol.builder().id(2L).nombre("USER").permisos(Collections.singletonList("READ")).build());
        when(rolService.obtenerTodosLosRoles()).thenReturn(roles);
        
        ResponseEntity<ResponseDTO> response = rolController.obtenerTodosLosRoles();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().isExito());
        assertEquals("Roles obtenidos exitosamente", response.getBody().getMensaje());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void obtenerTodosLosRoles_CuandoNoHayRoles_RetornaListaVacia() {
        when(rolService.obtenerTodosLosRoles()).thenReturn(Collections.emptyList());
        
        ResponseEntity<ResponseDTO> response = rolController.obtenerTodosLosRoles();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().isExito());
        assertTrue(((List<?>) response.getBody().getData()).isEmpty());
    }

    @Test
    void obtenerRolPorId_CuandoExisteRol_RetornaRol() {
        when(rolService.obtenerRolPorId(1L)).thenReturn(rol);
        
        ResponseEntity<ResponseDTO> response = rolController.obtenerRolPorId(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().isExito());
        assertEquals("Rol encontrado", response.getBody().getMensaje());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void obtenerRolPorId_CuandoNoExiste_RetornaNotFound() {
        when(rolService.obtenerRolPorId(1L))
            .thenThrow(new RuntimeException("Rol no encontrado"));
        
        ResponseEntity<ResponseDTO> response = rolController.obtenerRolPorId(1L);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse((Boolean) response.getBody().isExito());
        assertEquals("Rol no encontrado", response.getBody().getMensaje());
    }

    @Test
    void actualizarRol_CuandoActualizacionExitosa_RetornaRolActualizado() {
        when(rolService.actualizarRol(anyLong(), any(RolDTO.class))).thenReturn(rol);
        
        ResponseEntity<ResponseDTO> response = rolController.actualizarRol(1L, rolDTO);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().isExito());
        assertEquals("Rol actualizado exitosamente", response.getBody().getMensaje());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void actualizarRol_CuandoOcurreRuntimeException_RetornaConflict() {
        when(rolService.actualizarRol(anyLong(), any(RolDTO.class)))
            .thenThrow(new RuntimeException("Error al actualizar"));
        
        ResponseEntity<ResponseDTO> response = rolController.actualizarRol(1L, rolDTO);
        
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertFalse((Boolean) response.getBody().isExito());
        assertEquals("Error al actualizar", response.getBody().getMensaje());
    }

    @Test
    void eliminarRol_CuandoEliminacionExitosa_RetornaOk() {
        doNothing().when(rolService).eliminarRol(1L);
        
        ResponseEntity<ResponseDTO> response = rolController.eliminarRol(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().isExito());
        assertEquals("Rol eliminado exitosamente", response.getBody().getMensaje());
    }

    @Test
    void eliminarRol_CuandoNoExiste_RetornaNotFound() {
        doThrow(new RuntimeException("Rol no encontrado"))
            .when(rolService).eliminarRol(1L);
        
        ResponseEntity<ResponseDTO> response = rolController.eliminarRol(1L);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse((Boolean) response.getBody().isExito());
        assertEquals("Rol no encontrado", response.getBody().getMensaje());
    }
}