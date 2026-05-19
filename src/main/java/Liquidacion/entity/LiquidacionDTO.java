
package Liquidacion.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;



@Data
public class LiquidacionDTO {
    private List<LecturaSurtidorEntity> lecturas = new ArrayList<>();
    private VentasEntity venta;
}