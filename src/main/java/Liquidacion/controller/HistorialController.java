package Liquidacion.controller;

import Liquidacion.entity.EmpleadoEntity;
import Liquidacion.entity.VentasEntity;
import Liquidacion.repository.VentasRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HistorialController {

    @Autowired
    private VentasRepository ventasRepository;

    @GetMapping("/historialpdf")
    public String mostrarHistorialTurnos(Model model, HttpSession session) {

        EmpleadoEntity empleado = (EmpleadoEntity) session.getAttribute("empleado");

        if (empleado == null) {
            return "redirect:/login";
        }

        List<VentasEntity> historial = ventasRepository
                .findByTurno_EmpleadoAndEstacion(
                        empleado,
                        empleado.getEstacion()
                );

        model.addAttribute("historial", historial);
        model.addAttribute("empleado", empleado);

        return "historialpdf";
    }
}
