package com.usuarios.usuario;

import com.usuarios.usuario.controllers.UsuarioController;
import com.usuarios.usuario.dto.ResponseDTO;
import com.usuarios.usuario.dto.UsuarioDTO;
import com.usuarios.usuario.models.Usuario;
import com.usuarios.usuario.models.Rol;
import com.usuarios.usuario.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearUsuario_ConDatosValidos_RetornaUsuarioCreado() {
        // Arrange
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsername("usuario1");
        usuarioDTO.setPassword("password123");
        usuarioDTO.setNombreCompleto("Usuario Prueba");
        usuarioDTO.setRolId(1L);

        Usuario usuarioCreado = new Usuario();
        usuarioCreado.setId(1L);
        usuarioCreado.setUsername("usuario1");
        usuarioCreado.setPassword("password123");
        usuarioCreado.setNombreCompleto("Usuario Prueba");
        
        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("USER");
        usuarioCreado.setRol(rol);

        when(usuarioService.crearUsuario(any(UsuarioDTO.class))).thenReturn(usuarioCreado);

        // Act
        ResponseEntity<ResponseDTO> response = usuarioController.crearUsuario(usuarioDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isExito());
        assertEquals("Usuario creado exitosamente", response.getBody().getMensaje());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void crearUsuario_SinUsername_RetornaBadRequest() {
        // Arrange
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setPassword("password123");
        usuarioDTO.setNombreCompleto("Usuario Prueba");
        usuarioDTO.setRolId(1L);

        // Act
        ResponseEntity<ResponseDTO> response = usuarioController.crearUsuario(usuarioDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isExito());
        assertEquals("El username es requerido", response.getBody().getMensaje());
    }

    @Test
    void crearUsuario_SinPassword_RetornaBadRequest() {
        // Arrange
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsername("usuario1");
        usuarioDTO.setNombreCompleto("Usuario Prueba");
        usuarioDTO.setRolId(1L);

        // Act
        ResponseEntity<ResponseDTO> response = usuarioController.crearUsuario(usuarioDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isExito());
        assertEquals("La contraseña es requerida", response.getBody().getMensaje());
    }

    @Test
    void crearUsuario_SinNombreCompleto_RetornaBadRequest() {
        // Arrange
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsername("usuario1");
        usuarioDTO.setPassword("password123");
        usuarioDTO.setRolId(1L);

        // Act
        ResponseEntity<ResponseDTO> response = usuarioController.crearUsuario(usuarioDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isExito());
        assertEquals("El nombre completo es requerido", response.getBody().getMensaje());
    }

    @Test
    void crearUsuario_SinRol_RetornaBadRequest() {
        // Arrange
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsername("usuario1");
        usuarioDTO.setPassword("password123");
        usuarioDTO.setNombreCompleto("Usuario Prueba");

        // Act
        ResponseEntity<ResponseDTO> response = usuarioController.crearUsuario(usuarioDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isExito());
        assertEquals("El rol es requerido", response.getBody().getMensaje());
    }

    @Test
    void getAllUsuarios_RetornaListaUsuarios() {
        // Arrange
        List<Usuario> usuariosEsperados = Arrays.asList(
            crearUsuarioPrueba(1L, "usuario1", "Usuario Uno"),
            crearUsuarioPrueba(2L, "usuario2", "Usuario Dos")
        );
        when(usuarioService.findAll()).thenReturn(usuariosEsperados);

        // Act
        List<Usuario> resultado = usuarioController.getAllUsuarios();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(usuarioService, times(1)).findAll();
    }

    @Test
    void getUsuarioById_ConIdExistente_RetornaUsuario() {
        // Arrange
        Long id = 1L;
        Usuario usuarioEsperado = crearUsuarioPrueba(id, "usuario1", "Usuario Uno");
        when(usuarioService.findById(id)).thenReturn(usuarioEsperado);

        // Act
        ResponseEntity<Usuario> response = usuarioController.getUsuarioById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
    }

    @Test
    void getUsuarioById_ConIdInexistente_RetornaNotFound() {
        // Arrange
        Long id = 999L;
        when(usuarioService.findById(id)).thenReturn(null);

        // Act
        ResponseEntity<Usuario> response = usuarioController.getUsuarioById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateUsuario_ConIdExistente_RetornaUsuarioActualizado() {
        // Arrange
        Long id = 1L;
        Usuario usuarioActualizado = crearUsuarioPrueba(id, "usuario1", "Usuario Actualizado");
        when(usuarioService.findById(id)).thenReturn(usuarioActualizado);
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        // Act
        ResponseEntity<Usuario> response = usuarioController.updateUsuario(id, usuarioActualizado);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Usuario Actualizado", response.getBody().getNombreCompleto());
    }

    @Test
    void updateUsuario_ConIdInexistente_RetornaNotFound() {
        // Arrange
        Long id = 999L;
        Usuario usuario = crearUsuarioPrueba(id, "usuario1", "Usuario Uno");
        when(usuarioService.findById(id)).thenReturn(null);

        // Act
        ResponseEntity<Usuario> response = usuarioController.updateUsuario(id, usuario);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteUsuario_ConIdExistente_RetornaOk() {
        // Arrange
        Long id = 1L;
        Usuario usuario = crearUsuarioPrueba(id, "usuario1", "Usuario Uno");
        when(usuarioService.findById(id)).thenReturn(usuario);
        doNothing().when(usuarioService).deleteById(id);

        // Act
        ResponseEntity<Void> response = usuarioController.deleteUsuario(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usuarioService, times(1)).deleteById(id);
    }

    @Test
    void deleteUsuario_ConIdInexistente_RetornaNotFound() {
        // Arrange
        Long id = 999L;
        when(usuarioService.findById(id)).thenReturn(null);

        // Act
        ResponseEntity<Void> response = usuarioController.deleteUsuario(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(usuarioService, never()).deleteById(anyLong());
    }

    private Usuario crearUsuarioPrueba(Long id, String username, String nombreCompleto) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setUsername(username);
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setPassword("password");
        
        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("USER");
        usuario.setRol(rol);
        
        return usuario;
    }
}