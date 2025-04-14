package com.gestor.de.tareas.gestor.de.tareas.controller.admin;

import com.gestor.de.tareas.gestor.de.tareas.enums.EstadoTarea;
import com.gestor.de.tareas.gestor.de.tareas.serviceImpl.TareaServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final TareaServiceImpl tareaService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pendientes", tareaService.contarPorEstado(EstadoTarea.PENDIENTE));
        model.addAttribute("completadas", tareaService.contarPorEstado(EstadoTarea.COMPLETADO_CON_CHECK));
        model.addAttribute("vencidas", tareaService.contarPorEstado(EstadoTarea.VENCIDO));
        return "admin/dashboard";
    }
}
