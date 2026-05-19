package Liquidacion.controller;

import Liquidacion.entity.EstacionEntity;
import Liquidacion.entity.IslaEntity;
import Liquidacion.entity.PosicionEntity;
import Liquidacion.service.PosicionService;
import Liquidacion.service.IslaService;
import Liquidacion.service.EstacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PosicionController { // 👈 Sin @RequestMapping global

    @Autowired
    private PosicionService posicionService;

    @Autowired
    private IslaService islaService;

    @Autowired
    private EstacionService estacionService;

    // ✅ Nueva ruta: /posiciones
    @GetMapping("/posiciones")
    public String listarPosiciones(Model model) {
        model.addAttribute("posicion", new PosicionEntity());
        model.addAttribute("posiciones", posicionService.listarTodos());
        model.addAttribute("islas", islaService.listarTodos());
        model.addAttribute("estaciones", estacionService.obtenerTodas());
        return "posiciones"; // 👈 Vista separada
    }

    @PostMapping("/posicion/guardar")
    public String guardar(@ModelAttribute PosicionEntity posicion) {
        if (posicion.getEstacion() != null && posicion.getEstacion().getIdestacion() != null) {
            EstacionEntity estacion = estacionService.obtenerPorId(posicion.getEstacion().getIdestacion());
            posicion.setEstacion(estacion);
        }
        if (posicion.getIsla() != null && posicion.getIsla().getIdIsla() != null) {
            IslaEntity isla = islaService.buscarPorId(posicion.getIsla().getIdIsla());
            posicion.setIsla(isla);
        }
        posicionService.guardar(posicion);
        return "redirect:/posiciones"; // ✅
    }

    @GetMapping("/posicion/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("posicion", posicionService.buscarPorId(id));
        model.addAttribute("posiciones", posicionService.listarTodos());
        model.addAttribute("islas", islaService.listarTodos());
        model.addAttribute("estaciones", estacionService.obtenerTodas());
        return "posiciones"; // ✅
    }

    @GetMapping("/posicion/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        posicionService.eliminar(id);
        return "redirect:/posiciones"; // ✅
    }

    // Endpoint para API (ej: cargar posiciones por isla vía AJAX)
    @GetMapping("/posicion/isla/{idIsla}")
    @ResponseBody
    public List<PosicionDTO> getPosicionesPorIsla(@PathVariable Long idIsla) {
        return islaService.getPosicionesPorIsla(idIsla); // delega al service
    }

    public record PosicionDTO(String nombre, String tipoCombustible) {}
}
