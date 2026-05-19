package Liquidacion.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class FilaReporte {

    private Long idVentas;
    private String nombreEmpleado;
    private String nombreEstacion;
    private String isla;
    private int numeroTurno;
    private String turno;
    private Date fecha;
    private BigDecimal gls;
    private BigDecimal pesos;
    private BigDecimal ventaTotal;
    private BigDecimal entregas;
    private BigDecimal creditoSatelite;
    private BigDecimal creditoLocal;
    private BigDecimal prepagoLocal;
    private BigDecimal pagosElectronicos;
    private BigDecimal pagosQr;
    private BigDecimal monedas;
    private BigDecimal gastos;
    private BigDecimal puntosTerpel;
    private BigDecimal devoluciones;
    private BigDecimal otros;
    private BigDecimal cheques;
    private BigDecimal regalos;
    private BigDecimal prestamos;
    private BigDecimal descuadre;
    private BigDecimal efectivoFinal;
    private BigDecimal total;
    private BigDecimal diferencia;
    private String observaciones;
    private String estado;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private BigDecimal lecturaInicial;
    private BigDecimal lecturaFinal;



    public Long getIdVentas() {
        return idVentas;
    }

    public void setIdVentas(Long idVentas) {
        this.idVentas = idVentas;
    }


    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getNombreEstacion() {
        return nombreEstacion;
    }

    public void setNombreEstacion(String nombreEstacion) {
        this.nombreEstacion = nombreEstacion;
    }

    public String getIsla() {
        return isla;
    }

    public void setIsla(String isla) {
        this.isla = isla;
    }

    public int getNumeroTurno() {
        return numeroTurno;
    }

    public void setNumeroTurno(int numeroTurno) {
        this.numeroTurno = numeroTurno;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getGls() {
        return gls;
    }

    public void setGls(BigDecimal gls) {
        this.gls = gls;
    }

    public BigDecimal getPesos() {
        return pesos;
    }

    public void setPesos(BigDecimal pesos) {
        this.pesos = pesos;
    }

    public BigDecimal getVentaTotal() {
        return ventaTotal;
    }

    public void setVentaTotal(BigDecimal ventaTotal) {
        this.ventaTotal = ventaTotal;
    }

    public BigDecimal getEntregas() {
        return entregas;
    }

    public void setEntregas(BigDecimal entregas) {
        this.entregas = entregas;
    }

    public BigDecimal getCreditoSatelite() {
        return creditoSatelite;
    }

    public void setCreditoSatelite(BigDecimal creditoSatelite) {
        this.creditoSatelite = creditoSatelite;
    }

    public BigDecimal getCreditoLocal() {
        return creditoLocal;
    }

    public void setCreditoLocal(BigDecimal creditoLocal) {
        this.creditoLocal = creditoLocal;
    }

    public BigDecimal getPrepagoLocal() {
        return prepagoLocal;
    }

    public void setPrepagoLocal(BigDecimal prepagoLocal) {
        this.prepagoLocal = prepagoLocal;
    }

    public BigDecimal getPagosElectronicos() {
        return pagosElectronicos;
    }

    public void setPagosElectronicos(BigDecimal pagosElectronicos) {
        this.pagosElectronicos = pagosElectronicos;
    }

    public BigDecimal getPagosQr() {
        return pagosQr;
    }

    public void setPagosQr(BigDecimal pagosQr) {
        this.pagosQr = pagosQr;
    }

    public BigDecimal getMonedas() {
        return monedas;
    }

    public void setMonedas(BigDecimal monedas) {
        this.monedas = monedas;
    }

    public BigDecimal getGastos() {
        return gastos;
    }

    public void setGastos(BigDecimal gastos) {
        this.gastos = gastos;
    }

    public BigDecimal getPuntosTerpel() {
        return puntosTerpel;
    }

    public void setPuntosTerpel(BigDecimal puntosTerpel) {
        this.puntosTerpel = puntosTerpel;
    }

    public BigDecimal getDevoluciones() {
        return devoluciones;
    }

    public void setDevoluciones(BigDecimal devoluciones) {
        this.devoluciones = devoluciones;
    }

    public BigDecimal getOtros() {
        return otros;
    }

    public void setOtros(BigDecimal otros) {
        this.otros = otros;
    }

    public BigDecimal getCheques() {
        return cheques;
    }

    public void setCheques(BigDecimal cheques) {
        this.cheques = cheques;
    }

    public BigDecimal getRegalos() {
        return regalos;
    }

    public void setRegalos(BigDecimal regalos) {
        this.regalos = regalos;
    }

    public BigDecimal getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(BigDecimal prestamos) {
        this.prestamos = prestamos;
    }

    public BigDecimal getDescuadre() {
        return descuadre;
    }

    public void setDescuadre(BigDecimal descuadre) {
        this.descuadre = descuadre;
    }

    public BigDecimal getEfectivoFinal() {
        return efectivoFinal;
    }

    public void setEfectivoFinal(BigDecimal efectivoFinal) {
        this.efectivoFinal = efectivoFinal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getLecturaInicial() {
        return lecturaInicial;
    }

    public void setLecturaInicial(BigDecimal lecturaInicial) {
        this.lecturaInicial = lecturaInicial;
    }

    public BigDecimal getLecturaFinal() {
        return lecturaFinal;
    }

    public void setLecturaFinal(BigDecimal lecturaFinal) {
        this.lecturaFinal = lecturaFinal;
    }
}
