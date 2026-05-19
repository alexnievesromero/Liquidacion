package Liquidacion.controller;

import Liquidacion.entity.EmpleadoEntity;
import Liquidacion.entity.EstacionEntity;
import Liquidacion.entity.VentasEntity;
import Liquidacion.utili.ReporteTurnoExcel;
import Liquidacion.utili.ReporteTurnoFichaPDF;
import Liquidacion.entity.FilaReporte;
import Liquidacion.service.ReporteService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/Reportes/reportesTurnos")
    public String verReporte(
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        int idEstacion = reporteService.obtenerIdEstacionPorUsuario(username);

        Page<VentasEntity> pagina =
                reporteService.obtenerVentasPaginadas(idEstacion, page, 10);

        model.addAttribute("datos", pagina.getContent());
        model.addAttribute("paginaActual", page);
        model.addAttribute("totalPaginas", pagina.getTotalPages());

        return "Reportes/reportesTurnos";
    }

    @GetMapping("/Reportes/Pdf")
    public void exportarPdfFiltrado(
            @RequestParam(value = "fecha", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(value = "numeroTurno", required = false) Integer numeroTurno,
            HttpServletResponse response
    ) throws IOException, DocumentException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=ReporteTurno.pdf");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        int idEstacion = reporteService.obtenerIdEstacionPorUsuario(username);

        // Validación: al menos uno de los dos filtros debe estar presente
        if (fecha == null && numeroTurno == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Debe proporcionar al menos un parámetro: fecha o numeroTurno.");
            return;
        }

        List<FilaReporte> datos = reporteService.obtenerPorFechaYTurno(fecha, numeroTurno, idEstacion);

        new ReporteTurnoFichaPDF().exportar(datos, response);
    }

    @GetMapping("/Reportes/excel")
    public void exportarExcelFiltrado(
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) String numeroTurno,
            HttpServletResponse response
    ) throws IOException, ParseException { // agregamos ParseException
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        int idEstacion = reporteService.obtenerIdEstacionPorUsuario(username);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ReporteTurnosFiltrados.xlsx");

        List<FilaReporte> datos;
        if (fecha != null && !fecha.isEmpty()) {
            // Convertir fecha String a java.util.Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Ajusta según el formato que envíes
            Date fechaFiltro = sdf.parse(fecha);

            datos = reporteService.obtenerFiltrado(fecha, numeroTurno, idEstacion);

            // ✅ Pasar la fecha al método exportar
            new ReporteTurnoExcel().exportarCrudo(datos, fechaFiltro, response);

        } else {
            // Si no hay fecha, usar hoy
            Date fechaFiltro = new Date();
            datos = reporteService.obtenerFiltrado(fecha, numeroTurno, idEstacion);
            new ReporteTurnoExcel().exportarCrudo(datos, fechaFiltro, response);
        }
    }
//    @GetMapping("/historial")
//    public String historialTurnos(
//            @RequestParam(defaultValue = "0") int page,
//            Model model,
//            HttpSession session
//    ) {
//        EmpleadoEntity empleado = (EmpleadoEntity) session.getAttribute("empleado");
//        EstacionEntity estacion = empleado.getEstacion();
//
//        Page<VentasEntity> ventasPage =
//                reporteService.listarPorEmpleadoYEstacion(
//                        empleado,
//                        estacion,
//                        page,
//                        10
//                );
//
//        model.addAttribute("historial", ventasPage.getContent());
//        model.addAttribute("paginaActual", page);
//        model.addAttribute("totalPaginas", ventasPage.getTotalPages());
//
//        return "historial";
//    }

}
