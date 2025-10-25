package com.facturacion.backend.mapper;

import com.facturacion.backend.dto.response.UsuarioResponse;
import com.facturacion.backend.models.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    /**
     * Convierte una entidad Usuario a DTO Response
     */
    public UsuarioResponse toResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol() != null ? usuario.getRol().getNombreRol() : null,
                usuario.getActivo()
        );
    }

    /**
     * Convierte una lista de Usuarios a lista de DTOs
     */
    public List<UsuarioResponse> toResponseList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}