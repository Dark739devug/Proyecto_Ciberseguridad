package com.facturacion.backend.services;

import com.facturacion.backend.models.*;
import com.facturacion.backend.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CertificacionService {

    private final FacturaRepository facturaRepository;
    private final EstadoFacturaRepository estadoFacturaRepository;
    private final DetalleFacturaRepository detalleFacturaRepository;
    private final XmlService xmlService;
    private final RestTemplate restTemplate;

    @Value("${certificacion.url:http://localhost:3001/api}")
    private String urlCertificador;

    public CertificacionService(FacturaRepository facturaRepository,
                                EstadoFacturaRepository estadoFacturaRepository,
                                DetalleFacturaRepository detalleFacturaRepository,
                                XmlService xmlService,
                                RestTemplate restTemplate) {
        this.facturaRepository = facturaRepository;
        this.estadoFacturaRepository = estadoFacturaRepository;
        this.detalleFacturaRepository = detalleFacturaRepository;
        this.xmlService = xmlService;
        this.restTemplate = restTemplate;
    }

    /**
     * Certifica una factura enviándola al certificador FEL
     */
    @Transactional
    public Factura certificarFactura(Long idFactura) {
        // 1. Obtener la factura
        Factura factura = facturaRepository.findById(idFactura)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        // 2. Validar que esté en estado PENDIENTE
        if (!"PENDIENTE".equals(factura.getEstado().getCodigoEstado())) {
            throw new RuntimeException("Solo se pueden certificar facturas en estado PENDIENTE");
        }

        // 3. Obtener detalles
        List<DetalleFactura> detalles = detalleFacturaRepository.findByFactura_IdFactura(idFactura);

        // 4. Generar XML
        String xml = xmlService.generarXmlFactura(factura, detalles);

        // 5. Preparar request en el formato que espera tu compañero
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("nitEmisor", factura.getEstablecimiento().getNit());
            requestBody.put("xmlDTE", xml);
            requestBody.put("firmaElectronica", ""); // Opcional, vacío por ahora

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            // 6. Llamar al certificador
            ResponseEntity<Map> response = restTemplate.exchange(
                    urlCertificador + "/certificar",
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();

            if (response.getStatusCode() == HttpStatus.OK && responseBody != null) {
                // 7. Certificación exitosa
                Boolean exitoso = (Boolean) responseBody.get("exitoso");

                if (exitoso != null && exitoso) {
                    String uuid = (String) responseBody.get("uuid");
                    String numeroAutorizacion = (String) responseBody.get("numeroAutorizacion");
                    String xmlCertificado = (String) responseBody.get("xmlCertificado");

                    // Actualizar factura
                    factura.setUuidCertificacion(uuid);
                    factura.setNumeroAutorizacion(numeroAutorizacion);
                    factura.setXmlCertificado(xmlCertificado != null ? xmlCertificado : xml);
                    factura.setFechaCertificacion(LocalDateTime.now());

                    // Cambiar estado a CERTIFICADA
                    EstadoFactura estadoCertificada = estadoFacturaRepository
                            .findByCodigoEstado("CERTIFICADA")
                            .orElseThrow(() -> new RuntimeException("Estado CERTIFICADA no encontrado"));

                    factura.setEstado(estadoCertificada);

                    // Guardar
                    facturaRepository.save(factura);

                    return factura;
                } else {
                    throw new RuntimeException("El certificador retornó exitoso=false");
                }

            } else {
                throw new RuntimeException("Error en la respuesta del certificador");
            }

        } catch (Exception e) {
            // 8. Error en certificación
            EstadoFactura estadoError = estadoFacturaRepository
                    .findByCodigoEstado("ERROR")
                    .orElseThrow(() -> new RuntimeException("Estado ERROR no encontrado"));

            factura.setEstado(estadoError);
            facturaRepository.save(factura);

            throw new RuntimeException("Error al certificar factura: " + e.getMessage(), e);
        }
    }
}