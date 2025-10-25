package com.facturacion.backend.controllers;

import com.facturacion.backend.dto.request.CategoriaProductoRequest;
import com.facturacion.backend.dto.response.CategoriaProductoResponse;
import com.facturacion.backend.mapper.CategoriaProductoMapper;
import com.facturacion.backend.models.CategoriaProducto;
import com.facturacion.backend.services.CategoriaProductoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@SecurityRequirement(name = "bearerAuth")
public class CategoriaProductoController {

    private final CategoriaProductoService categoriaService;
    private final CategoriaProductoMapper categoriaMapper;

    public CategoriaProductoController(CategoriaProductoService categoriaService,
                                       CategoriaProductoMapper categoriaMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<CategoriaProductoResponse>> obtenerTodas() {
        List<CategoriaProducto> categorias = categoriaService.obtenerTodas();
        return ResponseEntity.ok(categoriaMapper.toResponseList(categorias));
    }

    @GetMapping("/activas")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<CategoriaProductoResponse>> obtenerActivas() {
        List<CategoriaProducto> categorias = categoriaService.obtenerActivas();
        return ResponseEntity.ok(categoriaMapper.toResponseList(categorias));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<CategoriaProductoResponse> obtenerPorId(@PathVariable Long id) {
        CategoriaProducto categoria = categoriaService.obtenerPorId(id);
        return ResponseEntity.ok(categoriaMapper.toResponse(categoria));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<CategoriaProductoResponse> crear(@RequestBody CategoriaProductoRequest request) {
        CategoriaProducto categoria = categoriaMapper.toEntity(request);
        CategoriaProducto categoriaCreada = categoriaService.crear(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaMapper.toResponse(categoriaCreada));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<CategoriaProductoResponse> actualizar(@PathVariable Long id, @RequestBody CategoriaProductoRequest request) {
        CategoriaProducto categoria = categoriaService.obtenerPorId(id);
        categoriaMapper.updateEntity(request, categoria);
        CategoriaProducto categoriaActualizada = categoriaService.actualizar(id, categoria);
        return ResponseEntity.ok(categoriaMapper.toResponse(categoriaActualizada));
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        categoriaService.desactivar(id);
        return ResponseEntity.ok().body("Categoría desactivada exitosamente");
    }
}