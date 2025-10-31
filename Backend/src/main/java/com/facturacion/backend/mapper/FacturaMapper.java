package com.facturacion.backend.mapper;

import com.facturacion.backend.dto.response.DetalleFacturaResponse;
import com.facturacion.backend.dto.response.FacturaResponse;
import com.facturacion.backend.models.DetalleFactura;
import com.facturacion.backend.models.Factura;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FacturaMapper {

    /**
     * Convierte Factura a Response SIN detalles
     */
    public FacturaResponse toResponse(Factura factura) {
        if (factura == null) return null;

        FacturaResponse response = new FacturaResponse();
        response.setIdFactura(factura.getIdFactura());
        response.setNumeroFactura(factura.getNumeroFactura());
        response.setSerie(factura.getSerie());
        response.setCorrelativo(factura.getCorrelativo());
        response.setNumeroAutorizacion(factura.getNumeroAutorizacion());
        response.setNombreEstablecimiento(factura.getEstablecimiento() != null ? factura.getEstablecimiento().getNombreComercial() : null);
        response.setRazonSocialCliente(factura.getCliente() != null ? factura.getCliente().getRazonSocial() : null);
        response.setNitCliente(factura.getCliente() != null ? factura.getCliente().getNit() : null);
        response.setTipoDocumento(factura.getTipoDocumento() != null ? factura.getTipoDocumento().getNombreTipo() : null);
        response.setFechaEmision(factura.getFechaEmision());
        response.setFechaCertificacion(factura.getFechaCertificacion());
        response.setSubtotal(factura.getSubtotal());
        response.setTotalDescuentos(factura.getTotalDescuentos());
        response.setSubtotalConDescuento(factura.getSubtotalConDescuento());
        response.setTotalIva(factura.getTotalIva());
        response.setTotalFactura(factura.getTotalFactura());
        response.setNombreEstado(factura.getEstado() != null ? factura.getEstado().getNombreEstado() : null);
        response.setUsuarioCreacion(factura.getUsuarioCreacion() != null ? factura.getUsuarioCreacion().getNombre() + " " + factura.getUsuarioCreacion().getApellido() : null);
        response.setObservaciones(factura.getObservaciones());
        response.setDetalles(null); // Sin detalles por defecto
        return response;
    }

    /**
     * Convierte Factura a Response CON detalles
     * ESTE ES EL MÉTODO QUE FALTABA
     */
    public FacturaResponse toResponseConDetalles(Factura factura, List<DetalleFactura> detalles) {
        FacturaResponse response = toResponse(factura);

        // Agregar detalles si existen
        if (detalles != null && !detalles.isEmpty()) {
            response.setDetalles(detalleToResponseList(detalles));
        }

        return response;
    }

    /**
     * Convierte lista de Facturas a Response SIN detalles
     */
    public List<FacturaResponse> toResponseList(List<Factura> facturas) {
        return facturas.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte DetalleFactura a Response
     */
    public DetalleFacturaResponse detalleToResponse(DetalleFactura detalle) {
        if (detalle == null) return null;

        DetalleFacturaResponse response = new DetalleFacturaResponse();
        response.setIdDetalle(detalle.getIdDetalle());
        response.setNumeroLinea(detalle.getNumeroLinea());
        response.setNombreProducto(detalle.getProducto() != null ? detalle.getProducto().getNombreProducto() : null);
        response.setDescripcionProducto(detalle.getDescripcionProducto());
        response.setCantidad(detalle.getCantidad());
        response.setUnidadMedida(detalle.getUnidadMedida());
        response.setPrecioUnitario(detalle.getPrecioUnitario());
        response.setDescuento(detalle.getDescuento());
        response.setSubtotalLinea(detalle.getSubtotalLinea());
        response.setAplicaIva(detalle.getAplicaIva());
        response.setMontoIva(detalle.getMontoIva());
        response.setTotalLinea(detalle.getTotalLinea());
        return response;
    }

    /**
     * Convierte lista de DetalleFactura a Response
     */
    public List<DetalleFacturaResponse> detalleToResponseList(List<DetalleFactura> detalles) {
        return detalles.stream()
                .map(this::detalleToResponse)
                .collect(Collectors.toList());
    }
}