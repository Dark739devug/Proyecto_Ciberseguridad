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
     * Genera XML de la factura según estándar FEL SAT/DTE de Guatemala
     */
    public String generarXmlFactura(Factura factura, List<DetalleFactura> detalles) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // --- 1. Elemento Raíz: <gte:GTDocumento> ---
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("gte:GTDocumento");
            rootElement.setAttribute("xmlns:gte", "http://www.sat.gob.gt/dte/fel/0.2.0");
            rootElement.setAttribute("Version", "0.1");
            doc.appendChild(rootElement);

            // --- 2. Elemento SAT: <gte:SAT> ---
            Element sat = doc.createElement("gte:SAT");
            sat.setAttribute("ClaseDocumento", "dte");
            rootElement.appendChild(sat);

            // --- 3. Elemento DTE: <gte:DTE> ---
            Element dte = doc.createElement("gte:DTE");
            dte.setAttribute("ID", "DatosCertificados");
            sat.appendChild(dte);

            // --- 4. Elemento DatosEmision: <gte:DatosEmision> ---
            Element datosEmision = doc.createElement("gte:DatosEmision");
            datosEmision.setAttribute("ID", "DatosEmision");
            dte.appendChild(datosEmision);

            // DATOS GENERALES
            Element datosGenerales = doc.createElement("gte:DatosGenerales");
            datosGenerales.setAttribute("CodigoMoneda", "GTQ");
            // Formato de fecha FEL: yyyy-MM-dd'T'HH:mm:ss'-06:00'
            String fechaHoraEmision = factura.getFechaEmision().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            datosGenerales.setAttribute("FechaHoraEmision", fechaHoraEmision + "-06:00");
            datosGenerales.setAttribute("Tipo", "FACT");
            datosEmision.appendChild(datosGenerales);

            // EMISOR
            Element emisor = doc.createElement("gte:Emisor");
            emisor.setAttribute("AfiliacionIVA", "GEN");
            emisor.setAttribute("CodigoEstablecimiento", factura.getEstablecimiento().getCodigoEstablecimiento());
            emisor.setAttribute("NITEmisor", factura.getEstablecimiento().getNit());
            emisor.setAttribute("NombreComercial", factura.getEstablecimiento().getNombreComercial());
            emisor.setAttribute("NombreEmisor", factura.getEstablecimiento().getRazonSocial());
            datosEmision.appendChild(emisor);

            // Dirección Emisor
            Element direccionEmisor = doc.createElement("gte:DireccionEmisor");
            addElement(doc, direccionEmisor, "gte:Direccion", factura.getEstablecimiento().getDireccion());
            addElement(doc, direccionEmisor, "gte:CodigoPostal", factura.getEstablecimiento().getCodigoPostal());
            addElement(doc, direccionEmisor, "gte:Municipio", factura.getEstablecimiento().getMunicipio());
            addElement(doc, direccionEmisor, "gte:Departamento", factura.getEstablecimiento().getDepartamento());
            addElement(doc, direccionEmisor, "gte:Pais", "GT");
            emisor.appendChild(direccionEmisor);

            // RECEPTOR
            Element receptor = doc.createElement("gte:Receptor");
            String nitCliente = factura.getCliente().getNit();
            receptor.setAttribute("IDReceptor", nitCliente.equals("CF") ? "CF" : nitCliente);
            receptor.setAttribute("NombreReceptor", factura.getCliente().getRazonSocial());
            datosEmision.appendChild(receptor);

            // Dirección Receptor
            Element direccionReceptor = doc.createElement("gte:DireccionReceptor");
            addElement(doc, direccionReceptor, "gte:Direccion", factura.getCliente().getDireccion());
            // CORRECCIÓN: Usamos un valor fijo para que compile
            addElement(doc, direccionReceptor, "gte:CodigoPostal", "01001");
            addElement(doc, direccionReceptor, "gte:Municipio", factura.getCliente().getMunicipio());
            addElement(doc, direccionReceptor, "gte:Departamento", factura.getCliente().getDepartamento());
            addElement(doc, direccionReceptor, "gte:Pais", "GT");
            receptor.appendChild(direccionReceptor);


            // ITEMS (Detalles)
            Element items = doc.createElement("gte:Items");
            datosEmision.appendChild(items);

            for (DetalleFactura detalle : detalles) {
                Element item = doc.createElement("gte:Item");
                item.setAttribute("BienOServicio", "B");
                item.setAttribute("NumeroLinea", String.valueOf(detalle.getNumeroLinea()));
                items.appendChild(item);

                addElement(doc, item, "gte:Cantidad", detalle.getCantidad().toString());
                addElement(doc, item, "gte:UnidadMedida", detalle.getUnidadMedida());
                addElement(doc, item, "gte:Descripcion", detalle.getDescripcionProducto());
                addElement(doc, item, "gte:PrecioUnitario", detalle.getPrecioUnitario().toString());

                addElement(doc, item, "gte:Precio", detalle.getPrecioUnitario().multiply(detalle.getCantidad()).toString());
                addElement(doc, item, "gte:Descuento", detalle.getDescuento().toString());

                // IMPUESTOS
                Element impuestos = doc.createElement("gte:Impuestos");
                item.appendChild(impuestos);
                if (detalle.getAplicaIva() && detalle.getMontoIva().doubleValue() > 0) {
                    Element impuesto = doc.createElement("gte:Impuesto");
                    addElement(doc, impuesto, "gte:NombreCorto", "IVA");
                    addElement(doc, impuesto, "gte:CodigoUnidadGravable", "1");
                    addElement(doc, impuesto, "gte:MontoGravable", detalle.getSubtotalLinea().toString());
                    addElement(doc, impuesto, "gte:MontoImpuesto", detalle.getMontoIva().toString());
                    impuestos.appendChild(impuesto);
                }

                addElement(doc, item, "gte:Total", detalle.getTotalLinea().toString());
            }

            // TOTALES
            Element totales = doc.createElement("gte:Totales");
            datosEmision.appendChild(totales);

            // Total Impuestos
            Element totalImpuestos = doc.createElement("gte:TotalImpuestos");
            Element totalImpuesto = doc.createElement("gte:TotalImpuesto");
            totalImpuesto.setAttribute("NombreCorto", "IVA");
            totalImpuesto.setAttribute("TotalMontoImpuesto", factura.getTotalIva().toString());
            totalImpuestos.appendChild(totalImpuesto);
            totales.appendChild(totalImpuestos);

            addElement(doc, totales, "gte:GranTotal", factura.getTotalFactura().toString());


            // Convertir a String
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            return writer.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar XML FEL: " + e.getMessage(), e);
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