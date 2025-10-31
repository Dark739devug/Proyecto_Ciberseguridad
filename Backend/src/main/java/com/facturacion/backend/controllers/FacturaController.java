package com.facturacion.backend.controllers;

import com.facturacion.backend.dto.request.FacturaRequest;
import com.facturacion.backend.dto.response.FacturaResponse;
import com.facturacion.backend.mapper.FacturaMapper;
import com.facturacion.backend.models.Factura;
import com.facturacion.backend.repositories.FacturaRepository;
import com.facturacion.backend.services.FacturaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.facturacion.backend.models.DetalleFactura;
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
    private FacturaRepository facturaRepository;

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private FacturaMapper facturaMapper;

    /**
     * POST /api/facturas
     * Crea una factura completa con cálculo automático de totales
     */
    @PostMapping
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> crear(@RequestBody FacturaRequest request) {
        try {
            // Convertir DTOs a formato del Service
            List<FacturaService.DetalleFacturaRequest> detalles = request.getDetalles().stream()
                    .map(item -> {
                        FacturaService.DetalleFacturaRequest detalle = new FacturaService.DetalleFacturaRequest();
                        detalle.setIdProducto(item.getIdProducto());
                        detalle.setCantidad(item.getCantidad());
                        detalle.setDescuento(item.getDescuento());
                        return detalle;
                    })
                    .collect(Collectors.toList());

            // Crear factura usando el Service (calcula automáticamente)
            Factura factura = facturaService.crearFactura(
                    request.getIdEstablecimiento(),
                    request.getIdCliente(),
                    request.getIdTipoDocumento(),
                    request.getIdUsuarioCreacion(),
                    detalles,
                    request.getObservaciones()
            );

            // Obtener detalles y mapear CON detalles
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

    /**
     * GET /api/facturas
     * Lista todas las facturas CON detalles
     */
    @GetMapping
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<FacturaResponse>> listarTodas() {
        List<Factura> facturas = facturaRepository.findAll();

        // Mapear cada factura con sus detalles
        List<FacturaResponse> responses = facturas.stream()
                .map(factura -> {
                    List<DetalleFactura> detalles = facturaService.obtenerDetallesPorFactura(factura.getIdFactura());
                    return facturaMapper.toResponseConDetalles(factura, detalles);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/facturas/{id}
     * Obtiene una factura por ID CON detalles
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<FacturaResponse> obtenerPorId(@PathVariable Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Factura no encontrada"
                ));

        // Obtener detalles
        List<DetalleFactura> detalles = facturaService.obtenerDetallesPorFactura(id);

        // Mapear con detalles
        FacturaResponse response = facturaMapper.toResponseConDetalles(factura, detalles);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/facturas/numero/{numeroFactura}
     * Obtiene una factura por su número
     */
    @GetMapping("/numero/{numeroFactura}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<FacturaResponse> obtenerPorNumero(@PathVariable String numeroFactura) {
        Factura factura = facturaRepository.findByNumeroFactura(numeroFactura)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Factura no encontrada"
                ));
        return ResponseEntity.ok(facturaMapper.toResponse(factura));
    }

    /**
     * GET /api/facturas/estado/{idEstado}
     * Lista facturas por estado
     */
    @GetMapping("/estado/{idEstado}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<FacturaResponse>> listarPorEstado(@PathVariable Long idEstado) {
        List<Factura> facturas = facturaRepository.findByEstado_IdEstado(idEstado);
        return ResponseEntity.ok(facturaMapper.toResponseList(facturas));
    }

    /**
     * GET /api/facturas/cliente/{idCliente}
     * Lista facturas de un cliente
     */
    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<FacturaResponse>> listarPorCliente(@PathVariable Long idCliente) {
        List<Factura> facturas = facturaRepository.findByCliente_IdCliente(idCliente);
        return ResponseEntity.ok(facturaMapper.toResponseList(facturas));
    }

    /**
     * GET /api/facturas/rango-fechas
     * Lista facturas en un rango de fechas
     */
    @GetMapping("/rango-fechas")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<FacturaResponse>> listarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<Factura> facturas = facturaRepository.findByFechaEmisionBetween(fechaInicio, fechaFin);
        return ResponseEntity.ok(facturaMapper.toResponseList(facturas));
    }

    /**
     * POST /api/facturas/{id}/certificar
     * Envía factura a certificar
     */
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
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * POST /api/facturas/{id}/anular
     * Anula una factura
     */
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
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }


}