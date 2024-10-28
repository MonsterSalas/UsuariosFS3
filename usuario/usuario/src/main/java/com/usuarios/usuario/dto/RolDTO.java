package com.usuarios.usuario.dto;

import lombok.Data;
import java.util.List;
@Data
public class RolDTO {
    private String nombre;
    private List<String> permisos;
}