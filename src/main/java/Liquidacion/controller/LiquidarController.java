package Liquidacion.controller;

import Liquidacion.dato.PosicionConInicialDTO;
import Liquidacion.entity.*;
import Liquidacion.repository.*;
import Liquidacion.service.SeleccionTurnosService;
import Liquidacion.service.TurnoSesionCierre;
import Liquidacion.utili.ReporteTurnoFichaPDF;
import Liquidacion.service.IslaService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/liquidar")
public class LiquidarController {

    @Autowired
    private TurnoRepository turnoRepo;
    @Autowired
    private IslaRepository islaRepo;
    @Autowired
    private IslaService islaService;
    @Autowired
    private LecturaSurtidorRepository lecturaRepo;
    @Autowired
    private VentasRepository ventaRepo;
    @Autowired
    private PosicionRepository posicionRepo;
    @Autowired
    private TurnoSesionCierre turnoCierreService;
    @Autowired
    private ventasCambiosRepository cambiosRepo;
    @Autowired
    private SeleccionTurnosService seleccionTurnosService;



    @GetMapping
    public String mostrarFormularioLiquidar(Model model, HttpSession session) {
        EmpleadoEntity empleado = (EmpleadoEntity) session.getAttribute("empleado");
        if (empleado == null) {
            return "redirect:/menuEmpleado";
        }

        // Crear DTO inicializado
        LiquidacionDTO liquidacion = new LiquidacionDTO();
        liquidacion.setVenta(new VentasEntity());
        liquidacion.setLecturas(List.of(new LecturaSurtidorEntity()));

        // Obtener estación del empleado (si existe)
        Long idEstacion = empleado.getEstacion() != null ? empleado.getEstacion().getIdestacion() : null;

        List<SeleccionTurnosEntity> turnos = new ArrayList<>();

        if (idEstacion != null) {
            turnos = seleccionTurnosService.turnosPorEstacion(idEstacion);
        }

        model.addAttribute("turnos", turnos);

        model.addAttribute("empleado", empleado);

        // ✅ Si tiene estación, filtramos por ella, si no, mostramos todas
        if (idEstacion != null) {
            model.addAttribute("islas", islaService.listarTodosIdes(idEstacion));
        } else {
            model.addAttribute("islas", islaService.listarTodos());
        }

        model.addAttribute("liquidacion", liquidacion);
        model.addAttribute("turnos", turnos);
        model.addAttribute("fechaActual", LocalDate.now());
        model.addAttribute("horaActual", LocalTime.now().withSecond(0).withNano(0));
// generar token de seguridad
        String tokenLiquidacion = java.util.UUID.randomUUID().toString();

// guardarlo en sesión
        session.setAttribute("tokenLiquidacion", tokenLiquidacion);

// enviarlo al HTML
        model.addAttribute("tokenLiquidacion", tokenLiquidacion);


        return "liquidar";
    }

    @PostMapping("/guardar")
    @Transactional
    public String guardarLiquidacion(
            @ModelAttribute("liquidacion") LiquidacionDTO liquidacion,
            @RequestParam Long islaId,
            @RequestParam String turno,
            @RequestParam String tokenLiquidacion,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            HttpSession session,
            RedirectAttributes redirectAttributes) {


        EmpleadoEntity empleado = (EmpleadoEntity) session.getAttribute("empleado");
        if (empleado == null) {
            return "redirect:/menuEmpleado";
        }

        String tokenSession = (String) session.getAttribute("tokenLiquidacion");

        if (tokenSession == null || !tokenSession.equals(tokenLiquidacion)) {

            redirectAttributes.addFlashAttribute(
                    "mensajeError",
                    "⚠️ Esta liquidación ya fue enviada o la página está desactualizada."
            );

            return "redirect:/menuEmpleado";
        }

//        session.removeAttribute("tokenLiquidacion");
//        boolean yaLiquido = turnoRepo.existsByEmpleadoAndFechaAndTurno(
//                empleado,
//                fecha != null ? fecha : LocalDate.now(),
//                turno
//        );
//
//        if (yaLiquido) {
//
//            redirectAttributes.addFlashAttribute(
//                    "mensajeError",
//                    "⚠️ Ya existe una liquidación para este turno."
//            );
//
//            return "redirect:/menuEmpleado";
//        }
        if (islaId == null) {

            redirectAttributes.addFlashAttribute(
                    "mensajeError",
                    "⚠️ Debe seleccionar una isla."
            );

            return "redirect:/liquidar";
        }
        IslaEntity isla = islaRepo.findById(islaId)
                .orElseThrow(() -> new RuntimeException("Isla no encontrada"));

        EstacionEntity estacion = empleado.getEstacion();

        Long ultimo = turnoRepo.obtenerUltimoNumeroTurno();
        Long numeroTurno = (ultimo != null) ? ultimo + 1 : 1;

        TurnoEntity nuevoTurno = new TurnoEntity();
        nuevoTurno.setEmpleado(empleado);
        nuevoTurno.setIsla(isla);
        nuevoTurno.setEstacion(estacion);
        nuevoTurno.setTurno(turno);
        nuevoTurno.setNumeroTurno(numeroTurno);
        nuevoTurno.setFecha(fecha != null ? fecha : LocalDate.now());
        nuevoTurno.setHoraInicio(LocalDateTime.now());
        nuevoTurno.setEstado("ACTIVO");
        turnoRepo.save(nuevoTurno);

        for (LecturaSurtidorEntity lectura : liquidacion.getLecturas()) {
            if (lectura.getPesos() != null) {
                lectura.setTurno(nuevoTurno);
                lectura.setEstacion(estacion);
                lectura.setIsla(isla);
                lecturaRepo.save(lectura);
            }
        }

        VentasEntity venta = liquidacion.getVenta();
        if (venta != null && venta.getTotal() != null) {
            venta.setTurno(nuevoTurno);
            venta.setEstacion(estacion);
            ventaRepo.save(venta);
        }

        turnoCierreService.cerrarTurnoYSessionSiExiste(empleado);

        redirectAttributes.addFlashAttribute(
                "mensajeExito",
                "✅ Liquidación guardada y turno cerrado correctamente. Por favor cierre sesión."
        );
        redirectAttributes.addFlashAttribute(
                "turnoId", nuevoTurno.getIdTurno()
        );

        return "redirect:/menuEmpleado";
    }



    @PostMapping("/ventas/{id}/editar")
    @Transactional
    public String editarVenta(
            @PathVariable Long id,
            @ModelAttribute("venta") VentasEntity nuevos,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        VentasEntity original = ventaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        // Usuario que hizo el cambio
        String usuario = (principal != null) ? principal.getName() : "DESCONOCIDO";

        // Método auxiliar para registrar cambios
        BiConsumer<String, String[]> registrarCambio = (campo, valores) -> {
            if (!valores[0].equals(valores[1])) {
                ventasCambiosEntity c = new ventasCambiosEntity();
                c.setIdVentas(id);
                c.setEstacion(original.getEstacion()); // 👈 AQUÍ
                c.setCampo(campo);
                c.setValorAnterior(valores[0]);
                c.setValorNuevo(valores[1]);
                c.setUsuario(usuario);
                cambiosRepo.save(c);
            }
        };

        // Comparar cada campo editable
        registrarCambio.accept("venta_total", new String[]{
                String.valueOf(original.getVenta_total()),
                String.valueOf(nuevos.getVenta_total())
        });

        registrarCambio.accept("entregas", new String[]{
                String.valueOf(original.getEntregas()),
                String.valueOf(nuevos.getEntregas())
        });

        registrarCambio.accept("credito_satelite", new String[]{
                String.valueOf(original.getCredito_satelite()),
                String.valueOf(nuevos.getCredito_satelite())
        });

        registrarCambio.accept("credito_local", new String[]{
                String.valueOf(original.getCredito_local()),
                String.valueOf(nuevos.getCredito_local())
        });

        registrarCambio.accept("prepago_local", new String[]{
                String.valueOf(original.getPrepago_local()),
                String.valueOf(nuevos.getPrepago_local())
        });

        registrarCambio.accept("pagos_electronicos", new String[]{
                String.valueOf(original.getPagos_electronicos()),
                String.valueOf(nuevos.getPagos_electronicos())
        });

        registrarCambio.accept("pagos_Qr", new String[]{
                String.valueOf(original.getPagos_Qr()),
                String.valueOf(nuevos.getPagos_Qr())
        });

        registrarCambio.accept("monedas", new String[]{
                String.valueOf(original.getMonedas()),
                String.valueOf(nuevos.getMonedas())
        });

        registrarCambio.accept("gastos", new String[]{
                String.valueOf(original.getGastos()),
                String.valueOf(nuevos.getGastos())
        });

        registrarCambio.accept("puntos_terpel", new String[]{
                String.valueOf(original.getPuntos_terpel()),
                String.valueOf(nuevos.getPuntos_terpel())
        });

        registrarCambio.accept("devoluciones", new String[]{
                String.valueOf(original.getDevoluciones()),
                String.valueOf(nuevos.getDevoluciones())
        });

        registrarCambio.accept("otros", new String[]{
                String.valueOf(original.getOtros()),
                String.valueOf(nuevos.getOtros())
        });

        registrarCambio.accept("cheques", new String[]{
                String.valueOf(original.getCheques()),
                String.valueOf(nuevos.getCheques())
        });

        registrarCambio.accept("regalos", new String[]{
                String.valueOf(original.getRegalos()),
                String.valueOf(nuevos.getRegalos())
        });

        registrarCambio.accept("prestamos", new String[]{
                String.valueOf(original.getPrestamos()),
                String.valueOf(nuevos.getPrestamos())
        });

        registrarCambio.accept("descuadres", new String[]{
                String.valueOf(original.getDescuadres()),
                String.valueOf(nuevos.getDescuadres())
        });

        registrarCambio.accept("efectivo_final", new String[]{
                String.valueOf(original.getEfectivo_final()),
                String.valueOf(nuevos.getEfectivo_final())
        });

        registrarCambio.accept("total", new String[]{
                String.valueOf(original.getTotal()),
                String.valueOf(nuevos.getTotal())
        });

        registrarCambio.accept("diferencia", new String[]{
                String.valueOf(original.getDiferencia()),
                String.valueOf(nuevos.getDiferencia())
        });

        registrarCambio.accept("observacion_admin", new String[]{
                String.valueOf(original.getObservacion_admin()),
                String.valueOf(nuevos.getObservacion_admin())
        });

        // Actualizar la venta original
        original.setVenta_total(nuevos.getVenta_total());
        original.setEntregas(nuevos.getEntregas());
        original.setCredito_satelite(nuevos.getCredito_satelite());
        original.setCredito_local(nuevos.getCredito_local());
        original.setPrepago_local(nuevos.getPrepago_local());
        original.setPagos_electronicos(nuevos.getPagos_electronicos());
        original.setPagos_Qr(nuevos.getPagos_Qr());
        original.setMonedas(nuevos.getMonedas());
        original.setGastos(nuevos.getGastos());
        original.setPuntos_terpel(nuevos.getPuntos_terpel());
        original.setDevoluciones(nuevos.getDevoluciones());
        original.setOtros(nuevos.getOtros());
        original.setCheques(nuevos.getCheques());
        original.setRegalos(nuevos.getRegalos());
        original.setPrestamos(nuevos.getPrestamos());
        original.setDescuadres(nuevos.getDescuadres());
        original.setEfectivo_final(nuevos.getEfectivo_final());
        original.setTotal(nuevos.getTotal());
        original.setDiferencia(nuevos.getDiferencia());
        original.setObservacion_admin(nuevos.getObservacion_admin());

        ventaRepo.save(original);

        redirectAttributes.addFlashAttribute("mensaje", "✔ Cambios guardados y registrados en historial.");
        return "redirect:/liquidar/ventas/" + id + "/revisar";
    }



@PostMapping("/ventas/actualizarEstado")
@ResponseBody
@Transactional
public ResponseEntity<String> actualizarEstadoVenta(
        @RequestParam Long ventaId,
        @RequestParam String estado,
        @RequestParam(required = false) String observacionesAdmin) {

    try {
        VentasEntity venta = ventaRepo.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        // 🔒 BLOQUEO DEFINITIVO
        if ("aprobado".equalsIgnoreCase(venta.getEstado())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("⚠️ Esta venta ya fue aprobada y no puede modificarse.");
        }

        venta.setEstado(estado);
        venta.setObservacion_admin(observacionesAdmin);
        ventaRepo.save(venta);

        return ResponseEntity.ok("✅ Venta aprobada correctamente");

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("❌ Error al actualizar: " + e.getMessage());
    }
}



    @GetMapping(value = "/posicion/isla/{islaId}", produces = "application/json")
    @ResponseBody
    public List<PosicionConInicialDTO> obtenerPosicionesConLecturaInicial(@PathVariable Long islaId) {

        List<PosicionEntity> posiciones = posicionRepo.findByIsla_IdIsla(islaId);
        List<PosicionConInicialDTO> resultado = new ArrayList<>();

        for (PosicionEntity pos : posiciones) {

            PosicionConInicialDTO dto = new PosicionConInicialDTO();
            dto.setIdPosicion(pos.getIdPosicion());
            dto.setNombre(pos.getNombrePosicion());
            dto.setTipoCombustible(pos.getCombustible());

            // AHORA sí buscas la lectura correcta: por isla + posición + tipo de combustible
            LecturaSurtidorEntity ultima = lecturaRepo
                    .findTopByIsla_IdIslaAndPosicion_IdPosicionAndTipoCombustibleOrderByIdLecturaDesc(
                            islaId,
                            pos.getIdPosicion(),
                            pos.getCombustible()
                    );


            dto.setLecturaInicial(
                    (ultima != null && ultima.getLecturaFinal() != null)
                            ? ultima.getLecturaFinal()
                            : BigDecimal.ZERO
            );

            resultado.add(dto);
        }

        return resultado;
    }


    @GetMapping("/reporte")
    public void generarReporte(@RequestParam("turnoId") Long turnoId, HttpServletResponse response) throws IOException, DocumentException {
        TurnoEntity turno = turnoRepo.findById(turnoId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        VentasEntity venta = ventaRepo.findByTurno(turno)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        FilaReporte fila = new FilaReporte();
        fila.setNombreEmpleado(turno.getEmpleado().getNombre());
        fila.setTurno(turno.getTurno());
        fila.setFecha(java.sql.Date.valueOf(turno.getFecha()));
        fila.setNumeroTurno(turno.getNumeroTurno().intValue());
        fila.setNombreEstacion(turno.getEstacion().getNombre());
        fila.setIsla("Isla " + turno.getIsla().getIsla());
        fila.setFechaInicio(turno.getHoraInicio());
//        fila.setFechaFin(turno.getHoraFin());
        fila.setVentaTotal(venta.getVenta_total());
        fila.setEntregas(venta.getEntregas());
        fila.setCreditoSatelite(venta.getCredito_satelite());
        fila.setCreditoLocal(venta.getCredito_local());
        fila.setPrepagoLocal(venta.getPrepago_local());
        fila.setPagosElectronicos(venta.getPagos_electronicos());
        fila.setMonedas(venta.getMonedas());
        fila.setGastos(venta.getGastos());
        fila.setOtros(venta.getOtros());
        fila.setCheques(venta.getCheques());
        fila.setRegalos(venta.getRegalos());
        fila.setPrestamos(venta.getPrestamos());
        fila.setDescuadre(venta.getDescuadres());
        fila.setEfectivoFinal(venta.getEfectivo_final());
        fila.setTotal(venta.getTotal());
        fila.setDiferencia(venta.getDiferencia());
        fila.setObservaciones(venta.getObservaciones());

        List<FilaReporte> reporte = List.of(fila);
        new ReporteTurnoFichaPDF().exportar(reporte, response);
    }

    @Autowired
    private ventasCambiosRepository ventasCambiosRepo;

@GetMapping("/ventas/{id}/revisar")
public String revisarVenta(@PathVariable Long id, Model model) {

    VentasEntity venta = ventaRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

    EstacionEntity estacion = venta.getEstacion(); // ✅ AQUÍ

    if (estacion == null) {
        throw new RuntimeException("La venta no tiene estación asociada");
    }

    model.addAttribute("venta", venta);

    List<ventasCambiosEntity> historial =
            ventasCambiosRepo.findByIdVentasAndEstacion_IdestacionOrderByFechaDesc(
                    venta.getIdVentas(),
                    estacion.getIdestacion()
            );

    model.addAttribute("historial", historial);

    return "revisar_venta";
}


}
