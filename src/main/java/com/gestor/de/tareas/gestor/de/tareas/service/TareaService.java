package com.gestor.de.tareas.gestor.de.tareas.service;

import com.gestor.de.tareas.gestor.de.tareas.entity.Tarea;
import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;
import com.gestor.de.tareas.gestor.de.tareas.enums.EstadoTarea;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TareaService {
    Optional<Tarea> obtenerPorId(Long id);
    List<Tarea> listarTodas();
    Tarea actualizarEstado(Long id, EstadoTarea estado);

    List<Tarea> findByGrupoIdOrAsignadoId(List<Long> grupoIds, Long usuarioId);

    void crearTareaConCreador(Tarea tarea, String username);

    List<Tarea> obtenerPorEstado(EstadoTarea estado);

    long contarPorEstado(EstadoTarea estado);
    void asignarTarea(Long tareaId, Long grupoId, Long trabajadorId);

    boolean puedeVerTarea(Tarea tarea, Usuario usuario);

    Map<String, Object> generarDatosPorDias(int dias);

}