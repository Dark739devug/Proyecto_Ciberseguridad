package com.facturacion.backend.controllers;

import com.facturacion.backend.dto.request.ProductoRequest;
import com.facturacion.backend.dto.response.ProductoResponse;
import com.facturacion.backend.mapper.ProductoMapper;
import com.facturacion.backend.models.Producto;
import com.facturacion.backend.services.ProductoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@SecurityRequirement(name = "bearerAuth")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoMapper productoMapper;

    public ProductoController(ProductoService productoService, ProductoMapper productoMapper) {
        this.productoService = productoService;
        this.productoMapper = productoMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<ProductoResponse>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productoMapper.toResponseList(productos));
    }

    @GetMapping("/activos")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<ProductoResponse>> obtenerActivos() {
        List<Producto> productos = productoService.obtenerActivos();
        return ResponseEntity.ok(productoMapper.toResponseList(productos));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(productoMapper.toResponse(producto));
    }

    @GetMapping("/codigo/{codigo}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<ProductoResponse> obtenerPorCodigo(@PathVariable String codigo) {
        Producto producto = productoService.obtenerPorCodigo(codigo);
        return ResponseEntity.ok(productoMapper.toResponse(producto));
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<ProductoResponse>> buscarPorNombre(@RequestParam String nombre) {
        List<Producto> productos = productoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(productoMapper.toResponseList(productos));
    }

    @GetMapping("/categoria/{idCategoria}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<ProductoResponse>> obtenerPorCategoria(@PathVariable Long idCategoria) {
        List<Producto> productos = productoService.obtenerPorCategoria(idCategoria);
        return ResponseEntity.ok(productoMapper.toResponseList(productos));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<ProductoResponse> crear(@RequestBody ProductoRequest request) {
        Producto producto = new Producto();
        producto.setCodigoProducto(request.getCodigoProducto());
        producto.setNombreProducto(request.getNombreProducto());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioUnitario(request.getPrecioUnitario());
        producto.setStockActual(request.getStockActual());
        producto.setUnidadMedida(request.getUnidadMedida());
        producto.setAplicaIva(request.getAplicaIva());

        Producto productoCreado = productoService.crear(producto, request.getIdCategoria(), request.getIdEstablecimiento());
        return ResponseEntity.status(HttpStatus.CREATED).body(productoMapper.toResponse(productoCreado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id, @RequestBody ProductoRequest request) {
        Producto producto = new Producto();
        producto.setCodigoProducto(request.getCodigoProducto());
        producto.setNombreProducto(request.getNombreProducto());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioUnitario(request.getPrecioUnitario());
        producto.setStockActual(request.getStockActual());
        producto.setUnidadMedida(request.getUnidadMedida());
        producto.setAplicaIva(request.getAplicaIva());

        Producto productoActualizado = productoService.actualizar(id, producto, request.getIdCategoria(), request.getIdEstablecimiento());
        return ResponseEntity.ok(productoMapper.toResponse(productoActualizado));
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        productoService.desactivar(id);
        return ResponseEntity.ok().body("Producto desactivado exitosamente");
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> actualizarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        productoService.actualizarStock(id, cantidad);
        return ResponseEntity.ok().body("Stock actualizado exitosamente");
    }
}