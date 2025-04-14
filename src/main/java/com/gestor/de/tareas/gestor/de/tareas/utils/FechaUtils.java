package com.gestor.de.tareas.gestor.de.tareas.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FechaUtils {

    public static long diasHastaVencimiento(LocalDate fechaVencimiento) {
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
    }

    public static boolean estaVencida(LocalDate fechaVencimiento) {
        return LocalDate.now().isAfter(fechaVencimiento);
    }
}