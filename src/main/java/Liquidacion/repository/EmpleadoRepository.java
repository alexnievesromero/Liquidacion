
package Liquidacion.repository;

import Liquidacion.entity.EmpleadoEntity;

import java.util.List;
import java.util.Optional;

import Liquidacion.entity.EstacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Long> {
    Optional<EmpleadoEntity> findByUsuario(String usuario);
    // En EmpleadoRepository.java

        List<EmpleadoEntity> findByEstacion(EstacionEntity estacion);


}

