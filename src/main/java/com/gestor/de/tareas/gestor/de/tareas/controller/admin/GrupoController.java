package com.gestor.de.tareas.gestor.de.tareas.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestor.de.tareas.gestor.de.tareas.entity.Grupo;
import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;
import com.gestor.de.tareas.gestor.de.tareas.enums.Rol;
import com.gestor.de.tareas.gestor.de.tareas.serviceImpl.GrupoServiceImpl;
import com.gestor.de.tareas.gestor.de.tareas.serviceImpl.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/grupos")
@RequiredArgsConstructor
public class GrupoController {


    private final UsuarioServiceImpl usuarioService;
    private final GrupoServiceImpl grupoService;

    @GetMapping("")
    public String verGrupos(Model model) {
        model.addAttribute("grupos", grupoService.listarTodos());
        model.addAttribute("usuarios", usuarioService.obtenerPorRol(Rol.TRABAJADOR));
        return "admin/grupos";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrearGrupo(Model model) {
        model.addAttribute("grupo", new Grupo());
        model.addAttribute("usuarios", usuarioService.obtenerPorRol(Rol.TRABAJADOR));
        return "admin/crear_grupo";
    }

    @PostMapping("/crear")
    public String crearGrupo(@ModelAttribute Grupo grupo, @RequestParam(required = false) List<Long> miembrosIds) {
        grupoService.crearGrupoConMiembros(grupo, miembrosIds);
        return "redirect:/admin/grupos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarGrupo(@PathVariable Long id, Model model) {
        Grupo grupo = grupoService.obtenerPorId(id).orElseThrow();
        List<Usuario> trabajadores = usuarioService.obtenerPorRol(Rol.TRABAJADOR);

        model.addAttribute("grupo", grupo);
        model.addAttribute("trabajadores", trabajadores);
        return "admin/editar_grupo";
    }

    @PostMapping("/editar/{id}")
    public String actualizarMiembrosGrupo(@PathVariable Long id, @RequestParam(required = false) List<Long> miembros) {
        grupoService.actualizarMiembros(id, miembros != null ? miembros : new ArrayList<>());
        return "redirect:/admin/grupos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id){
        grupoService.delete(id);
        return "redirect:/admin/grupos";
    }
}
