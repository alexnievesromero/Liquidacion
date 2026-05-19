package Liquidacion.repository;

import Liquidacion.entity.SeleccionTurnosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeleccionTurnoRepository  extends JpaRepository<SeleccionTurnosEntity, Long> {

    List<SeleccionTurnosEntity> findByEstacion_Idestacion(Long idEstacion);
}
