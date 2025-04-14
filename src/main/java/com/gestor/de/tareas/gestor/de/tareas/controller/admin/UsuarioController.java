package com.gestor.de.tareas.gestor.de.tareas.controller.admin;

import com.gestor.de.tareas.gestor.de.tareas.entity.Usuario;
import com.gestor.de.tareas.gestor.de.tareas.serviceImpl.UsuarioServiceImpl;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
public class UsuarioController {


    private final UsuarioServiceImpl usuarioService;


    @GetMapping("")
    public String listaUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        usuarios.sort(Comparator.comparing((Usuario u) -> u.getRol().name()).reversed());
        model.addAttribute("usuarios", usuarios);
        return "admin/usuarios";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrearUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/crear_usuario";
    }

    @PostMapping("/crear")
    public String crearUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.crearUsuario(usuario);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean eliminado = usuarioService.eliminarUsuario(id);

        if (!eliminado) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el usuario.");
        } else {
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente.");
        }

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerPorId(id);

        if (usuarioOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El usuario no existe.");
            return "redirect:/admin/usuarios";
        }

        model.addAttribute("usuario", usuarioOptional.get());
        return "admin/editar_usuario";
    }

    @PostMapping("/editar/{id}")
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
}
