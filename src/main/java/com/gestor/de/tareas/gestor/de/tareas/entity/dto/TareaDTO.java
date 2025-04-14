package com.gestor.de.tareas.gestor.de.tareas.entity.dto;

import com.gestor.de.tareas.gestor.de.tareas.enums.EstadoTarea;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TareaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaVencimiento;
    private EstadoTarea estado;
    private String nombreCreador;
    private String nombreAsignado;
}

