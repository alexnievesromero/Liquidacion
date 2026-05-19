package Liquidacion.utili;

import Liquidacion.entity.VentasEntity;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class ReporteMensualExcel {

    public void exportar(List<VentasEntity> datos, String fecha, HttpServletResponse response) throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Reporte Mensual");

        int fila = 0;

        // 🔹 TITULO
        Row titulo = sheet.createRow(fila++);
        titulo.createCell(0).setCellValue("REPORTE MENSUAL - " + (fecha != null ? fecha : "ACTUAL"));

        fila++;

        // 🔹 ENCABEZADOS
        Row header = sheet.createRow(fila++);
        header.createCell(0).setCellValue("Empleado");
        header.createCell(1).setCellValue("Estación");
        header.createCell(2).setCellValue("Isla");
        header.createCell(3).setCellValue("Turno");
        header.createCell(4).setCellValue("Fecha");
        header.createCell(5).setCellValue("Total");

        // 🔹 DATOS
        BigDecimal totalGeneral = BigDecimal.ZERO;

        for (VentasEntity v : datos) {

            Row row = sheet.createRow(fila++);

            row.createCell(0).setCellValue(v.getTurno().getEmpleado().getNombre());
            row.createCell(1).setCellValue(v.getEstacion().getNombre());
            row.createCell(2).setCellValue(v.getTurno().getIsla().getIsla());
            row.createCell(3).setCellValue(v.getTurno().getTurno());
            row.createCell(4).setCellValue(v.getTurno().getFecha().toString());

            double valor = v.getTotal() != null ? v.getTotal().doubleValue() : 0;
            row.createCell(5).setCellValue(valor);

            totalGeneral = totalGeneral.add(v.getTotal() != null ? v.getTotal() : BigDecimal.ZERO);
        }

        // 🔹 TOTAL FINAL
        Row totalRow = sheet.createRow(fila + 1);
        totalRow.createCell(4).setCellValue("TOTAL:");
        totalRow.createCell(5).setCellValue(totalGeneral.doubleValue());

        wb.write(response.getOutputStream());
        wb.close();
    }
}
