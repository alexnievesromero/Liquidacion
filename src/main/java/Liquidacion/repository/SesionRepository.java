package Liquidacion.repository;

import Liquidacion.entity.EmpleadoEntity;
import Liquidacion.entity.SesionEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SesionRepository extends JpaRepository<SesionEntity, Long> {

    boolean existsByEmpleadoAndEstado(EmpleadoEntity empleado, String estado);
    Optional<SesionEntity> findByEmpleadoAndEstado(EmpleadoEntity empleado, String estado);


    @Modifying
    @Query("UPDATE SesionEntity s SET s.horaLogout = :horaLogout, s.estado = 'FINALIZADA' "
            + "WHERE s.empleado.idEmpleado = :idEmpleado AND s.estado = 'ACTIVA'")
    void cerrarSesionActiva(@Param("idEmpleado") Long idEmpleado,
            @Param("horaLogout") LocalDateTime horaLogout);

    SesionEntity findTopByEmpleadoAndEstadoOrderByHoraLoginDesc(
            EmpleadoEntity empleado,
            String estado
    );


}
