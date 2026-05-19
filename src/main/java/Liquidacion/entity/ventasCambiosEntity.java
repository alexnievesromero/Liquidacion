package Liquidacion.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ventas_cambios")
public class ventasCambiosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCambio;

    private Long idVentas;

    private String campo;

    private String valorAnterior;

    private String valorNuevo;

    private String usuario;

    private LocalDateTime fecha = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_estacion", nullable = false)
    private EstacionEntity estacion;
}

