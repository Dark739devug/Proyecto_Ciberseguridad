package com.facturacion.backend.controllers;

import com.facturacion.backend.models.Factura;
import com.facturacion.backend.repositories.FacturaRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@SecurityRequirement(name = "bearerAuth")
public class FacturaController {

    @Autowired
    private FacturaRepository facturaRepository;

    @GetMapping
    public List<Factura> listarTodas() {
        return facturaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Factura obtenerPorId(@PathVariable Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Factura no encontrada"
                ));
    }

    @GetMapping("/numero/{numeroFactura}")
    public Factura obtenerPorNumero(@PathVariable String numeroFactura) {
        return facturaRepository.findByNumeroFactura(numeroFactura)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Factura no encontrada"
                ));
    }

    @GetMapping("/estado/{idEstado}")
    public List<Factura> listarPorEstado(@PathVariable Long idEstado) {
        return facturaRepository.findByEstadoIdEstado(idEstado);
    }

    @GetMapping("/cliente/{idCliente}")
    public List<Factura> listarPorCliente(@PathVariable Long idCliente) {
        return facturaRepository.findByClienteIdCliente(idCliente);
    }

    @GetMapping("/rango-fechas")
    public List<Factura> listarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return facturaRepository.findByFechaEmisionBetween(fechaInicio, fechaFin);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Factura crear(@RequestBody Factura factura) {
        return facturaRepository.save(factura);
    }

    @PutMapping("/{id}")
    public Factura actualizar(@PathVariable Long id, @RequestBody Factura factura) {
        if (!facturaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada");
        }
        factura.setIdFactura(id);
        return facturaRepository.save(factura);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        facturaRepository.deleteById(id);
    }
}