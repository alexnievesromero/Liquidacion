package Liquidacion.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "turno")
public class TurnoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTurno;

    private LocalDate fecha;
    private String turno;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private Long numeroTurno;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_isla", nullable = false) // ✅ CORREGIDO
    private IslaEntity isla;

    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private EmpleadoEntity empleado;

    @ManyToOne
    @JoinColumn(name = "id_estacion", nullable = false)
    private EstacionEntity estacion;

}
