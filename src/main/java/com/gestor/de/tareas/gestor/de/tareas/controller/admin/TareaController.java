package com.gestor.de.tareas.gestor.de.tareas.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gestor.de.tareas.gestor.de.tareas.entity.Grupo;
import com.gestor.de.tareas.gestor.de.tareas.entity.Tarea;
import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;
import com.gestor.de.tareas.gestor.de.tareas.enums.Rol;
import com.gestor.de.tareas.gestor.de.tareas.serviceImpl.GrupoServiceImpl;
import com.gestor.de.tareas.gestor.de.tareas.serviceImpl.TareaServiceImpl;
import com.gestor.de.tareas.gestor.de.tareas.serviceImpl.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin/tareas")
@RequiredArgsConstructor
public class TareaController {

    private final UsuarioServiceImpl usuarioService;
    private final TareaServiceImpl tareaService;
    private final GrupoServiceImpl grupoService;
    private final ObjectMapper objectMapper;

    @GetMapping("")
    public String verTareas(Model model) {
        model.addAttribute("tareas", tareaService.listarTodas());
        model.addAttribute("usuarios", usuarioService.obtenerPorRol(Rol.TRABAJADOR));
        model.addAttribute("grupos", grupoService.listarTodos());
        return "admin/tareas";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrearTareas(Model model) {
        model.addAttribute("tarea", new Tarea());
        return "admin/crear_tarea";
    }

    @PostMapping("/crear")
    public String crearTarea(@ModelAttribute("tarea") Tarea tarea, Principal principal) {
        tareaService.crearTareaConCreador(tarea,principal.getName());
        return "redirect:/admin/tareas";
    }

    @GetMapping("/asignar/{id}")
    public String mostrarFormularioAsignar(@PathVariable Long id, Model model) {
        Tarea tarea = tareaService.obtenerPorId(id).orElseThrow();
        List<Grupo> grupos = grupoService.listarTodos();
        List<Usuario> trabajadores = usuarioService.obtenerPorRol(Rol.TRABAJADOR);

        model.addAttribute("tarea", tarea);
        model.addAttribute("grupos", grupos);
        model.addAttribute("trabajadores", trabajadores);
        return "admin/asignar_tarea";
    }

    @PostMapping("/asignar/{id}")
    public String asignarTarea(
            @PathVariable Long id,
            @RequestParam(required = false) Long grupoId,
            @RequestParam(required = false) Long trabajadorId
    ) {
        tareaService.asignarTarea(id, grupoId, trabajadorId);
        return "redirect:/admin/tareas";
    }

    @GetMapping("/estadisticas")
    public String verEstadisticas(Model model) throws JsonProcessingException, JsonProcessingException {
        model.addAttribute("estadisticasDia", objectMapper.writeValueAsString(tareaService.generarDatosPorDias(1)));
        model.addAttribute("estadisticas10Dias", objectMapper.writeValueAsString(tareaService.generarDatosPorDias(10)));
        model.addAttribute("estadisticas15Dias", objectMapper.writeValueAsString(tareaService.generarDatosPorDias(15)));
        return "admin/estadisticas";
    }
}
