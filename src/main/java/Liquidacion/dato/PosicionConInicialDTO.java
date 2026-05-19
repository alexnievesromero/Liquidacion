
package Liquidacion.dato;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class PosicionConInicialDTO {
    private Long idPosicion;
    private String nombre;
    private String tipoCombustible;
    private BigDecimal lecturaInicial;
}
