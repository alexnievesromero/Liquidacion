package Liquidacion.controller;

import Liquidacion.entity.EstacionEntity;
import Liquidacion.service.EstacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class InicioController {

    @Autowired
    private EstacionService estacionService;

    @GetMapping("/")
    public String mostrarInicio(Model model) {
        List<EstacionEntity> estaciones = estacionService.obtenerTodas();
        model.addAttribute("estaciones", estaciones);
        return "index"; // nombre de tu HTML Thymeleaf
    }
}
