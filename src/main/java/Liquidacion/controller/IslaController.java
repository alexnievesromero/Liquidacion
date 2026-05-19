package Liquidacion.controller;

import Liquidacion.entity.EstacionEntity;
import Liquidacion.entity.IslaEntity;
import Liquidacion.service.IslaService;
import Liquidacion.service.EstacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class IslaController { // 👈 ¡Cambié el @RequestMapping! Ahora sin prefijo

    @Autowired
    private IslaService islaService;

    @Autowired
    private EstacionService estacionService;

    // ✅ Nueva ruta: /islas (no /isla/gestionar)
    @GetMapping("/islas")
    public String listarIslas(Model model) {
        model.addAttribute("isla", new IslaEntity()); // formulario vacío
        model.addAttribute("islas", islaService.listarTodos());
        model.addAttribute("estaciones", estacionService.obtenerTodas());
        return "islas"; // 👈 Vista separada
    }

    @PostMapping("/isla/guardar")
    public String guardar(@ModelAttribute IslaEntity isla) {
        if (isla.getEstacion() != null && isla.getEstacion().getIdestacion() != null) {
            EstacionEntity estacion = estacionService.obtenerPorId(isla.getEstacion().getIdestacion());
            isla.setEstacion(estacion);
        }
        islaService.guardar(isla); // asumo que tienes un método guardar en el service
        return "redirect:/islas"; // ✅ redirige a la lista de islas
    }

    @GetMapping("/isla/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        IslaEntity isla = islaService.buscarPorId(id);
        model.addAttribute("isla", isla);
        model.addAttribute("islas", islaService.listarTodos());
        model.addAttribute("estaciones", estacionService.obtenerTodas());
        return "islas"; // ✅ misma vista, con datos cargados
    }

    @GetMapping("/isla/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        islaService.eliminar(id);
        return "redirect:/islas"; // ✅
    }
}