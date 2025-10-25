package com.facturacion.backend.dto.response;

public class UsuarioResponse {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String nombreRol;
    private Boolean activo;

    // Constructor vacío
    public UsuarioResponse() {}

    // Constructor completo
    public UsuarioResponse(Long idUsuario, String nombre, String apellido,
                           String email, String nombreRol, Boolean activo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.nombreRol = nombreRol;
        this.activo = activo;
    }

    // Getters y Setters
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}