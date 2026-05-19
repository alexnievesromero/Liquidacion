package Liquidacion.security;

import Liquidacion.entity.EmpleadoEntity;
import Liquidacion.repository.EmpleadoRepository;
import Liquidacion.repository.TurnoRepository;
import Liquidacion.repository.SesionRepository;
import Liquidacion.service.TurnoSesionCierre;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final EmpleadoRepository empleadoRepo;
    private final TurnoSesionCierre turnoCierreService;

    public CustomLogoutSuccessHandler(EmpleadoRepository empleadoRepo,
                                      TurnoSesionCierre turnoCierreService) {
        this.empleadoRepo = empleadoRepo;
        this.turnoCierreService = turnoCierreService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {

        if (authentication != null) {
            String username = authentication.getName();
            empleadoRepo.findByUsuario(username)
                    .ifPresent(turnoCierreService::cerrarTurnoYSessionSiExiste);
        }

        request.getRequestDispatcher("/login?logout")
                .forward(request, response);
    }
}


