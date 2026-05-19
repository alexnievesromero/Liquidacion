package Liquidacion.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "posicion")
public class PosicionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_posicion")
    private Long idPosicion;

    private String nombrePosicion;
    private String combustible;

    @ManyToOne
    @JoinColumn(name = "id_isla")
    @JsonBackReference
    private IslaEntity isla;
    
    @ManyToOne
    @JoinColumn(name = "id_estacion")
    @JsonBackReference
    private EstacionEntity estacion;
}


