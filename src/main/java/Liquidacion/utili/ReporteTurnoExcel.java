package Liquidacion.utili;

import Liquidacion.entity.FilaReporte;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ReporteTurnoExcel {
    // nuevo metodo para exportar asi como aparece en la base de datos.
    public void exportarCrudo(List<FilaReporte> datos, Date fechaFiltro, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte Crudo");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        List<FilaReporte> filtrados = datos.stream()
                .filter(r -> sdf.format(r.getFecha()).equals(sdf.format(fechaFiltro)))
                .collect(Collectors.toList());

        if (filtrados.isEmpty()) {
            Row fila = sheet.createRow(0);
            fila.createCell(0).setCellValue("No hay datos para la fecha seleccionada");
            workbook.write(response.getOutputStream());
            workbook.close();
            return;
        }

        // ======= ENCABEZADOS =======
        String[] columnas = {
                "Empleado",
                "Estación",
                "Isla",
                "Número Turno",
                "Turno",
//                "Fecha",
                "Fecha Hora Inicio",
                "Fecha Hora Fin",
//                "Lectura Inicial",
//                "Lectura Final",
//                "Gls",
//                "Pesos",
                "Venta Total",
                "Entregas",
                "Crédito Satélite",
                "Crédito Local",
                "Prepago Local",
                "Pagos Electrónicos",
                "Pagos QR",
                "Monedas",
                "Gastos",
                "Puntos Terpel",
                "Devoluciones",
                "Otros",
                "Cheques",
                "Regalos",
                "Préstamos",
                "Descuadres",
                "Efectivo Final",
                "Total",
                "Diferencia",
                "Observaciones"
        };

        Row header = sheet.createRow(0);
        for (int i = 0; i < columnas.length; i++) {
            header.createCell(i).setCellValue(columnas[i]);
        }
// ======= LLENAR DATOS CRUDOS =======
        int rowIndex = 1;

// 🚫 Evitar filas duplicadas por número de turno
        Set<Integer> turnosProcesados = new HashSet<>();

        for (FilaReporte r : filtrados) {

            // Si ya se escribió este turno → saltar
            if (turnosProcesados.contains(r.getNumeroTurno())) {
                continue;
            }

            // Marcar el turno como procesado
            turnosProcesados.add(r.getNumeroTurno());

            Row row = sheet.createRow(rowIndex++);
            int col = 0;

            row.createCell(col++).setCellValue(r.getNombreEmpleado());
            row.createCell(col++).setCellValue(r.getNombreEstacion());
            row.createCell(col++).setCellValue(r.getIsla());
            row.createCell(col++).setCellValue(r.getNumeroTurno());
            row.createCell(col++).setCellValue(r.getTurno());

            row.createCell(col++).setCellValue(
                    r.getFechaInicio() != null ? r.getFechaInicio().toString() : ""
            );
            row.createCell(col++).setCellValue(
                    r.getFechaFin() != null ? r.getFechaFin().toString() : ""
            );

            row.createCell(col++).setCellValue(
                    r.getVentaTotal() != null ? r.getVentaTotal().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getEntregas() != null ? r.getEntregas().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getCreditoSatelite() != null ? r.getCreditoSatelite().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getCreditoLocal() != null ? r.getCreditoLocal().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getPrepagoLocal() != null ? r.getPrepagoLocal().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getPagosElectronicos() != null ? r.getPagosElectronicos().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getPagosQr() != null ? r.getPagosQr().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getMonedas() != null ? r.getMonedas().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getGastos() != null ? r.getGastos().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getPuntosTerpel() != null ? r.getPuntosTerpel().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getDevoluciones() != null ? r.getDevoluciones().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getOtros() != null ? r.getOtros().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getCheques() != null ? r.getCheques().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getRegalos() != null ? r.getRegalos().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getPrestamos() != null ? r.getPrestamos().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getDescuadre() != null ? r.getDescuadre().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getEfectivoFinal() != null ? r.getEfectivoFinal().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getTotal() != null ? r.getTotal().doubleValue() : 0
            );
            row.createCell(col++).setCellValue(
                    r.getDiferencia() != null ? r.getDiferencia().doubleValue() : 0
            );

            row.createCell(col++).setCellValue(r.getObservaciones());



        }

        // Ajuste automático de ancho
        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }



}

//    public void exportar(List<FilaReporte> datos, Date fechaFiltro, HttpServletResponse response) throws IOException {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Reporte Turnos");
//
//        // Formato de fecha
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//
//        // Filtrado por fecha
//        List<FilaReporte> datosFiltrados = datos.stream()
//                .filter(r -> sdf.format(r.getFecha()).equals(sdf.format(fechaFiltro)))
//                .collect(Collectors.toList());
//
//        if (datosFiltrados.isEmpty()) {
//            Row fila = sheet.createRow(0);
//            fila.createCell(0).setCellValue("No hay datos para la fecha seleccionada");
//        } else {
//
//            // ======= ESTILOS =======
//
//            // Estilo encabezado gris + negrilla + bordes
//            CellStyle headerStyle = workbook.createCellStyle();
//            Font headerFont = workbook.createFont();
//            headerFont.setBold(true);
//            headerStyle.setFont(headerFont);
//            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//            headerStyle.setBorderTop(BorderStyle.THIN);
//            headerStyle.setBorderBottom(BorderStyle.THIN);
//            headerStyle.setBorderLeft(BorderStyle.THIN);
//            headerStyle.setBorderRight(BorderStyle.THIN);
//
//            // Estilo estándar de celdas
//            CellStyle cellStyle = workbook.createCellStyle();
//            cellStyle.setBorderTop(BorderStyle.THIN);
//            cellStyle.setBorderBottom(BorderStyle.THIN);
//            cellStyle.setBorderLeft(BorderStyle.THIN);
//            cellStyle.setBorderRight(BorderStyle.THIN);
//
//            // Formato numérico ###,###.00
//            CellStyle numberStyle = workbook.createCellStyle();
//            numberStyle.cloneStyleFrom(cellStyle);
//            DataFormat format = workbook.createDataFormat();
//            numberStyle.setDataFormat(format.getFormat("#,##0.00"));
//
//            // ======= CABECERAS =======
//
//            Row filaFecha = sheet.createRow(0);
//            filaFecha.createCell(0).setCellValue("Fecha: " + sdf.format(fechaFiltro));
//
//            Row filaTurnos = sheet.createRow(1);
//            filaTurnos.createCell(0).setCellValue("Turnos / Vendedores");
//
//            // Turno - nombre (ordenados)
//            List<String> vendedores = datosFiltrados.stream()
//                    .map(r -> r.getTurno() + " - " + r.getNombreEmpleado())
//                    .distinct()
//                    .sorted()
//                    .collect(Collectors.toList());
//
//            // ======= ENCABEZADO PRINCIPAL =======
//            Row headerRow = sheet.createRow(2);
//            headerRow.createCell(0).setCellValue("CONCEPTO");
//            headerRow.getCell(0).setCellStyle(headerStyle);
//
//            int colIndex = 1;
//            for (String v : vendedores) {
//                Cell cell = headerRow.createCell(colIndex++);
//                cell.setCellValue(v);
//                cell.setCellStyle(headerStyle);
//            }
//
//            Cell cellResumen = headerRow.createCell(colIndex);
//            cellResumen.setCellValue("RESUMEN");
//            cellResumen.setCellStyle(headerStyle);
//
//            // ======= CONCEPTOS =======
//            String[] conceptos = {
//                    "Entregas", "Vales Crédito", "Pago QR o Nequi", "Pago con Tarjetas",
//                    "Monedas", "Préstamos", "Gastos", "Puntos TERPEL",
//                    "Devoluciones", "Bonos prepago", "Efectivo Líquido", "Total"
//            };
//
//            // ======= LLENADO =======
//            for (int i = 0; i < conceptos.length; i++) {
//                Row row = sheet.createRow(i + 3);
//
//                Cell conceptoCell = row.createCell(0);
//                conceptoCell.setCellValue(conceptos[i]);
//                conceptoCell.setCellStyle(cellStyle);
//
//                colIndex = 1;
//                double resumenFila = 0;
//
//                for (String v : vendedores) {
//                    String[] parts = v.split(" - ", 2);
//                    String turno = parts[0];
//                    String nombre = parts[1];
//
//                    double valor = calcularValor(datosFiltrados, nombre, turno, conceptos[i]);
//
//                    Cell cell = row.createCell(colIndex++);
//                    cell.setCellValue(valor);
//                    cell.setCellStyle(numberStyle);
//
//                    resumenFila += valor;
//                }
//
//                Cell cellSum = row.createCell(colIndex);
//                cellSum.setCellValue(resumenFila);
//                cellSum.setCellStyle(numberStyle);
//            }
//
//            // Ajustar tamaño de columnas
//            for (int i = 0; i <= vendedores.size() + 1; i++) {
//                sheet.autoSizeColumn(i);
//            }
//        }
//
//        // Exportar
//        workbook.write(response.getOutputStream());
//        workbook.close();
//    }
//
//    private double valor(BigDecimal b) {
//        return b != null ? b.doubleValue() : 0.0;
//    }
//
//    private double calcularValor(List<FilaReporte> datos, String nombre, String turno, String concepto) {
//        return datos.stream()
//                .filter(r -> r.getNombreEmpleado().equals(nombre) && r.getTurno().equals(turno))
//                .mapToDouble(r -> switch (concepto) {
//                    case "Entregas" -> valor(r.getEntregas());
//                    case "Vales Crédito" -> valor(r.getCreditoLocal());
//                    case "Pago QR o Nequi" -> valor(r.getPagosQr());
//                    case "Pago con Tarjetas" -> valor(r.getPagosElectronicos());
//                    case "Monedas" -> valor(r.getMonedas());
//                    case "Préstamos" -> valor(r.getPrestamos());
//                    case "Gastos" -> valor(r.getGastos());
//                    case "Puntos TERPEL" -> 0;
//                    case "Devoluciones" -> 0;
//                    case "Bonos prepago" -> valor(r.getPrepagoLocal());
//                    case "Efectivo Líquido" -> valor(r.getEfectivoFinal());
//                    case "Total" -> valor(r.getTotal());
//                    default -> 0;
//                })
//                .sum();
//    }













