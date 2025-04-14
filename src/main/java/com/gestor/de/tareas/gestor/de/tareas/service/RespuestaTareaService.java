package com.gestor.de.tareas.gestor.de.tareas.service;

import com.gestor.de.tareas.gestor.de.tareas.entity.RespuestaTarea;
import com.gestor.de.tareas.gestor.de.tareas.entity.Tarea;
import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface RespuestaTareaService {

    void responderTarea(Long tareaId, String descripcion, MultipartFile archivo, String username) throws IOException, IOException;

    Optional<RespuestaTarea> obtenerPorTareaYTrabajador(Tarea tarea, Usuario trabajador);

    List<RespuestaTarea> listarPorTarea(Tarea tarea);
}
