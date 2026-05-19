package Liquidacion.service;

import Liquidacion.entity.EmpleadoEntity;
import Liquidacion.repository.SesionRepository;
import Liquidacion.repository.TurnoRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TurnoSesionCierre {

    private final TurnoRepository turnoRepo;
    private final SesionRepository sesionRepo;

    public TurnoSesionCierre (TurnoRepository turnoRepo,
                              SesionRepository sesionRepo) {
        this.turnoRepo = turnoRepo;
        this.sesionRepo = sesionRepo;
    }

    @Transactional
    public void cerrarTurnoYSessionSiExiste(EmpleadoEntity empleado) {

        // 🔹 Cerrar sesión activa
        sesionRepo.findByEmpleadoAndEstado(empleado, "ACTIVO")
                .ifPresent(sesion -> {
                    sesion.setHoraLogout(LocalDateTime.now());
                    sesion.setEstado("INACTIVO");
                    sesionRepo.save(sesion);
                });

        // 🔹 Cerrar turno activo
        turnoRepo.findByEmpleadoAndEstado(empleado, "ACTIVO")
                .ifPresent(turno -> {
                    turno.setHoraFin(LocalDateTime.now());
                    turno.setEstado("CERRADO");
                    turnoRepo.save(turno);
                });
    }
}
