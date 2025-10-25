package com.facturacion.backend.dto.request;

import java.math.BigDecimal;

public class DetalleFacturaRequest {
    private Long idProducto;
    private BigDecimal cantidad;
    private BigDecimal descuento;

    // Constructor vacío
    public DetalleFacturaRequest() {}

    // Getters y Setters
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }
}