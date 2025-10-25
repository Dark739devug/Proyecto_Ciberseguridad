package com.facturacion.backend.mapper;

import com.facturacion.backend.dto.request.ProductoRequest;
import com.facturacion.backend.dto.response.ProductoResponse;
import com.facturacion.backend.models.Producto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductoMapper {

    public ProductoResponse toResponse(Producto producto) {
        if (producto == null) return null;

        ProductoResponse response = new ProductoResponse();
        response.setIdProducto(producto.getIdProducto());
        response.setCodigoProducto(producto.getCodigoProducto());
        response.setNombreProducto(producto.getNombreProducto());
        response.setDescripcion(producto.getDescripcion());
        response.setNombreCategoria(producto.getCategoria() != null ? producto.getCategoria().getNombreCategoria() : null);
        response.setNombreEstablecimiento(producto.getEstablecimiento() != null ? producto.getEstablecimiento().getNombreComercial() : null);
        response.setPrecioUnitario(producto.getPrecioUnitario());
        response.setStockActual(producto.getStockActual());
        response.setUnidadMedida(producto.getUnidadMedida());
        response.setAplicaIva(producto.getAplicaIva());
        response.setFechaCreacion(producto.getFechaCreacion());
        response.setActivo(producto.getActivo());
        return response;
    }

    public List<ProductoResponse> toResponseList(List<Producto> productos) {
        return productos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}