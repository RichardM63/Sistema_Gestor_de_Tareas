package com.gestor.de.tareas.gestor.de.tareas;

import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;
import com.gestor.de.tareas.gestor.de.tareas.enums.Rol;
import com.gestor.de.tareas.gestor.de.tareas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestorDeTareasApplication {
	@Autowired
	private UsuarioService usuarioService;

	public static void main(String[] args) {
		SpringApplication.run(GestorDeTareasApplication.class, args);
	}

}
