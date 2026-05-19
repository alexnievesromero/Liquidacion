package Liquidacion.utili;

import Liquidacion.entity.FilaReporte;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ReporteTurnoFichaPDF {

    private PdfPCell celdaValor(BigDecimal valor) {
        String textoFormateado = formatearMiles(valor);
        PdfPCell celda = new PdfPCell(new Phrase(textoFormateado));
        celda.setPadding(3);
        return celda;
    }

    private String formatearMiles(java.math.BigDecimal valor) {
        if (valor == null) {
            return "0";
        }
        NumberFormat format = NumberFormat.getNumberInstance(new Locale("es", "CO"));
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(2);
        return format.format(valor);
    }
    // metodo para exportar el cierre de turno. 

    public void exportar(List<FilaReporte> datos, HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_turnos_detallado.pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

//        try {
//            Image logo = Image.getInstance("src/main/resources/static/GN_Pequeño.png"); // o tu ruta relativa
//            logo.scaleToFit(100, 118); // Tamaño deseado (ancho, alto)
//            logo.setAlignment(Image.ALIGN_LEFT); // Puedes usar ALIGN_CENTER o ALIGN_RIGHT
//            document.add(logo);
//        } catch (DocumentException | IOException e) {
//        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (FilaReporte r : datos) {
            // Encabezado institucional
            Paragraph empresa = new Paragraph("Reporte Generado por sistema GESNNOVA\n\n",
                    new Font(Font.HELVETICA, 10, Font.BOLD));
            empresa.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(empresa);

            // Título
            Paragraph titulo = new Paragraph("REPORTE DE TURNO", new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE));
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            titulo.setSpacingBefore(6);

            PdfPCell cellTitulo = new PdfPCell();
            cellTitulo.setPhrase(titulo);
            cellTitulo.setBackgroundColor(new Color(56, 108, 180));
            cellTitulo.setPadding(8);
            cellTitulo.setColspan(2);
            cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPTable encabezado = new PdfPTable(2);
            encabezado.setWidthPercentage(100);
            encabezado.addCell(cellTitulo);

            // Datos básicos
            encabezado.addCell(getCell("Empleado:", Font.BOLD));
            encabezado.addCell(getCell(r.getNombreEmpleado(), Font.NORMAL));

            encabezado.addCell(getCell("Turno:", Font.BOLD));
            encabezado.addCell(getCell(r.getTurno(), Font.NORMAL));

            encabezado.addCell(getCell("Fecha inicio:", Font.BOLD));
            String fechaInicio = r.getFechaInicio() != null ? r.getFechaInicio().format(formatter) : "—";
            encabezado.addCell(getCell(fechaInicio, Font.NORMAL));

            encabezado.addCell(getCell("Fecha Fin:", Font.BOLD));
            String fechaFin = r.getFechaFin() != null ? r.getFechaFin().format(formatter) : "—";
            encabezado.addCell(getCell(fechaFin, Font.NORMAL));

            encabezado.addCell(getCell("N° Turno:", Font.BOLD));
            encabezado.addCell(getCell(String.valueOf(r.getNumeroTurno()), Font.NORMAL));

            encabezado.addCell(getCell("Estacion", Font.BOLD));
            encabezado.addCell(getCell(r.getNombreEstacion(), Font.NORMAL));

            document.add(encabezado);
            document.add(new Paragraph("\n"));

            // Tabla de datos y observaciones
            PdfPTable tabla = new PdfPTable(new float[]{1.5f, 1.5f});
            tabla.setWidthPercentage(100);

            // Parte izquierda: valores numéricos
            PdfPTable izquierda = new PdfPTable(2);
            izquierda.setWidthPercentage(100);
            izquierda.addCell(celdaLabel("Venta Total:"));
            izquierda.addCell(celdaValor(r.getVentaTotal()));

            izquierda.addCell(celdaLabel("Entregas:"));
            izquierda.addCell(celdaValor(r.getEntregas()));

            izquierda.addCell(celdaLabel("Crédito Satélite:"));
            izquierda.addCell(celdaValor(r.getCreditoSatelite()));

            izquierda.addCell(celdaLabel("Crédito Local:"));
            izquierda.addCell(celdaValor(r.getCreditoLocal()));

            izquierda.addCell(celdaLabel("Prepago Local:"));
            izquierda.addCell(celdaValor(r.getPrepagoLocal()));

            izquierda.addCell(celdaLabel("Pagos Electrónicos:"));
            izquierda.addCell(celdaValor(r.getPagosElectronicos()));

            izquierda.addCell(celdaLabel("Monedas:"));
            izquierda.addCell(celdaValor(r.getMonedas()));

            izquierda.addCell(celdaLabel("Gastos:"));
            izquierda.addCell(celdaValor(r.getGastos()));

            izquierda.addCell(celdaLabel("Puntos terpel"));
            izquierda.addCell(celdaValor(r.getPuntosTerpel()));

            izquierda.addCell(celdaLabel("Otros:"));
            izquierda.addCell(celdaValor(r.getOtros()));
            izquierda.addCell(celdaLabel("Cheques:"));
            izquierda.addCell(celdaValor(r.getCheques()));
            izquierda.addCell(celdaLabel("Regalos:"));
            izquierda.addCell(celdaValor(r.getRegalos()));
            izquierda.addCell(celdaLabel("Prestamos:"));
            izquierda.addCell(celdaValor(r.getPrestamos()));
            izquierda.addCell(celdaLabel("Descuadres:"));
            izquierda.addCell(celdaValor(r.getDescuadre()));
            izquierda.addCell(celdaLabel("Efectivo Final:"));
            izquierda.addCell(celdaValor(r.getEfectivoFinal()));
            izquierda.addCell(celdaLabel("Total:"));
            izquierda.addCell(celdaValor(r.getTotal()));
            izquierda.addCell(celdaLabel("Diferencia:"));
            izquierda.addCell(celdaValor(r.getDiferencia()));

            // Parte derecha: Observaciones
            PdfPCell obsTitulo = new PdfPCell(new Phrase("Observaciones:", new Font(Font.HELVETICA, 11, Font.BOLD)));
            obsTitulo.setHorizontalAlignment(Element.ALIGN_LEFT);
            obsTitulo.setPadding(5);
            obsTitulo.setBorder(Rectangle.NO_BORDER);

            PdfPCell obsContenido = new PdfPCell(new Phrase(r.getObservaciones() != null ? r.getObservaciones() : "Ninguna"));
            obsContenido.setMinimumHeight(200);
            obsContenido.setPadding(5);
            obsContenido.setBorder(Rectangle.BOX);

            PdfPTable derecha = new PdfPTable(1);
            derecha.setWidthPercentage(100);
            derecha.addCell(obsTitulo);
            derecha.addCell(obsContenido);

            // Añadir las dos columnas a tabla principal
            tabla.addCell(izquierda);
            tabla.addCell(derecha);

            document.add(tabla);
            document.newPage();
        }

        document.close();
    }

    private PdfPCell getCell(String texto, int estilo) {
        Font font = new Font(Font.HELVETICA, 10, estilo);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        return cell;
    }

    private PdfPCell celdaLabel(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, new Font(Font.HELVETICA, 9, Font.BOLD)));
        cell.setPadding(3);
        return cell;
    }


}
