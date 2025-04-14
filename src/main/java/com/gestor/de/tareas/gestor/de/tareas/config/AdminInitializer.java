package com.gestor.de.tareas.gestor.de.tareas.config;

import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;
import com.gestor.de.tareas.gestor.de.tareas.enums.Rol;
import com.gestor.de.tareas.gestor.de.tareas.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void crearUsuarioAdmin() {
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = Usuario.builder()
                    .puesto("Administrador Principal")
                    .username("admin")
                    .email("admin@empresa.com")
                    .password(passwordEncoder.encode("admin1234"))
                    .rol(Rol.ADMIN)
                    .build();

            usuarioRepository.save(admin);
            System.out.println("✅ Usuario ADMIN creado: admin / admin1234");
        }
    }
}