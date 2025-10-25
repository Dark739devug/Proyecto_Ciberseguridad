package com.facturacion.backend.controllers;

import com.facturacion.backend.dto.response.UsuarioResponse;
import com.facturacion.backend.mapper.UsuarioMapper;
import com.facturacion.backend.models.Usuario;
import com.facturacion.backend.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    /**
     * GET /api/usuarios
     * Obtiene todos los usuarios (solo Admin)
     */
    @GetMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<UsuarioResponse>> obtenerTodos() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        List<UsuarioResponse> response = usuarioMapper.toResponseList(usuarios);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/usuarios/{id}
     * Obtiene un usuario por ID (solo Admin)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        UsuarioResponse response = usuarioMapper.toResponse(usuario);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/usuarios/{id}
     * Actualiza un usuario (solo Admin)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<UsuarioResponse> actualizar(
            @PathVariable Long id,
            @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
        UsuarioResponse response = usuarioMapper.toResponse(usuarioActualizado);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/usuarios/{id}
     * Desactiva un usuario (solo Admin)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.ok().body("Usuario desactivado exitosamente");
    }
}