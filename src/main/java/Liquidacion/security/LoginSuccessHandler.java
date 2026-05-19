package Liquidacion.security;

import Liquidacion.entity.EmpleadoEntity;
import Liquidacion.entity.EstacionEntity;
import Liquidacion.entity.SesionEntity;
import Liquidacion.repository.EmpleadoRepository;
import Liquidacion.repository.EstacionRepository;
import Liquidacion.repository.IslaRepository;
import Liquidacion.repository.SesionRepository;
import Liquidacion.repository.TurnoRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final EmpleadoRepository empleadoRepo;
    private final SesionRepository sesionRepo;

    public LoginSuccessHandler(EmpleadoRepository empleadoRepo,
//            TurnoRepository turnoRepo,
            SesionRepository sesionRepo
//            IslaRepository islaRepo,
//            EstacionRepository estacionRepo
    ) {
        this.empleadoRepo = empleadoRepo;
        this.sesionRepo = sesionRepo;

    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        try {
            String username = authentication.getName();
            EmpleadoEntity empleado = empleadoRepo.findByUsuario(username).orElse(null);

            if (empleado == null) {
                response.sendRedirect("/login?msg=empleadoNoEncontrado");

                return;
            }

            // Validar que el empleado tenga una estación asignada
            EstacionEntity estacion = empleado.getEstacion();
            if (estacion == null) {
                response.sendRedirect("/login?msg=noperteneceempresa");
                return;
            }

            // Guardar la estación del empleado en sesión
            request.getSession(true).setAttribute("estacionId", estacion.getIdestacion());
            request.getSession().setAttribute("estacionNombre", estacion.getNombre());

            // Verificar si ya tiene una sesión activa
            boolean sesionActiva = sesionRepo.existsByEmpleadoAndEstado(empleado, "ACTIVO");
            if (!sesionActiva) {
                SesionEntity sesion = new SesionEntity();
                sesion.setEmpleado(empleado);
                sesion.setHoraLogin(LocalDateTime.now());
                sesion.setEstado("ACTIVO");
                sesionRepo.save(sesion);
            }

            // Redirigir según rol
            switch (empleado.getRol().toUpperCase()) {
                case "ADMIN" ->
                    response.sendRedirect("/menuAdmin");
                case "ADMINGESNNOVA" ->
                        response.sendRedirect("/islas");
                case "SUPERVISOR" ->
                    response.sendRedirect("/menuAdmin");
                case "EMPLEADO" ->
                    response.sendRedirect("/menuEmpleado");
                default ->
                    response.sendRedirect("/login?msg=rolDesconocido");
            }

        } catch (IOException ex) { // Para depuración
            // Para depuración
            if (!response.isCommitted()) {
                response.sendRedirect("/login?msg=errorInterno");
            }
        }
    }

}
