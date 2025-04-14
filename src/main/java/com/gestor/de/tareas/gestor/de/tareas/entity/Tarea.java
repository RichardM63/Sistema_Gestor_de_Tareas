package com.gestor.de.tareas.gestor.de.tareas.entity;

import com.gestor.de.tareas.gestor.de.tareas.enums.EstadoTarea;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    private EstadoTarea estado;

    @ManyToOne
    private Usuario creador;

    @ManyToOne
    private Usuario asignado;

    @ManyToOne
    private Grupo grupo;
}

