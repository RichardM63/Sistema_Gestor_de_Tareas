package com.gestor.de.tareas.gestor.de.tareas.entity.dto;

import com.gestor.de.tareas.gestor.de.tareas.enums.Rol;
import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String username;
    private String email;
    private Rol rol;
}