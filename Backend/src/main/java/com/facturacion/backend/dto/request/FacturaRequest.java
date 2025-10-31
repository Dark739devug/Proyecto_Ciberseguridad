package com.facturacion.backend.dto.request;

import java.util.List;

public class FacturaRequest {
    private Long idEstablecimiento;
    private Long idCliente;
    private Long idTipoDocumento;
    private Long idUsuarioCreacion; // ← AGREGADO
    private String observaciones;
    private List<DetalleFacturaRequest> detalles;

    public FacturaRequest() {}

    // Getters y Setters
    public Long getIdEstablecimiento() { return idEstablecimiento; }
    public void setIdEstablecimiento(Long idEstablecimiento) { this.idEstablecimiento = idEstablecimiento; }

    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    public Long getIdTipoDocumento() { return idTipoDocumento; }
    public void setIdTipoDocumento(Long idTipoDocumento) { this.idTipoDocumento = idTipoDocumento; }

    public Long getIdUsuarioCreacion() { return idUsuarioCreacion; }
    public void setIdUsuarioCreacion(Long idUsuarioCreacion) { this.idUsuarioCreacion = idUsuarioCreacion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public List<DetalleFacturaRequest> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleFacturaRequest> detalles) { this.detalles = detalles; }
}