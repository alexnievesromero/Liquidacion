package Liquidacion.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Entity
@Table(name = "lectura_surtidor")
public class LecturaSurtidorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLectura;

    private String tipoCombustible;
    private BigDecimal lecturaInicial;
    private BigDecimal lecturaFinal;
    private BigDecimal gls;
    private BigDecimal pesos;

    @ManyToOne
    @JoinColumn(name = "id_turno")
    private TurnoEntity turno;

    @ManyToOne
    @JoinColumn(name = "id_isla")
    private IslaEntity isla;

    @ManyToOne
    @JoinColumn(name = "id_estacion", nullable = false)
    private EstacionEntity estacion;

    @ManyToOne
    @JoinColumn(name = "id_posicion", nullable = false)
    private  PosicionEntity posicion;
}
