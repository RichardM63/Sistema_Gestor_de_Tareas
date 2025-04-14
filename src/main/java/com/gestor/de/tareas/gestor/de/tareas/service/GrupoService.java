package com.gestor.de.tareas.gestor.de.tareas.service;

import com.gestor.de.tareas.gestor.de.tareas.entity.Grupo;
import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GrupoService {
    Grupo crearGrupo(Grupo grupo);
    Optional<Grupo> obtenerPorId(Long id);
    List<Grupo> listarTodos();
    Grupo asignarMiembros(Long grupoId, List<Long> idUsuarios);

    void crearGrupoConMiembros(Grupo grupo, List<Long> miembrosIds);

    void actualizarMiembros(Long grupoId, List<Long> usuarioIds);
    List<Grupo> listarGruposDelTrabajador(Usuario  usuario);
}