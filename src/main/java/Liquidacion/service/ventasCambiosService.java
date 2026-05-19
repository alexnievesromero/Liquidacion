package Liquidacion.service;


import Liquidacion.entity.VentasEntity;

import Liquidacion.entity.ventasCambiosEntity;
import Liquidacion.repository.ventasCambiosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ventasCambiosService {

    private final ventasCambiosRepository cambioRepo;

    public void registrarCambios(VentasEntity antes, VentasEntity despues, String usuario) {

        comparar("venta_total", antes.getVenta_total(), despues.getVenta_total(), antes.getIdVentas(), usuario);
        comparar("entregas", antes.getEntregas(), despues.getEntregas(), antes.getIdVentas(), usuario);
        comparar("credito_satelite", antes.getCredito_satelite(), despues.getCredito_satelite(), antes.getIdVentas(), usuario);
        comparar("credito_local", antes.getCredito_local(), despues.getCredito_local(), antes.getIdVentas(), usuario);
        comparar("prepago_local", antes.getPrepago_local(), despues.getPrepago_local(), antes.getIdVentas(), usuario);
        comparar("pagos_electronicos", antes.getPagos_electronicos(), despues.getPagos_electronicos(), antes.getIdVentas(), usuario);
        comparar("pagos_Qr", antes.getPagos_Qr(), despues.getPagos_Qr(), antes.getIdVentas(), usuario);
        comparar("monedas", antes.getMonedas(), despues.getMonedas(), antes.getIdVentas(), usuario);
        comparar("gastos", antes.getGastos(), despues.getGastos(), antes.getIdVentas(), usuario);
        comparar("puntos_terpel", antes.getPuntos_terpel(), despues.getPuntos_terpel(), antes.getIdVentas(), usuario);
        comparar("devoluciones", antes.getDevoluciones(), despues.getDevoluciones(), antes.getIdVentas(), usuario);
        comparar("otros", antes.getOtros(), despues.getOtros(), antes.getIdVentas(), usuario);
        comparar("cheques", antes.getCheques(), despues.getCheques(), antes.getIdVentas(), usuario);
        comparar("regalos", antes.getRegalos(), despues.getRegalos(), antes.getIdVentas(), usuario);
        comparar("prestamos", antes.getPrestamos(), despues.getPrestamos(), antes.getIdVentas(), usuario);
        comparar("descuadres", antes.getDescuadres(), despues.getDescuadres(), antes.getIdVentas(), usuario);
        comparar("efectivo_final", antes.getEfectivo_final(), despues.getEfectivo_final(), antes.getIdVentas(), usuario);
        comparar("total", antes.getTotal(), despues.getTotal(), antes.getIdVentas(), usuario);
        comparar("diferencia", antes.getDiferencia(), despues.getDiferencia(), antes.getIdVentas(), usuario);
        comparar("observacion_admin", antes.getObservacion_admin(), despues.getObservacion_admin(), antes.getIdVentas(), usuario);
    }

    private void comparar(String campo, Object valorAntes, Object valorDespues, Long idVenta, String usuario) {
        if (valorAntes == null && valorDespues == null) return;

        String antes = valorAntes == null ? "" : valorAntes.toString();
        String despues = valorDespues == null ? "" : valorDespues.toString();

        if (!antes.equals(despues)) {
            ventasCambiosEntity cambio = new ventasCambiosEntity();
            cambio.setCampo(campo);
            cambio.setValorAnterior(antes);
            cambio.setValorNuevo(despues);
            cambio.setIdVentas(idVenta);
            cambio.setUsuario(usuario);

            cambioRepo.save(cambio);
        }
    }
}

