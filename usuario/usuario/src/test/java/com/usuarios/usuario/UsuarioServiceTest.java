package com.usuarios.usuario;

import com.usuarios.usuario.dto.UsuarioDTO;
import com.usuarios.usuario.models.Rol;
import com.usuarios.usuario.models.Usuario;
import com.usuarios.usuario.repository.RolRepository;
import com.usuarios.usuario.repository.UsuarioRepository;
import com.usuarios.usuario.services.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UsuarioService usuarioService;

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

        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("USER");

        when(usuarioRepository.findByUsername("usuario1")).thenReturn(null);
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        
        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setId(1L);
        usuarioGuardado.setUsername("usuario1");
        usuarioGuardado.setPassword("password123");
        usuarioGuardado.setNombreCompleto("Usuario Prueba");
        usuarioGuardado.setRol(rol);

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        // Act
        Usuario resultado = usuarioService.crearUsuario(usuarioDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("usuario1", resultado.getUsername());
        assertEquals("Usuario Prueba", resultado.getNombreCompleto());
        assertEquals(rol.getId(), resultado.getRol().getId());
    }

    @Test
    void crearUsuario_ConUsernameExistente_LanzaExcepcion() {
        // Arrange
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsername("usuario1");
        usuarioDTO.setPassword("password123");
        usuarioDTO.setNombreCompleto("Usuario Prueba");
        usuarioDTO.setRolId(1L);

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setUsername("usuario1");

        when(usuarioRepository.findByUsername("usuario1")).thenReturn(usuarioExistente);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuarioDTO);
        });
        assertEquals("El username ya está en uso", exception.getMessage());
    }

    @Test
    void crearUsuario_ConNombreCompletoVacio_LanzaExcepcion() {
        // Arrange
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsername("usuario1");
        usuarioDTO.setPassword("password123");
        usuarioDTO.setNombreCompleto("");
        usuarioDTO.setRolId(1L);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuarioDTO);
        });
        assertEquals("El nombre completo es requerido", exception.getMessage());
    }

    @Test
    void crearUsuario_ConRolInexistente_LanzaExcepcion() {
        // Arrange
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsername("usuario1");
        usuarioDTO.setPassword("password123");
        usuarioDTO.setNombreCompleto("Usuario Prueba");
        usuarioDTO.setRolId(999L);

        when(usuarioRepository.findByUsername("usuario1")).thenReturn(null);
        when(rolRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuarioDTO);
        });
        assertEquals("Rol no encontrado", exception.getMessage());
    }

    @Test
    void findAll_RetornaListaUsuarios() {
        // Arrange
        List<Usuario> usuariosEsperados = Arrays.asList(
            crearUsuarioPrueba(1L, "usuario1", "Usuario Uno"),
            crearUsuarioPrueba(2L, "usuario2", "Usuario Dos")
        );
        when(usuarioRepository.findAll()).thenReturn(usuariosEsperados);

        // Act
        List<Usuario> resultado = usuarioService.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    void findById_ConIdExistente_RetornaUsuario() {
        // Arrange
        Long id = 1L;
        Usuario usuarioEsperado = crearUsuarioPrueba(id, "usuario1", "Usuario Uno");
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioEsperado));

        // Act
        Usuario resultado = usuarioService.findById(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
    }

    @Test
    void findById_ConIdInexistente_RetornaNull() {
        // Arrange
        Long id = 999L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Usuario resultado = usuarioService.findById(id);

        // Assert
        assertNull(resultado);
    }

    @Test
    void save_ConUsuarioValido_RetornaUsuarioGuardado() {
        // Arrange
        Usuario usuario = crearUsuarioPrueba(1L, "usuario1", "Usuario Uno");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        Usuario resultado = usuarioService.save(usuario);

        // Assert
        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());
    }

    @Test
    void deleteById_EliminaUsuario() {
        // Arrange
        Long id = 1L;
        doNothing().when(usuarioRepository).deleteById(id);

        // Act
        usuarioService.deleteById(id);

        // Assert
        verify(usuarioRepository, times(1)).deleteById(id);
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