package com.gestor.de.tareas.gestor.de.tareas.serviceImpl;

import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;
import com.gestor.de.tareas.gestor.de.tareas.enums.Rol;
import com.gestor.de.tareas.gestor.de.tareas.repository.UsuarioRepository;
import com.gestor.de.tareas.gestor.de.tareas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public boolean eliminarUsuario(Long id) {
        Optional<Usuario> usuario = obtenerPorId(id);
        if (usuario.isEmpty() || usuario.get().getRol().name().equals("ADMIN")) {
            return false; // No eliminar un ADMIN
        }
        usuarioRepository.deleteById(id);
        return true;
    }
    @Override
    public boolean editarUsuario(Usuario usuario,Long id) {
        Optional<Usuario> usuarioExistente = obtenerPorId(id);
        Usuario actual = usuarioExistente.get();
        actual.setId(id);
        actual.setUsername(usuario.getUsername());
        actual.setEmail(usuario.getEmail());
        actual.setPuesto(usuario.getPuesto());

        usuarioRepository.save(actual);
        return true;
    }


    @Override
    public Usuario crearUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRol(Rol.TRABAJADOR);
        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> obtenerPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol).orElseThrow();
    }
}
