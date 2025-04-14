package com.gestor.de.tareas.gestor.de.tareas.serviceImpl;

import com.gestor.de.tareas.gestor.de.tareas.entity.Grupo;
import com.gestor.de.tareas.gestor.de.tareas.entity.Tarea;
import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;
import com.gestor.de.tareas.gestor.de.tareas.enums.EstadoTarea;
import com.gestor.de.tareas.gestor.de.tareas.repository.GrupoRepository;
import com.gestor.de.tareas.gestor.de.tareas.repository.TareaRepository;
import com.gestor.de.tareas.gestor.de.tareas.repository.UsuarioRepository;
import com.gestor.de.tareas.gestor.de.tareas.service.TareaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TareaServiceImpl implements TareaService {

    private final TareaRepository tareaRepository;
    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Tarea> listarTodas() {
        return tareaRepository.findAll();
    }

    @Override
    public Tarea actualizarEstado(Long id, EstadoTarea estado) {
        Tarea tarea = tareaRepository.findById(id).orElseThrow();
        tarea.setId(id);
        tarea.setEstado(estado); // Cambiar el estado de la tarea
        return tareaRepository.save(tarea);
    }

    @Override
    public Optional<Tarea> obtenerPorId(Long id) {
        return tareaRepository.findById(id);
    }

    @Override
    public List<Tarea> findByGrupoIdOrAsignadoId(List<Long> grupoIds,Long usuarioId) {
        return tareaRepository.findByGrupoIdOrAsignadoId(grupoIds,usuarioId);
    }

    @Override
    public void crearTareaConCreador(Tarea tarea, String username) {
        // Asumimos que el creador viene a partir del username
        Usuario creador = usuarioRepository.findByUsername(username).orElseThrow();// Deberías obtener al usuario por el username desde el service UsuarioServiceImpl
        tarea.setCreador(creador); // Se asigna al creador
        tarea.setEstado(EstadoTarea.PENDIENTE); // Se establece el estado por defecto
        tareaRepository.save(tarea);
    }

    @Override
    public List<Tarea> obtenerPorEstado(EstadoTarea estado) {
        return tareaRepository.findByEstado(estado);
    }

    @Override
    public long contarPorEstado(EstadoTarea estado) {
        return tareaRepository.countByEstado(estado);
    }

    @Override
    public void asignarTarea(Long tareaId, Long grupoId, Long trabajadorId) {
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        if (grupoId != null) {
            Grupo grupo = grupoRepository.findById(grupoId).orElse(null);
            tarea.setGrupo(grupo);
            tarea.setAsignado(null);
        } else if (trabajadorId != null) {
            Usuario trabajador = usuarioRepository.findById(trabajadorId).orElse(null);
            tarea.setAsignado(trabajador);
            tarea.setGrupo(null);
        }

        tareaRepository.save(tarea);
    }

    public long contarTareasPorEstado(List<Tarea> tareas,EstadoTarea estadoTarea) {
        return tareas.stream().filter(tarea->tarea.getEstado()==estadoTarea).count();
    }

    @Override
    public boolean puedeVerTarea(Tarea tarea, Usuario usuario) {
        boolean asignadaDirectamente = tarea.getAsignado() != null && tarea.getAsignado().getId().equals(usuario.getId());
        boolean asignadaAGrupo = tarea.getGrupo() != null && usuario.getGrupos().contains(tarea.getGrupo());

        return asignadaDirectamente || asignadaAGrupo;
    }

    @Override
    public Map<String, Object> generarDatosPorDias(int dias) {
        LocalDate desde = LocalDate.now().minusDays(dias); // CAMBIADO de LocalDateTime a LocalDate
        List<Tarea> tareas = tareaRepository.findByFechaInicioAfter(desde); // también cambiaste el método del repo

        Map<EstadoTarea, Long> conteo = tareas.stream()
                .collect(Collectors.groupingBy(Tarea::getEstado, Collectors.counting()));

        Map<String, Object> datos = new HashMap<>();
        datos.put("labels", List.of("Pendientes", "Completadas", "Vencidas", "Con Check", "Rechazadas"));
        datos.put("datasets", List.of(Map.of(
                "label", "Tareas",
                "data", List.of(
                        conteo.getOrDefault(EstadoTarea.PENDIENTE, 0L),
                        conteo.getOrDefault(EstadoTarea.COMPLETADO_SIN_CHECK, 0L),
                        conteo.getOrDefault(EstadoTarea.VENCIDO, 0L),
                        conteo.getOrDefault(EstadoTarea.COMPLETADO_CON_CHECK, 0L),
                        conteo.getOrDefault(EstadoTarea.RECHAZADO, 0L)
                ),
                "backgroundColor", List.of("#3B82F6", "#10B981", "#EF4444", "#6366F1", "#F59E0B")
        )));

        return datos;
    }



}

