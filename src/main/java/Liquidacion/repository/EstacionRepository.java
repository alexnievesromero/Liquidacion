package Liquidacion.repository;

import Liquidacion.entity.EstacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EstacionRepository extends JpaRepository<EstacionEntity, Long> {

    // Ejemplo correcto si tienes campo "nombre" en la entidad
    List<EstacionEntity> findByNombre(String nombre);

}

