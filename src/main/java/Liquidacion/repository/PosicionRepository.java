
package Liquidacion.repository;

import Liquidacion.entity.PosicionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosicionRepository extends JpaRepository<PosicionEntity, Long> {
     List<PosicionEntity> findByIsla_IdIsla(Long islaId);
}

