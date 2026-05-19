package Liquidacion.repository;


import Liquidacion.entity.ventasCambiosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ventasCambiosRepository extends JpaRepository<ventasCambiosEntity, Long> {

    List<ventasCambiosEntity> findByIdVentasOrderByFechaDesc(Long idVentas);
    List<ventasCambiosEntity> findAllByOrderByFechaDesc();
    List<ventasCambiosEntity>
    findByIdVentasAndEstacion_IdestacionOrderByFechaDesc(
            Long idVentas,
            Long idestacion
    );
}
