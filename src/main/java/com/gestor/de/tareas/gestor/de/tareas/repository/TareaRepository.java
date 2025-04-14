package com.gestor.de.tareas.gestor.de.tareas.repository;

import com.gestor.de.tareas.gestor.de.tareas.entity.Tarea;
import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;
import com.gestor.de.tareas.gestor.de.tareas.enums.EstadoTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
    @Query("SELECT t FROM Tarea t WHERE t.grupo.id in :grupoIds OR t.asignado.id = :usuarioId")
    List<Tarea> findByGrupoIdOrAsignadoId(@Param("grupoIds") List<Long> grupoIds, @Param("usuarioId") Long usuarioId);
    List<Tarea> findByEstado(EstadoTarea estado);
    long countByEstado(EstadoTarea estado);
    List<Tarea> findByFechaInicioAfter(LocalDate fecha);

}