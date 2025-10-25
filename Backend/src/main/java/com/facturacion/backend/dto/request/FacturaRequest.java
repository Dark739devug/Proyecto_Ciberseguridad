package com.facturacion.backend.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class FacturaRequest {
    private Long idEstablecimiento;
    private Long idCliente;
    private Long idTipoDocumento;
    private String observaciones;
    private List<DetalleFacturaRequest> detalles;

    // Constructor vacío
    public FacturaRequest() {}

    // Getters y Setters
    public Long getIdEstablecimiento() { return idEstablecimiento; }
    public void setIdEstablecimiento(Long idEstablecimiento) { this.idEstablecimiento = idEstablecimiento; }

    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    public Long getIdTipoDocumento() { return idTipoDocumento; }
    public void setIdTipoDocumento(Long idTipoDocumento) { this.idTipoDocumento = idTipoDocumento; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public List<DetalleFacturaRequest> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleFacturaRequest> detalles) { this.detalles = detalles; }
}