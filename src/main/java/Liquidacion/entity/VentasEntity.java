package Liquidacion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Entity
@Table(name = "ventas")
public class VentasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Ventas")
    private Long idVentas;

    @Column(name = "venta_total")
    private BigDecimal venta_total;
    private BigDecimal entregas;
    @Column(name = "credito_satelite")
    private BigDecimal credito_satelite;
    @Column(name = "credito_local")
    private BigDecimal credito_local;
    @Column(name = "prepago_local")
    private BigDecimal prepago_local;
    @Column(name = "pagos_electronicos")
    private BigDecimal pagos_electronicos;
    @Column(name = "pagos_Qr")
    private BigDecimal pagos_Qr;
    private BigDecimal monedas;
    private BigDecimal gastos;
    @Column(name = "puntos_terpel")
    private BigDecimal puntos_terpel;
    private BigDecimal devoluciones;
    private BigDecimal otros;
    private BigDecimal cheques;
    private BigDecimal regalos;
    private BigDecimal prestamos;
    private BigDecimal descuadres;
    @Column(name = "efectivo_final")
    private BigDecimal efectivo_final;
    private BigDecimal total;
    private BigDecimal diferencia;

    private String observaciones;
    private String estado = "pendiente";
    @Column(name = "observacion_admin")
    private String observacion_admin;

    @ManyToOne
    @JoinColumn(name = "id_turno")
    private TurnoEntity turno;

    @ManyToOne
    @JoinColumn(name = "id_estacion", nullable = false)
    private EstacionEntity estacion;

    public void setTurno(TurnoEntity turno) {
        this.turno = turno;
    }

}
