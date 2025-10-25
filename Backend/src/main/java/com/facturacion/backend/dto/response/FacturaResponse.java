package com.facturacion.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FacturaResponse {
    private Long idFactura;
    private String numeroFactura;
    private String serie;
    private Integer correlativo;
    private String numeroAutorizacion;
    private String nombreEstablecimiento;
    private String razonSocialCliente;
    private String nitCliente;
    private String tipoDocumento;
    private LocalDateTime fechaEmision;
    private LocalDateTime fechaCertificacion;
    private BigDecimal subtotal;
    private BigDecimal totalDescuentos;
    private BigDecimal subtotalConDescuento;
    private BigDecimal totalIva;
    private BigDecimal totalFactura;
    private String nombreEstado;
    private String usuarioCreacion;
    private String observaciones;
    private List<DetalleFacturaResponse> detalles;

    // Constructor vacío
    public FacturaResponse() {}

    // Getters y Setters completos...
    public Long getIdFactura() { return idFactura; }
    public void setIdFactura(Long idFactura) { this.idFactura = idFactura; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }

    public Integer getCorrelativo() { return correlativo; }
    public void setCorrelativo(Integer correlativo) { this.correlativo = correlativo; }

    public String getNumeroAutorizacion() { return numeroAutorizacion; }
    public void setNumeroAutorizacion(String numeroAutorizacion) { this.numeroAutorizacion = numeroAutorizacion; }

    public String getNombreEstablecimiento() { return nombreEstablecimiento; }
    public void setNombreEstablecimiento(String nombreEstablecimiento) { this.nombreEstablecimiento = nombreEstablecimiento; }

    public String getRazonSocialCliente() { return razonSocialCliente; }
    public void setRazonSocialCliente(String razonSocialCliente) { this.razonSocialCliente = razonSocialCliente; }

    public String getNitCliente() { return nitCliente; }
    public void setNitCliente(String nitCliente) { this.nitCliente = nitCliente; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public LocalDateTime getFechaCertificacion() { return fechaCertificacion; }
    public void setFechaCertificacion(LocalDateTime fechaCertificacion) { this.fechaCertificacion = fechaCertificacion; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getTotalDescuentos() { return totalDescuentos; }
    public void setTotalDescuentos(BigDecimal totalDescuentos) { this.totalDescuentos = totalDescuentos; }

    public BigDecimal getSubtotalConDescuento() { return subtotalConDescuento; }
    public void setSubtotalConDescuento(BigDecimal subtotalConDescuento) { this.subtotalConDescuento = subtotalConDescuento; }

    public BigDecimal getTotalIva() { return totalIva; }
    public void setTotalIva(BigDecimal totalIva) { this.totalIva = totalIva; }

    public BigDecimal getTotalFactura() { return totalFactura; }
    public void setTotalFactura(BigDecimal totalFactura) { this.totalFactura = totalFactura; }

    public String getNombreEstado() { return nombreEstado; }
    public void setNombreEstado(String nombreEstado) { this.nombreEstado = nombreEstado; }

    public String getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(String usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public List<DetalleFacturaResponse> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleFacturaResponse> detalles) { this.detalles = detalles; }
}