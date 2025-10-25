package com.facturacion.backend.dto.request;

public class CategoriaProductoRequest {
    private String nombreCategoria;
    private String descripcion;

    // Constructor vacío
    public CategoriaProductoRequest() {}

    // Getters y Setters
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}