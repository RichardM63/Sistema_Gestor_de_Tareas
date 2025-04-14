package com.gestor.de.tareas.gestor.de.tareas.service;

import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;
import com.gestor.de.tareas.gestor.de.tareas.enums.Rol;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario crearUsuario(Usuario usuario);
    Optional<Usuario> obtenerPorId(Long id);
    Optional<Usuario> obtenerPorUsername(String username);
    List<Usuario> listarTodos();
    List<Usuario> obtenerPorRol(Rol rol);

    boolean eliminarUsuario(Long id);

    boolean editarUsuario(Usuario usuario,Long id);
}