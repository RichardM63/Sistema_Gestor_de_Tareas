package com.gestor.de.tareas.gestor.de.tareas.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class GrupoDTO {
    private Long id;
    private String puesto;
    private List<String> miembrosEmails;
}
