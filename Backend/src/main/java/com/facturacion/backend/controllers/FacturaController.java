package com.facturacion.backend.controllers;

import com.facturacion.backend.dto.request.FacturaRequest;
import com.facturacion.backend.dto.response.FacturaResponse;
import com.facturacion.backend.mapper.FacturaMapper;
import com.facturacion.backend.models.DetalleFactura;
import com.facturacion.backend.models.Factura;
import com.facturacion.backend.services.FacturaService;
import com.facturacion.backend.services.PdfService;
import com.facturacion.backend.services.XmlService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/facturas")
@SecurityRequirement(name = "bearerAuth")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private FacturaMapper facturaMapper;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private XmlService xmlService;

    @PostMapping
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<?> crear(@RequestBody FacturaRequest request) {
        try {
            List<FacturaService.DetalleFacturaRequest> detalles = request.getDetalles().stream()
                    .map(item -> {
                        FacturaService.DetalleFacturaRequest detalle = new FacturaService.DetalleFacturaRequest();
                        detalle.setIdProducto(item.getIdProducto());
                        detalle.setCantidad(item.getCantidad());
                        detalle.setDescuento(item.getDescuento());
                        return detalle;
                    })
                    .collect(Collectors.toList());

            Factura factura = facturaService.crearFactura(
                    request.getIdEstablecimiento(),
                    request.getIdCliente(),
                    request.getIdTipoDocumento(),
                    request.getIdUsuarioCreacion(),
                    detalles,
                    request.getObservaciones()
            );

            List<DetalleFactura> detallesFactura = facturaService.obtenerDetallesPorFactura(factura.getIdFactura());
            FacturaResponse response = facturaMapper.toResponseConDetalles(factura, detallesFactura);

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("mensaje", "Factura creada exitosamente");
            resultado.put("factura", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al crear factura: " + e.getMessage()));
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<FacturaResponse>> listarTodas() {
        List<Factura> facturas = facturaService.obtenerTodas();
        List<FacturaResponse> responses = facturas.stream()
                .map(f -> facturaMapper.toResponseConDetalles(f, facturaService.obtenerDetallesPorFactura(f.getIdFactura())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<FacturaResponse> obtenerPorId(@PathVariable Long id) {
        Factura factura = facturaService.obtenerPorId(id);
        List<DetalleFactura> detalles = facturaService.obtenerDetallesPorFactura(id);
        FacturaResponse response = facturaMapper.toResponseConDetalles(factura, detalles);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/numero/{numeroFactura}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<FacturaResponse> obtenerPorNumero(@PathVariable String numeroFactura) {
        Factura factura = facturaService.obtenerPorNumero(numeroFactura);
        return ResponseEntity.ok(facturaMapper.toResponse(factura));
    }

    @GetMapping("/estado/{idEstado}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<FacturaResponse>> listarPorEstado(@PathVariable String idEstado) {
        List<Factura> facturas = facturaService.obtenerPorEstado(idEstado);
        return ResponseEntity.ok(facturaMapper.toResponseList(facturas));
    }

    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<FacturaResponse>> listarPorCliente(@PathVariable Long idCliente) {
        List<Factura> facturas = facturaService.obtenerPorCliente(idCliente);
        return ResponseEntity.ok(facturaMapper.toResponseList(facturas));
    }

    @GetMapping("/rango-fechas")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<FacturaResponse>> listarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<Factura> facturas = facturaService.obtenerPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(facturaMapper.toResponseList(facturas));
    }

    @PostMapping("/{id}/certificar")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<?> certificar(@PathVariable Long id) {
        try {
            Factura factura = facturaService.enviarACertificar(id);
            List<DetalleFactura> detalles = facturaService.obtenerDetallesPorFactura(id);

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Factura enviada a certificar",
                    "factura", facturaMapper.toResponseConDetalles(factura, detalles)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/anular")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> anular(
            @PathVariable Long id,
            @RequestParam Long idUsuario,
            @RequestParam String motivo) {
        try {
            Factura factura = facturaService.anularFactura(id, idUsuario, motivo);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Factura anulada exitosamente",
                    "factura", facturaMapper.toResponse(factura)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Long id) {
        Factura factura = facturaService.obtenerPorId(id);
        List<DetalleFactura> detalles = facturaService.obtenerDetallesPorFactura(id);
        byte[] pdfBytes = pdfService.generarPdfFactura(factura, detalles);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("Factura_" + String.valueOf(factura.getCorrelativo()) + ".pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}/xml")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<String> descargarXml(@PathVariable Long id) {
        Factura factura = facturaService.obtenerPorId(id);
        List<DetalleFactura> detalles = facturaService.obtenerDetallesPorFactura(id);
        String xml = xmlService.generarXmlFactura(factura, detalles);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("Factura_" + String.valueOf(factura.getCorrelativo()) + ".xml")
                .build());

        return new ResponseEntity<>(xml, headers, HttpStatus.OK);
    }
}

