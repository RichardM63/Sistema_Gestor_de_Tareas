package com.gestor.de.tareas.gestor.de.tareas.controller.trabajador;

import com.gestor.de.tareas.gestor.de.tareas.entity.Grupo;
import com.gestor.de.tareas.gestor.de.tareas.entity.RespuestaTarea;
import com.gestor.de.tareas.gestor.de.tareas.entity.Tarea;
import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;
import com.gestor.de.tareas.gestor.de.tareas.enums.EstadoTarea;
import com.gestor.de.tareas.gestor.de.tareas.service.TareaService;
import com.gestor.de.tareas.gestor.de.tareas.service.UsuarioService;
import com.gestor.de.tareas.gestor.de.tareas.serviceImpl.GrupoServiceImpl;
import com.gestor.de.tareas.gestor.de.tareas.serviceImpl.RespuestaTareaServiceImpl;
import com.gestor.de.tareas.gestor.de.tareas.serviceImpl.TareaServiceImpl;
import com.gestor.de.tareas.gestor.de.tareas.serviceImpl.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/trabajador")
@RequiredArgsConstructor
public class TrabajadorController {

    private final TareaServiceImpl tareaService;
    private final UsuarioServiceImpl usuarioService;
    private final GrupoServiceImpl grupoService;
    private final RespuestaTareaServiceImpl respuestaTareaService;

    @GetMapping("/tareas")
    public String dashboard(Principal principal, Model model) {
        String username =principal.getName();
        Usuario usuario = usuarioService.obtenerPorUsername(username).orElseThrow();
        List<Long> groupIds = grupoService.listarGruposDelTrabajador(usuario).stream().map(Grupo::getId).toList();
        List<Tarea> tareas = tareaService.findByGrupoIdOrAsignadoId(groupIds,usuario.getId());

        long pendientes = tareaService.contarTareasPorEstado(tareas, EstadoTarea.PENDIENTE);
        long completadas = tareaService.contarTareasPorEstado(tareas, EstadoTarea.COMPLETADO_CON_CHECK);
        long vencidas = tareaService.contarTareasPorEstado(tareas, EstadoTarea.VENCIDO);

        // Pasar los valores al modelo
        model.addAttribute("pendientes", pendientes);
        model.addAttribute("completadas", completadas);
        model.addAttribute("vencidas", vencidas);
        model.addAttribute("tareas", tareas);
        return "trabajador/dashboard";
    }

    @PostMapping("/tareas/estado")
    public String actualizarEstado(@RequestParam Long tareaId, @RequestParam EstadoTarea estado) {
        tareaService.actualizarEstado(tareaId, estado);
        return "redirect:/trabajador/tareas";
    }

    @GetMapping("/tareas/ver/{id}")
    public String mostrarFormularioRespuesta(@PathVariable Long id, Model model, Principal principal) {
        Usuario trabajador = usuarioService.obtenerPorUsername(principal.getName()).orElseThrow();
        Tarea tarea = tareaService.obtenerPorId(id).orElseThrow();

        Optional<RespuestaTarea> respuesta = respuestaTareaService.obtenerPorTareaYTrabajador(tarea, trabajador);

        model.addAttribute("tarea", tarea);
        respuesta.ifPresent(r -> model.addAttribute("respuesta", r));
        return "trabajador/ver_tarea";
    }

    @PostMapping("/tareas/responder/{id}")
    public String enviarRespuesta(@PathVariable Long id,
                                  @RequestParam String descripcion,
                                  @RequestParam("archivo") MultipartFile archivo,
                                  Principal principal) throws IOException {

        respuestaTareaService.responderTarea(id, descripcion, archivo, principal.getName());

        // Cambiar el estado de la tarea a "Completada sin Check"
        tareaService.actualizarEstado(id, EstadoTarea.COMPLETADO_SIN_CHECK);

        // Redirigir a la lista de tareas del trabajador
        return "redirect:/trabajador/tareas";  // Redirigir a la vista de tareas del trabajador
    }
}

