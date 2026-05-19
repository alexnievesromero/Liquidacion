package Liquidacion.entity;

import jakarta.persistence.*;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Pattern;
//import javax.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "empleado")
public class EmpleadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado") // ✅ mapeo explícito
    private Long idEmpleado;

    @ManyToOne
    @JoinColumn(name = "id_estacion", nullable = false)
    private EstacionEntity estacion;

    @NotBlank(message = "{empleado.nombre.notblank}")
    @Size(min = 2, max = 100, message = "{empleado.nombre.size}")
    private String nombre;

    @NotBlank(message = "{empleado.usuario.notblank}")
    @Pattern(regexp = "[a-zA-Z0-9_]+", message = "{empleado.usuario.pattern}")
    private String usuario;

    @NotBlank(message = "{empleado.contrasena.notblank}")
    @Size(min = 6, message = "{empleado.contrasena.size}")
    private String contrasena;
    private String rol;

    private String identificacion;
    private String email;
    @Size(max = 20, message = "{empleado.telefono.size}")
    @Column(length = 20) // 👈 tamaño explícito y razonable
    private String telefono;

}
