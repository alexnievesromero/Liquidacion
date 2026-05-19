package Liquidacion.repository;

import Liquidacion.entity.LecturaSurtidorEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author JEFE CIBERSEGURIDA
 */
public interface LecturaSurtidorRepository extends JpaRepository<LecturaSurtidorEntity, Long> {

    // Buscar lecturas por el id de la isla
    List<LecturaSurtidorEntity> findByIsla_IdIsla(Long islaId);

    LecturaSurtidorEntity findTopByIsla_IdIslaAndPosicion_IdPosicionAndTipoCombustibleOrderByIdLecturaDesc(
            Long islaId,
            Long idPosicion,
            String tipoCombustible
    );




}
