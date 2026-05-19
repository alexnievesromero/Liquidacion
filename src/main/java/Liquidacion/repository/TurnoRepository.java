package Liquidacion.repository;

import Liquidacion.entity.EmpleadoEntity;
import Liquidacion.entity.TurnoEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TurnoRepository extends JpaRepository<TurnoEntity, Long> {

    boolean existsByEmpleadoAndEstado(EmpleadoEntity empleado, String estado);

    Optional<TurnoEntity> findByEmpleadoAndEstado(EmpleadoEntity empleado, String estado);

    @Transactional(readOnly = true)
    @Query("SELECT MAX(t.numeroTurno) FROM TurnoEntity t")
    Long obtenerUltimoNumeroTurno();
    
    @Query("SELECT t FROM TurnoEntity t WHERE t.empleado = :empleado AND t.estado = 'ACTIVO'")
    List<TurnoEntity> findTurnosActivosByEmpleado(@Param("empleado") EmpleadoEntity empleado);

    boolean existsByEmpleadoAndFechaAndTurno(
            EmpleadoEntity empleado,
            LocalDate fecha,
            String turno
    );

}
