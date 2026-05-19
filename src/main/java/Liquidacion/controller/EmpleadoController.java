package Liquidacion.controller;

import Liquidacion.entity.EmpleadoEntity;
import Liquidacion.security.EmpleadoUserDetails;
import Liquidacion.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/empleado")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    // ✅ Modificado: ahora filtra por estación del usuario logueado
    @GetMapping
    public String vistaEmpleados(Model model, Authentication authentication) {
        // Obtener al empleado logueado (ej: admin o supervisor)
        EmpleadoEntity empleadoLogueado = ((EmpleadoUserDetails) authentication.getPrincipal()).getEmpleado();

        // Obtener solo los empleados de la misma estación
        List<EmpleadoEntity> empleados = empleadoService.findByEstacion(empleadoLogueado.getEstacion());

        model.addAttribute("empleados", empleados);
        return "listaempleados"; // 👈 nota: tu HTML se llama "listaempleado", ¿seguro?
    }

    @GetMapping("/nuevo")
    public String nuevoFormulario(Model model, Authentication authentication) {
        EmpleadoEntity empleado = new EmpleadoEntity();
        EmpleadoEntity admin = ((EmpleadoUserDetails) authentication.getPrincipal()).getEmpleado();
        empleado.setEstacion(admin.getEstacion()); // Asigna automáticamente la estación
        model.addAttribute("empleado", empleado);
        return "crearempleado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("empleado") EmpleadoEntity empleado,
                          BindingResult result,
                          Model model,
                          Authentication authentication) {

        if (result.hasErrors()) {
            EmpleadoEntity admin = ((EmpleadoUserDetails) authentication.getPrincipal()).getEmpleado();
            empleado.setEstacion(admin.getEstacion());
            model.addAttribute("empleado", empleado);
            return "crearempleado";
        }

        // ✅ La estación ya viene asignada en el formulario (o se podría forzar aquí también)
        empleadoService.guardar(empleado);
        return "redirect:/empleado";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, Authentication authentication) {
        EmpleadoEntity empleado = empleadoService.buscarPorId(id).orElseThrow();

        // 🔒 Opcional: verificar que el empleado editado pertenezca a la misma estación
        EmpleadoEntity logueado = ((EmpleadoUserDetails) authentication.getPrincipal()).getEmpleado();
        if (!empleado.getEstacion().getIdestacion().equals(logueado.getEstacion().getIdestacion())) {
            // O redirige a error, o lanza excepción, o simplemente no lo muestra
            return "redirect:/empleado";
        }

        model.addAttribute("empleado", empleado);
        return "crearempleado";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, Authentication authentication) {
        EmpleadoEntity empleado = empleadoService.buscarPorId(id).orElseThrow();
        EmpleadoEntity logueado = ((EmpleadoUserDetails) authentication.getPrincipal()).getEmpleado();

        // 🔒 Solo permite eliminar si es de la misma estación
        if (empleado.getEstacion().getIdestacion().equals(logueado.getEstacion().getIdestacion())) {
            empleadoService.eliminar(id);
        }
        return "redirect:/empleado";
    }
}
