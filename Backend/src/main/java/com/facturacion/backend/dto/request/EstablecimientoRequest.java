package com.facturacion.backend.dto.request;

public class EstablecimientoRequest {
    private String nit;
    private String nombreComercial;
    private String razonSocial;
    private String direccion;
    private String municipio;
    private String departamento;
    private String codigoPostal;
    private String telefono;
    private String email;
    private String codigoEstablecimiento;

    // Constructor vacío
    public EstablecimientoRequest() {}

    // Getters y Setters
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getNombreComercial() { return nombreComercial; }
    public void setNombreComercial(String nombreComercial) { this.nombreComercial = nombreComercial; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCodigoEstablecimiento() { return codigoEstablecimiento; }
    public void setCodigoEstablecimiento(String codigoEstablecimiento) { this.codigoEstablecimiento = codigoEstablecimiento; }
}