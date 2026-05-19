package Liquidacion.controller;

import Liquidacion.entity.EmpleadoEntity;
import Liquidacion.entity.EstacionEntity;
import Liquidacion.entity.VentasEntity;
import Liquidacion.utili.ReporteMensualExcel;
import Liquidacion.utili.ReporteTurnoExcel;
import Liquidacion.utili.ReporteTurnoFichaPDF;
import Liquidacion.entity.FilaReporte;
import Liquidacion.service.ReporteService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
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
public class ReporteMensualController {

    private final ReporteService reporteService;

    public ReporteMensualController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/Reportes/reportemensual")
    public String verReporte(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String fecha,
            Model model
    ) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        int idEstacion = reporteService.obtenerIdEstacionPorUsuario(username);

        Page<VentasEntity> pagina;

        // 🔥 FILTRO POR MES
        if (fecha != null && !fecha.isEmpty()) {
            pagina = reporteService.obtenerVentasPorMes(idEstacion, fecha, page, 10);
        } else {
            pagina = reporteService.obtenerVentasPaginadas(idEstacion, page, 10);
        }

        // 🔥 TOTAL DEL MES (solo lo que está en la página actual o todo si quieres luego)
        BigDecimal total = pagina.getContent()
                .stream()
                .map(v -> v.getTotal() != null ? v.getTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("datos", pagina.getContent());
        model.addAttribute("paginaActual", page);
        model.addAttribute("totalPaginas", pagina.getTotalPages());
        model.addAttribute("fecha", fecha);
        model.addAttribute("total", total);

        return "Reportes/reportemensual";
    }

    @GetMapping("/Reportes/excelMensual")
    public void exportarExcelMensual(
            @RequestParam(required = false) String fecha,
            HttpServletResponse response
    ) throws IOException, ParseException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        int idEstacion = reporteService.obtenerIdEstacionPorUsuario(username);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ReporteMensual.xlsx");

        List<VentasEntity> datos;

        if (fecha != null && !fecha.isEmpty()) {

            // 👉 formato viene como: 2026-03
            datos = reporteService.obtenerVentasPorMes(fecha, idEstacion);

        } else {
            // si no mandan fecha → mes actual
            datos = reporteService.obtenerVentasPorMesActual(idEstacion);
        }

        new ReporteMensualExcel().exportar(datos, fecha, response);
    }
}