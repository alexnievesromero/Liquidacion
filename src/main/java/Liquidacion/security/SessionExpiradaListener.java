package Liquidacion.security;

import Liquidacion.entity.EmpleadoEntity;
import Liquidacion.entity.SesionEntity;
import Liquidacion.repository.SesionRepository;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SessionExpiradaListener implements HttpSessionListener {

    private final SesionRepository sesionRepo;

    public SessionExpiradaListener(SesionRepository sesionRepo) {
        this.sesionRepo = sesionRepo;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

        Object empleadoObj = se.getSession().getAttribute("empleado");

        if (empleadoObj instanceof EmpleadoEntity empleado) {

            SesionEntity sesion = sesionRepo
                    .findTopByEmpleadoAndEstadoOrderByHoraLoginDesc(
                            empleado, "ACTIVO"
                    );

            if (sesion != null) {
                sesion.setEstado("EXPIRADA");
                sesion.setHoraLogout(LocalDateTime.now());
                sesionRepo.save(sesion);
            }
        }
    }
}
