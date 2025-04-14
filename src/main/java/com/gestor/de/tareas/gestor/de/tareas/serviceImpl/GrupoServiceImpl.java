package com.gestor.de.tareas.gestor.de.tareas.serviceImpl;

import com.gestor.de.tareas.gestor.de.tareas.entity.Grupo;
import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;
import com.gestor.de.tareas.gestor.de.tareas.enums.EstadoTarea;
import com.gestor.de.tareas.gestor.de.tareas.repository.GrupoRepository;
import com.gestor.de.tareas.gestor.de.tareas.repository.UsuarioRepository;
import com.gestor.de.tareas.gestor.de.tareas.service.GrupoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrupoServiceImpl implements GrupoService {

    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Grupo> listarTodos() {
        return grupoRepository.findAll();
    }

    @Override
    public Grupo asignarMiembros(Long grupoId, List<Long> idUsuarios) {
        return null;
    }

    @Override
    public Optional<Grupo> obtenerPorId(Long id) {
        return grupoRepository.findById(id);
    }

    @Override
    public Grupo crearGrupo(Grupo grupo) {
        return grupoRepository.save(grupo);
    }

    @Override
    public void crearGrupoConMiembros(Grupo grupo, List<Long> miembrosIds) {
        // Aquí agregamos la lógica para crear un grupo y asociar los miembros
        grupoRepository.save(grupo);
        // Agregar los miembros a la tabla intermedia
    }

    @Override
    public void actualizarMiembros(Long grupoId, List<Long> usuarioIds) {
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        List<Usuario> nuevosMiembros = usuarioRepository.findAllById(usuarioIds)
                .stream()
                .collect(Collectors.toList());

        grupo.setMiembros(nuevosMiembros);
        grupoRepository.save(grupo);
    }

    @Override
    public List<Grupo> listarGruposDelTrabajador(Usuario usuario) {
         return grupoRepository.findByMiembrosContaining(usuario);
    }


}

