package com.facturacion.backend.services;

import com.facturacion.backend.models.DetalleFactura;
import com.facturacion.backend.models.Factura;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService {

    /**
     * Genera un PDF de la factura
     */
    public byte[] generarPdfFactura(Factura factura, List<DetalleFactura> detalles) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Título
            document.add(new Paragraph("FACTURA ELECTRÓNICA")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\n"));

            // Información del establecimiento
            document.add(new Paragraph("ESTABLECIMIENTO")
                    .setFontSize(14)
                    .setBold());

            document.add(new Paragraph(factura.getEstablecimiento().getNombreComercial()));
            document.add(new Paragraph("NIT: " + factura.getEstablecimiento().getNit()));
            document.add(new Paragraph("Dirección: " + factura.getEstablecimiento().getDireccion()));
            document.add(new Paragraph("Teléfono: " + factura.getEstablecimiento().getTelefono()));

            document.add(new Paragraph("\n"));

            // Información de la factura
            document.add(new Paragraph("DATOS DE LA FACTURA")
                    .setFontSize(14)
                    .setBold());

            document.add(new Paragraph("Número: " + factura.getNumeroFactura()));
            document.add(new Paragraph("Serie: " + factura.getSerie()));
            document.add(new Paragraph("Fecha de emisión: " +
                    factura.getFechaEmision().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
            document.add(new Paragraph("Estado: " + factura.getEstado().getNombreEstado()));

            document.add(new Paragraph("\n"));

            // Información del cliente
            document.add(new Paragraph("CLIENTE")
                    .setFontSize(14)
                    .setBold());

            document.add(new Paragraph("Razón Social: " + factura.getCliente().getRazonSocial()));
            document.add(new Paragraph("NIT: " + factura.getCliente().getNit()));
            document.add(new Paragraph("Dirección: " + factura.getCliente().getDireccion()));

            document.add(new Paragraph("\n"));

            // Tabla de detalles
            document.add(new Paragraph("DETALLE DE PRODUCTOS")
                    .setFontSize(14)
                    .setBold());

            float[] columnWidths = {50, 200, 80, 80, 80, 80, 100};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Headers
            table.addHeaderCell("No.");
            table.addHeaderCell("Descripción");
            table.addHeaderCell("Cantidad");
            table.addHeaderCell("Precio Unit.");
            table.addHeaderCell("Descuento");
            table.addHeaderCell("IVA");
            table.addHeaderCell("Total");

            // Filas
            for (DetalleFactura detalle : detalles) {
                table.addCell(String.valueOf(detalle.getNumeroLinea()));
                table.addCell(detalle.getDescripcionProducto());
                table.addCell(detalle.getCantidad().toString());
                table.addCell("Q " + detalle.getPrecioUnitario().toString());
                table.addCell("Q " + detalle.getDescuento().toString());
                table.addCell("Q " + detalle.getMontoIva().toString());
                table.addCell("Q " + detalle.getTotalLinea().toString());
            }

            document.add(table);

            document.add(new Paragraph("\n"));

            // Totales
            document.add(new Paragraph("TOTALES")
                    .setFontSize(14)
                    .setBold());

            document.add(new Paragraph("Subtotal: Q " + factura.getSubtotal())
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("Descuentos: Q " + factura.getTotalDescuentos())
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("IVA (12%): Q " + factura.getTotalIva())
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("TOTAL: Q " + factura.getTotalFactura())
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT));

            // Observaciones
            if (factura.getObservaciones() != null && !factura.getObservaciones().isEmpty()) {
                document.add(new Paragraph("\n"));
                document.add(new Paragraph("OBSERVACIONES: " + factura.getObservaciones()));
            }

            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage(), e);
        }
    }
}