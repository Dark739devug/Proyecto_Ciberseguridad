package com.facturacion.backend.dto.response;

public class CategoriaProductoResponse {
    private Long idCategoria;
    private String nombreCategoria;
    private String descripcion;
    private Boolean activo;

    // Constructor vacío
    public CategoriaProductoResponse() {}

    // Getters y Setters
    public Long getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }

    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}