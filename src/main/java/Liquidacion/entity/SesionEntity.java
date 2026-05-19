package Liquidacion.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "sesion")
public class SesionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSesion;

    @ManyToOne
    @JoinColumn(name = "id_empleado")
    private EmpleadoEntity empleado;

    private LocalDateTime horaLogin;
    private LocalDateTime horaLogout;
    private String estado;
}
