package Liquidacion.controller;

import Liquidacion.dato.LoginDto;
import Liquidacion.entity.EmpleadoEntity;
import Liquidacion.service.EmpleadoService;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginForm(
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "error", required = false) String error,
            Model model) {

        // ✅ Siempre, siempre, siempre agrega loginDto
        if (!model.containsAttribute("loginDto")) {
            model.addAttribute("loginDto", new LoginDto());
        }

        if (logout != null) {
            model.addAttribute("logoutSuccess", "Sesión cerrada correctamente");
        }

        if (error != null) {
            // Aquí puedes personalizar el mensaje si quieres
            model.addAttribute("loginError", "error"); // o un código
        }

        return "login";
    }
    @Autowired
    private EmpleadoService empleadoService; // ← inyecta aquí
    // ✅ NUEVO: Procesar el login
    // AuthController.java

    @PostMapping("/login-process")
    public String procesarLogin(
            @RequestParam String usuario, // ← debe coincidir con name en el form
            @RequestParam String contrasena, // ← igual aquí
            HttpSession session,
            Model model) {

        Optional<EmpleadoEntity> resultado = empleadoService.login(usuario, contrasena);

        if (resultado.isPresent()) {
            // ✅ Login exitoso: guardar en sesión
            session.setAttribute("empleado", resultado.get());
            return "redirect:/menuAdmin";
        } else {
            // ❌ Error: volver al login con mensaje
            model.addAttribute("loginError", "Usuario o contraseña incorrectos");
            model.addAttribute("loginDto", new LoginDto());
            return "login";
        }
    }
}
