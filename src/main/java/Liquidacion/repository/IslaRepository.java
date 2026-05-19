package Liquidacion.repository;

import Liquidacion.entity.IslaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IslaRepository extends JpaRepository<IslaEntity, Long> {

    List<IslaEntity> findByIsla(String isla); // buscar por nombre de isla

    List<IslaEntity> findByIdIsla(Long idIsla); // si buscas por id

     List<IslaEntity> findByEstacion_Idestacion(Long idestacion);
}
