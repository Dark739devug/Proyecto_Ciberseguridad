package com.facturacion.backend.mapper;

import com.facturacion.backend.dto.request.CategoriaProductoRequest;
import com.facturacion.backend.dto.response.CategoriaProductoResponse;
import com.facturacion.backend.models.CategoriaProducto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoriaProductoMapper {

    public CategoriaProductoResponse toResponse(CategoriaProducto categoria) {
        if (categoria == null) return null;

        CategoriaProductoResponse response = new CategoriaProductoResponse();
        response.setIdCategoria(categoria.getIdCategoria());
        response.setNombreCategoria(categoria.getNombreCategoria());
        response.setDescripcion(categoria.getDescripcion());
        response.setActivo(categoria.getActivo());
        return response;
    }

    public List<CategoriaProductoResponse> toResponseList(List<CategoriaProducto> categorias) {
        return categorias.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CategoriaProducto toEntity(CategoriaProductoRequest request) {
        if (request == null) return null;

        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombreCategoria(request.getNombreCategoria());
        categoria.setDescripcion(request.getDescripcion());
        categoria.setActivo(true);
        return categoria;
    }

    public void updateEntity(CategoriaProductoRequest request, CategoriaProducto categoria) {
        if (request == null || categoria == null) return;

        if (request.getNombreCategoria() != null) categoria.setNombreCategoria(request.getNombreCategoria());
        if (request.getDescripcion() != null) categoria.setDescripcion(request.getDescripcion());
    }
}