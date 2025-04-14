package com.gestor.de.tareas.gestor.de.tareas.serviceImpl;

import com.gestor.de.tareas.gestor.de.tareas.entity.RespuestaTarea;
import com.gestor.de.tareas.gestor.de.tareas.entity.Tarea;
import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;
import com.gestor.de.tareas.gestor.de.tareas.repository.RespuestaTareaRepository;
import com.gestor.de.tareas.gestor.de.tareas.repository.TareaRepository;
import com.gestor.de.tareas.gestor.de.tareas.repository.UsuarioRepository;
import com.gestor.de.tareas.gestor.de.tareas.service.RespuestaTareaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RespuestaTareaServiceImpl implements RespuestaTareaService {

    private final RespuestaTareaRepository respuestaRepo;
    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void responderTarea(Long tareaId, String descripcion, MultipartFile archivo, String username) throws IOException {
        // Obtener la tarea y el trabajador desde sus respectivos servicios
        Tarea tarea = tareaRepository.findById(tareaId).orElseThrow();
        Usuario trabajador = usuarioRepository.findByUsername(username).orElseThrow();

        // Guardar archivo si existe
        String nombreArchivo = archivo.isEmpty() ? null : archivo.getOriginalFilename();
        if (nombreArchivo != null) {
            Path destino = Paths.get("uploads/" + nombreArchivo);
            Files.createDirectories(destino.getParent());
            archivo.transferTo(destino);
        }

        // Crear y guardar la respuesta de la tarea
        RespuestaTarea respuesta = RespuestaTarea.builder()
                .descripcion(descripcion)
                .archivo(nombreArchivo)
                .fechaRespuesta(LocalDateTime.now())
                .tarea(tarea)
                .trabajador(trabajador)
                .build();

        respuestaRepo.save(respuesta);
    }

    @Override
    public Optional<RespuestaTarea> obtenerPorTareaYTrabajador(Tarea tarea, Usuario trabajador) {
        return respuestaRepo.findByTareaAndTrabajador(tarea, trabajador);
    }


    @Override
    public List<RespuestaTarea> listarPorTarea(Tarea tarea) {
        return respuestaRepo.findByTarea(tarea);
    }


}
