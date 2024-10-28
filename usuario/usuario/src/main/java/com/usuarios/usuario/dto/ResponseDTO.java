package com.usuarios.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {
    private String mensaje;
    private Object data;
    private boolean exito;
}