package Liquidacion.controller;

import Liquidacion.entity.EstacionEntity;
import Liquidacion.service.EstacionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//import javax.validation.Valid;


@Controller
@RequestMapping("/estaciones")
public class EstacionController {

    @Autowired
    private EstacionService estacionService;

    // Mostrar todas las estaciones
    @GetMapping
    public String listarEstaciones(Model model) {
        model.addAttribute("estaciones", estacionService.obtenerTodas());
        model.addAttribute("estacion", new EstacionEntity()); // Para el formulario vacío
        return "estaciones"; // ← Nombre de tu HTML
    }

    // Guardar una nueva estación o actualizar existente
    @PostMapping("/guardar")
    public String guardarEstacion(@Valid EstacionEntity estacion, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("estaciones", estacionService.obtenerTodas());
            return "estaciones";
        }
        estacionService.guardar(estacion);
        return "redirect:/estaciones";
    }

    // Cargar formulario de edición
    @GetMapping("/editar/{id}")
    public String editarEstacion(@PathVariable Long id, Model model) {
        EstacionEntity estacion = estacionService.obtenerPorId(id);
        model.addAttribute("estacion", estacion);
        model.addAttribute("estaciones", estacionService.obtenerTodas());
        return "estaciones";
    }

    // Eliminar estación
    @GetMapping("/eliminar/{id}")
    public String eliminarEstacion(@PathVariable Long id) {
        estacionService.eliminar(id);
        return "redirect:/estaciones";
    }
}
