package Liquidacion.controller;

import Liquidacion.repository.EmpleadoRepository;
import Liquidacion.repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession; // Importante para manejar la sesión

@Controller
@RequestMapping("/menuEmpleado")
public class MenuEmpleadoController {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private TurnoRepository turnoRepository;

    @GetMapping
    public String menuEmpleado(Model model,
            Authentication auth,
            HttpSession session,
            @RequestParam(value = "yaActivo", required = false) Boolean yaActivo,
            @RequestParam(value = "turnoId", required = false) Long turnoId) {

        String username = auth.getName();
        empleadoRepository.findByUsuario(username).ifPresent(empleado -> {
            session.setAttribute("empleado", empleado);

            turnoRepository.findByEmpleadoAndEstado(empleado, "ACTIVO")
                    .ifPresent(t -> {
                        model.addAttribute("turnoActivo", t);

                        // ✅ Aquí agregamos también la estación
                        if (t.getEstacion() != null) {
                            model.addAttribute("estacion", t.getEstacion());
                        } else {
                            model.addAttribute("estacion", "Sin estación asignada");
                        }
                    });
        });

        if (Boolean.TRUE.equals(yaActivo)) {
            model.addAttribute("mensaje", "Ya tienes un turno y sesión activos.");
        }

        if (turnoId != null) {
            model.addAttribute("turnoId", turnoId);
        }

        return "menuEmpleado";
    }

}
