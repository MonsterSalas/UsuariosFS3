package com.usuarios.usuario.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private String username;
    private String password;
    private String nombreCompleto;
    private Long rolId;
}
