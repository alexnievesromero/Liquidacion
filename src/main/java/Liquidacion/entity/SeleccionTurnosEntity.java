package Liquidacion.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "seleccionturnos")
public class SeleccionTurnosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idseleccionturnos;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "idestacion")
    private EstacionEntity estacion;

}
