package Liquidacion.repository;

import Liquidacion.entity.EmpleadoEntity;
import Liquidacion.entity.EstacionEntity;
import Liquidacion.entity.TurnoEntity;
import Liquidacion.entity.VentasEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ALEXANDER NIEVES
 */
@Repository
public interface VentasRepository extends JpaRepository<VentasEntity, Long> {

    Optional<VentasEntity> findByTurno(TurnoEntity turno);

    List<VentasEntity> findByTurno_EmpleadoAndEstacion(
            EmpleadoEntity empleado,
            EstacionEntity estacion
    );
    // ✅ NUEVO: PAGINACIÓN
    Page<VentasEntity> findByTurno_EmpleadoAndEstacion(
            EmpleadoEntity empleado,
            EstacionEntity estacion,
            Pageable pageable
    );
    Page<VentasEntity> findByEstacion_Idestacion(int idEstacion, Pageable pageable);

    @Query("""
SELECT v FROM VentasEntity v
WHERE v.turno.estacion.id = :idEstacion
AND FUNCTION('DATE_FORMAT', v.turno.fecha, '%Y-%m') = :fecha
""")
    Page<VentasEntity> findByMesYEstacion(
            @Param("fecha") String fecha,
            @Param("idEstacion") int idEstacion,
            Pageable pageable
    );

    @Query("""
SELECT v FROM VentasEntity v
WHERE FUNCTION('DATE_FORMAT', v.turno.fecha, '%Y-%m') = :fecha
AND v.estacion.id = :idEstacion
""")
    List<VentasEntity> obtenerPorMes(@Param("fecha") String fecha,
                                     @Param("idEstacion") int idEstacion);

    // 🔹 MES ACTUAL (AUTOMÁTICO)
    @Query("""
    SELECT v FROM VentasEntity v
    WHERE FUNCTION('DATE_FORMAT', v.turno.fecha, '%Y-%m') =
          FUNCTION('DATE_FORMAT', CURRENT_DATE, '%Y-%m')
    AND v.estacion.id = :idEstacion
    ORDER BY v.turno.fecha DESC
    """)
    List<VentasEntity> obtenerMesActual(@Param("idEstacion") int idEstacion);
 }
