
package Liquidacion.dato;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ReporteVentasDTO {

    String getnombreEmpleado();

    String getIsla();

    String getTurno();

    BigDecimal getGls();

    BigDecimal getPesos();

    BigDecimal getVentaTotal();

    BigDecimal getEntregas();

    BigDecimal getCreditoSatelite();

    BigDecimal getCreditoLocal();

    BigDecimal getPrepagoLocal();

    BigDecimal getPagosElectronicos();

    BigDecimal getPagosQr();

    BigDecimal getMonedas();

    BigDecimal getGastos();

    BigDecimal getPuntosTerpel();

    BigDecimal getDevoluciones();

    BigDecimal getOtros();

    BigDecimal getCheques();

    BigDecimal getRegalos();

    BigDecimal getPrestamos();

    BigDecimal getDescuadres();

    BigDecimal geteFectivoFinal();

    BigDecimal getTotal();

    BigDecimal getDiferencia();

    String getObservaciones();
    
    LocalDateTime getFechaInicio();
    
    LocalDateTime getFechaFin();
}
