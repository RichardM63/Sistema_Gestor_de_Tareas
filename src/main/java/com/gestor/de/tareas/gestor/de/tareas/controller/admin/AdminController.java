package com.gestor.de.tareas.gestor.de.tareas.controller.admin;

import com.gestor.de.tareas.gestor.de.tareas.entity.Grupo;
import com.gestor.de.tareas.gestor.de.tareas.entity.Tarea;
import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;
import com.gestor.de.tareas.gestor.de.tareas.enums.EstadoTarea;
import com.gestor.de.tareas.gestor.de.tareas.enums.Rol;
import com.gestor.de.tareas.gestor.de.tareas.serviceImpl.GrupoServiceImpl;
import com.gestor.de.tareas.gestor.de.tareas.serviceImpl.TareaServiceImpl;
import com.gestor.de.tareas.gestor.de.tareas.serviceImpl.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UsuarioServiceImpl usuarioService;
    private final TareaServiceImpl tareaService;
    private final GrupoServiceImpl grupoService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pendientes", tareaService.contarPorEstado(EstadoTarea.PENDIENTE));
        model.addAttribute("completadas", tareaService.contarPorEstado(EstadoTarea.COMPLETADO_CON_CHECK));
        model.addAttribute("vencidas", tareaService.contarPorEstado(EstadoTarea.VENCIDO));
        return "admin/dashboard";
    }

    @GetMapping("/usuarios")
    public String listaUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        usuarios.sort(Comparator.comparing((Usuario u) -> u.getRol().name()).reversed());
        model.addAttribute("usuarios", usuarios);
        return "admin/usuarios";
    }

    @GetMapping("/usuarios/crear")
    public String mostrarFormularioCrearUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/crear_usuario";
    }

    @PostMapping("/usuarios/crear")
    public String crearUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.crearUsuario(usuario);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean eliminado = usuarioService.eliminarUsuario(id);

        if (!eliminado) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el usuario.");
        } else {
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente.");
        }

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerPorId(id);

        if (usuarioOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El usuario no existe.");
            return "redirect:/admin/usuarios";
        }

        model.addAttribute("usuario", usuarioOptional.get());
        return "admin/editar_usuario";
    }

    @PostMapping("/usuarios/editar/{id}")
    public String editarUsuario(Model model,@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes, @PathVariable Long id) {
        boolean actualizado = usuarioService.editarUsuario(usuario,id);

        if (!actualizado) {
            redirectAttributes.addFlashAttribute("error", "No se pudo actualizar el usuario.");
        } else {
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado correctamente.");
        }
        model.addAttribute("id",id);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/tareas")
    public String verTareas(Model model) {
        model.addAttribute("tareas", tareaService.listarTodas());
        model.addAttribute("usuarios", usuarioService.obtenerPorRol(Rol.TRABAJADOR));
        model.addAttribute("grupos", grupoService.listarTodos());
        return "admin/tareas";
    }

    @GetMapping("/tareas/crear")
    public String mostrarFormularioCrearTareas(Model model) {
        model.addAttribute("tarea", new Tarea());
        return "admin/crear_tarea";
    }

    @PostMapping("/tareas/crear")
    public String crearTarea(@ModelAttribute("tarea") Tarea tarea, Principal principal) {
        tareaService.crearTareaConCreador(tarea,principal.getName());
        return "redirect:/admin/tareas";
    }

    @GetMapping("/tareas/asignar/{id}")
    public String mostrarFormularioAsignar(@PathVariable Long id, Model model) {
        Tarea tarea = tareaService.obtenerPorId(id).orElseThrow();
        List<Grupo> grupos = grupoService.listarTodos();
        List<Usuario> trabajadores = usuarioService.obtenerPorRol(Rol.TRABAJADOR);

        model.addAttribute("tarea", tarea);
        model.addAttribute("grupos", grupos);
        model.addAttribute("trabajadores", trabajadores);
        return "admin/asignar_tarea";
    }

    @PostMapping("/tareas/asignar/{id}")
    public String asignarTarea(
            @PathVariable Long id,
            @RequestParam(required = false) Long grupoId,
            @RequestParam(required = false) Long trabajadorId
    ) {
        tareaService.asignarTarea(id, grupoId, trabajadorId);
        return "redirect:/admin/tareas";
    }

    @GetMapping("/grupos")
    public String verGrupos(Model model) {
        model.addAttribute("grupos", grupoService.listarTodos());
        model.addAttribute("usuarios", usuarioService.obtenerPorRol(Rol.TRABAJADOR));
        return "admin/grupos";
    }

    @GetMapping("/grupos/crear")
    public String mostrarFormularioCrearGrupo(Model model) {
        model.addAttribute("grupo", new Grupo());
        model.addAttribute("usuarios", usuarioService.obtenerPorRol(Rol.TRABAJADOR));
        return "admin/crear_grupo";
    }

    @PostMapping("/grupos/crear")
    public String crearGrupo(@ModelAttribute Grupo grupo, @RequestParam(required = false) List<Long> miembrosIds) {
        grupoService.crearGrupoConMiembros(grupo, miembrosIds);
        return "redirect:/admin/grupos";
    }

    @GetMapping("/grupos/editar/{id}")
    public String mostrarFormularioEditarGrupo(@PathVariable Long id, Model model) {
        Grupo grupo = grupoService.obtenerPorId(id).orElseThrow();
        List<Usuario> trabajadores = usuarioService.obtenerPorRol(Rol.TRABAJADOR);

        model.addAttribute("grupo", grupo);
        model.addAttribute("trabajadores", trabajadores);
        return "admin/editar_grupo";
    }

    @PostMapping("/grupos/editar/{id}")
    public String actualizarMiembrosGrupo(@PathVariable Long id, @RequestParam(required = false) List<Long> miembros) {
        grupoService.actualizarMiembros(id, miembros != null ? miembros : new ArrayList<>());
        return "redirect:/admin/grupos";
    }

    @GetMapping("/estadisticas")
    public String verEstadisticas(Model model) {
        model.addAttribute("estadisticasDia", tareaService.generarDatosPorDias(1));
        model.addAttribute("estadisticas10Dias", tareaService.generarDatosPorDias(10));
        model.addAttribute("estadisticas15Dias", tareaService.generarDatosPorDias(15));
        return "admin/estadisticas";
    }



}
