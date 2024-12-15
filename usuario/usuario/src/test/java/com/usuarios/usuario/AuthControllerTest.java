package com.usuarios.usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.usuarios.usuario.controllers.AuthController;
import com.usuarios.usuario.models.Rol;
import com.usuarios.usuario.models.Usuario;
import com.usuarios.usuario.repository.UsuarioRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ConCredencialesValidas_RetornaLoginExitoso() {
        // Arrange
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "usuario1");
        credentials.put("password", "password123");

        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ADMIN");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombreCompleto("Usuario Prueba");
        usuario.setUsername("usuario1");
        usuario.setPassword("password123");
        usuario.setRol(rol);

        when(usuarioRepository.findByUsernameAndPassword("usuario1", "password123"))
            .thenReturn(Optional.of(usuario));

        // Act
        ResponseEntity<?> response = authController.login(credentials);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Login exitoso", responseBody.get("mensaje"));
        assertEquals("ADMIN", responseBody.get("rol"));
        assertEquals("usuario1", responseBody.get("username"));
    }

    @Test
    void login_ConCredencialesInvalidas_RetornaBadRequest() {
        // Arrange
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "usuarioInvalido");
        credentials.put("password", "passwordInvalido");

        when(usuarioRepository.findByUsernameAndPassword(anyString(), anyString()))
            .thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = authController.login(credentials);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Credenciales inválidas", responseBody.get("mensaje"));
    }

    @Test
    void login_ConCredencialesNulas_RetornaBadRequest() {
        // Arrange
        Map<String, String> credentials = new HashMap<>();
        // No agregamos username ni password

        when(usuarioRepository.findByUsernameAndPassword(null, null))
            .thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = authController.login(credentials);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Credenciales inválidas", responseBody.get("mensaje"));
    }

    @Test
    void login_ConSoloUsername_RetornaBadRequest() {
        // Arrange
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "usuario1");
        // No agregamos password

        when(usuarioRepository.findByUsernameAndPassword("usuario1", null))
            .thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = authController.login(credentials);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Credenciales inválidas", responseBody.get("mensaje"));
    }

    @Test
    void login_ConSoloPassword_RetornaBadRequest() {
        // Arrange
        Map<String, String> credentials = new HashMap<>();
        credentials.put("password", "password123");
        // No agregamos username

        when(usuarioRepository.findByUsernameAndPassword(null, "password123"))
            .thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = authController.login(credentials);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Credenciales inválidas", responseBody.get("mensaje"));
    }
}