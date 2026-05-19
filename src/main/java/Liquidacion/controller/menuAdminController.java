package Liquidacion.controller;

import Liquidacion.entity.EmpleadoEntity;
import Liquidacion.security.EmpleadoUserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// menuAdminController.java
@Controller
public class menuAdminController {

    @GetMapping("/menuAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostraradmin(Model model, Authentication authentication) {
        // ✅ Recuperar el empleado completo
        EmpleadoUserDetails userDetails = (EmpleadoUserDetails) authentication.getPrincipal();
        EmpleadoEntity empleado = userDetails.getEmpleado();

        model.addAttribute("empleado", empleado);
        return "menuAdmin";
    }
}
