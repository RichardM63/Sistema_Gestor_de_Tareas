package com.gestor.de.tareas.gestor.de.tareas.enums;

public enum EstadoTarea {
    PENDIENTE,
    COMPLETADO_SIN_CHECK,
    COMPLETADO_CON_CHECK,
    VENCIDO,
    RECHAZADO;

    public String getCssClass() {
        switch (this) {
            case PENDIENTE:
                return "badge-pendiente";
            case COMPLETADO_SIN_CHECK:
                return "badge-completado_no_check";
            case COMPLETADO_CON_CHECK:
                return "badge-completado";
            case VENCIDO:
                return "badge-vencido";
            case RECHAZADO:
                return "badge-rechazado";
            default:
                return "";
        }
    }
}

