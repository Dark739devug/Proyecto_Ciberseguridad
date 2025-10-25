package com.facturacion.backend.controllers;

import com.facturacion.backend.dto.request.EstablecimientoRequest;
import com.facturacion.backend.dto.response.EstablecimientoResponse;
import com.facturacion.backend.mapper.EstablecimientoMapper;
import com.facturacion.backend.models.Establecimiento;
import com.facturacion.backend.services.EstablecimientoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/establecimientos")
@SecurityRequirement(name = "bearerAuth")
public class EstablecimientoController {

    private final EstablecimientoService establecimientoService;
    private final EstablecimientoMapper establecimientoMapper;

    public EstablecimientoController(EstablecimientoService establecimientoService,
                                     EstablecimientoMapper establecimientoMapper) {
        this.establecimientoService = establecimientoService;
        this.establecimientoMapper = establecimientoMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<EstablecimientoResponse>> obtenerTodos() {
        List<Establecimiento> establecimientos = establecimientoService.obtenerTodos();
        return ResponseEntity.ok(establecimientoMapper.toResponseList(establecimientos));
    }

    @GetMapping("/activos")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<EstablecimientoResponse>> obtenerActivos() {
        List<Establecimiento> establecimientos = establecimientoService.obtenerActivos();
        return ResponseEntity.ok(establecimientoMapper.toResponseList(establecimientos));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<EstablecimientoResponse> obtenerPorId(@PathVariable Long id) {
        Establecimiento establecimiento = establecimientoService.obtenerPorId(id);
        return ResponseEntity.ok(establecimientoMapper.toResponse(establecimiento));
    }

    @GetMapping("/nit/{nit}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<EstablecimientoResponse> obtenerPorNit(@PathVariable String nit) {
        Establecimiento establecimiento = establecimientoService.obtenerPorNit(nit);
        return ResponseEntity.ok(establecimientoMapper.toResponse(establecimiento));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<EstablecimientoResponse> crear(@RequestBody EstablecimientoRequest request) {
        Establecimiento establecimiento = establecimientoMapper.toEntity(request);
        Establecimiento establecimientoCreado = establecimientoService.crear(establecimiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(establecimientoMapper.toResponse(establecimientoCreado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<EstablecimientoResponse> actualizar(@PathVariable Long id, @RequestBody EstablecimientoRequest request) {
        Establecimiento establecimiento = establecimientoService.obtenerPorId(id);
        establecimientoMapper.updateEntity(request, establecimiento);
        Establecimiento establecimientoActualizado = establecimientoService.actualizar(id, establecimiento);
        return ResponseEntity.ok(establecimientoMapper.toResponse(establecimientoActualizado));
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        establecimientoService.desactivar(id);
        return ResponseEntity.ok().body("Establecimiento desactivado exitosamente");
    }
}