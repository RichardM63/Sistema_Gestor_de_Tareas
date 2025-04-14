package com.gestor.de.tareas.gestor.de.tareas.repository;

import com.gestor.de.tareas.gestor.de.tareas.entity.RespuestaTarea;
import com.gestor.de.tareas.gestor.de.tareas.entity.Tarea;
import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RespuestaTareaRepository extends JpaRepository<RespuestaTarea, Long> {
    List<RespuestaTarea> findByTarea(Tarea tarea);
    Optional<RespuestaTarea> findByTareaAndTrabajador(Tarea tarea, Usuario trabajador);

}
