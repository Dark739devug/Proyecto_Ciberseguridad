package com.facturacion.backend.services;

import com.facturacion.backend.models.DetalleFactura;
import com.facturacion.backend.models.Factura;
import org.springframework.stereotype.Service;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class XmlService {

    /**
     * Genera XML de la factura según estándar FEL Guatemala
     */
    public String generarXmlFactura(Factura factura, List<DetalleFactura> detalles) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Elemento raíz
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("FacturaElectronica");
            rootElement.setAttribute("version", "1.0");
            doc.appendChild(rootElement);

            // Encabezado
            Element encabezado = doc.createElement("Encabezado");
            rootElement.appendChild(encabezado);

            addElement(doc, encabezado, "NumeroFactura", factura.getNumeroFactura());
            addElement(doc, encabezado, "Serie", factura.getSerie());
            addElement(doc, encabezado, "Correlativo", String.valueOf(factura.getCorrelativo()));
            addElement(doc, encabezado, "FechaEmision",
                    factura.getFechaEmision().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            addElement(doc, encabezado, "TipoDocumento", factura.getTipoDocumento().getCodigoTipo());

            // Emisor (Establecimiento)
            Element emisor = doc.createElement("Emisor");
            rootElement.appendChild(emisor);

            addElement(doc, emisor, "NIT", factura.getEstablecimiento().getNit());
            addElement(doc, emisor, "NombreComercial", factura.getEstablecimiento().getNombreComercial());
            addElement(doc, emisor, "RazonSocial", factura.getEstablecimiento().getRazonSocial());
            addElement(doc, emisor, "Direccion", factura.getEstablecimiento().getDireccion());
            addElement(doc, emisor, "Municipio", factura.getEstablecimiento().getMunicipio());
            addElement(doc, emisor, "Departamento", factura.getEstablecimiento().getDepartamento());
            addElement(doc, emisor, "CodigoPostal", factura.getEstablecimiento().getCodigoPostal());

            // Receptor (Cliente)
            Element receptor = doc.createElement("Receptor");
            rootElement.appendChild(receptor);

            addElement(doc, receptor, "NIT", factura.getCliente().getNit());
            addElement(doc, receptor, "RazonSocial", factura.getCliente().getRazonSocial());
            addElement(doc, receptor, "NombreComercial", factura.getCliente().getNombreComercial());
            addElement(doc, receptor, "Direccion", factura.getCliente().getDireccion());

            // Detalles
            Element detallesElement = doc.createElement("Detalles");
            rootElement.appendChild(detallesElement);

            for (DetalleFactura detalle : detalles) {
                Element item = doc.createElement("Item");
                detallesElement.appendChild(item);

                addElement(doc, item, "NumeroLinea", String.valueOf(detalle.getNumeroLinea()));
                addElement(doc, item, "Descripcion", detalle.getDescripcionProducto());
                addElement(doc, item, "Cantidad", detalle.getCantidad().toString());
                addElement(doc, item, "UnidadMedida", detalle.getUnidadMedida());
                addElement(doc, item, "PrecioUnitario", detalle.getPrecioUnitario().toString());
                addElement(doc, item, "Descuento", detalle.getDescuento().toString());
                addElement(doc, item, "SubtotalLinea", detalle.getSubtotalLinea().toString());
                addElement(doc, item, "AplicaIVA", String.valueOf(detalle.getAplicaIva()));
                addElement(doc, item, "MontoIVA", detalle.getMontoIva().toString());
                addElement(doc, item, "TotalLinea", detalle.getTotalLinea().toString());
            }

            // Totales
            Element totales = doc.createElement("Totales");
            rootElement.appendChild(totales);

            addElement(doc, totales, "Subtotal", factura.getSubtotal().toString());
            addElement(doc, totales, "TotalDescuentos", factura.getTotalDescuentos().toString());
            addElement(doc, totales, "SubtotalConDescuento", factura.getSubtotalConDescuento().toString());
            addElement(doc, totales, "TotalIVA", factura.getTotalIva().toString());
            addElement(doc, totales, "TotalFactura", factura.getTotalFactura().toString());

            // Convertir a String
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            return writer.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar XML: " + e.getMessage(), e);
        }
    }

    private void addElement(Document doc, Element parent, String tagName, String textContent) {
        if (textContent != null) {
            Element element = doc.createElement(tagName);
            element.setTextContent(textContent);
            parent.appendChild(element);
        }
    }
}
