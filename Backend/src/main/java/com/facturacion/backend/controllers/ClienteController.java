package com.facturacion.backend.controllers;

import com.facturacion.backend.dto.request.ClienteRequest;
import com.facturacion.backend.dto.response.ClienteResponse;
import com.facturacion.backend.mapper.ClienteMapper;
import com.facturacion.backend.models.Cliente;
import com.facturacion.backend.services.ClienteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@SecurityRequirement(name = "bearerAuth")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<ClienteResponse>> obtenerTodos() {
        List<Cliente> clientes = clienteService.obtenerTodos();
        return ResponseEntity.ok(clienteMapper.toResponseList(clientes));
    }

    @GetMapping("/activos")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<ClienteResponse>> obtenerActivos() {
        List<Cliente> clientes = clienteService.obtenerActivos();
        return ResponseEntity.ok(clienteMapper.toResponseList(clientes));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<ClienteResponse> obtenerPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(clienteMapper.toResponse(cliente));
    }

    @GetMapping("/nit/{nit}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<ClienteResponse> obtenerPorNit(@PathVariable String nit) {
        Cliente cliente = clienteService.obtenerPorNit(nit);
        return ResponseEntity.ok(clienteMapper.toResponse(cliente));
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<List<ClienteResponse>> buscarPorRazonSocial(@RequestParam String razonSocial) {
        List<Cliente> clientes = clienteService.buscarPorRazonSocial(razonSocial);
        return ResponseEntity.ok(clienteMapper.toResponseList(clientes));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<ClienteResponse> crear(@RequestBody ClienteRequest request) {
        Cliente cliente = clienteMapper.toEntity(request);
        Cliente clienteCreado = clienteService.crear(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteMapper.toResponse(clienteCreado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Facturación')")
    public ResponseEntity<ClienteResponse> actualizar(@PathVariable Long id, @RequestBody ClienteRequest request) {
        Cliente cliente = clienteService.obtenerPorId(id);
        clienteMapper.updateEntity(request, cliente);
        Cliente clienteActualizado = clienteService.actualizar(id, cliente);
        return ResponseEntity.ok(clienteMapper.toResponse(clienteActualizado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.ok().body("Cliente eliminado exitosamente");
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        clienteService.desactivar(id);
        return ResponseEntity.ok().body("Cliente desactivado exitosamente");
    }
}