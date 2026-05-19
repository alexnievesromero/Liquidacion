package Liquidacion.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
@Table(name = "isla")
public class IslaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_isla")
    private Long idIsla;

    private String isla;

    @ManyToOne
    @JoinColumn(name = "id_estacion")
    private EstacionEntity estacion;

    @OneToMany(mappedBy = "isla", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PosicionEntity> posiciones;

}

