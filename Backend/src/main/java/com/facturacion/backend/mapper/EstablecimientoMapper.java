package com.facturacion.backend.mapper;

import com.facturacion.backend.dto.request.EstablecimientoRequest;
import com.facturacion.backend.dto.response.EstablecimientoResponse;
import com.facturacion.backend.models.Establecimiento;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EstablecimientoMapper {

    public EstablecimientoResponse toResponse(Establecimiento establecimiento) {
        if (establecimiento == null) return null;

        EstablecimientoResponse response = new EstablecimientoResponse();
        response.setIdEstablecimiento(establecimiento.getIdEstablecimiento());
        response.setNit(establecimiento.getNit());
        response.setNombreComercial(establecimiento.getNombreComercial());
        response.setRazonSocial(establecimiento.getRazonSocial());
        response.setDireccion(establecimiento.getDireccion());
        response.setMunicipio(establecimiento.getMunicipio());
        response.setDepartamento(establecimiento.getDepartamento());
        response.setCodigoPostal(establecimiento.getCodigoPostal());
        response.setTelefono(establecimiento.getTelefono());
        response.setEmail(establecimiento.getEmail());
        response.setCodigoEstablecimiento(establecimiento.getCodigoEstablecimiento());
        response.setActivoCertificador(establecimiento.getActivoCertificador());
        response.setFechaRegistro(establecimiento.getFechaRegistro());
        response.setActivo(establecimiento.getActivo());
        return response;
    }

    public List<EstablecimientoResponse> toResponseList(List<Establecimiento> establecimientos) {
        return establecimientos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Establecimiento toEntity(EstablecimientoRequest request) {
        if (request == null) return null;

        Establecimiento establecimiento = new Establecimiento();
        establecimiento.setNit(request.getNit());
        establecimiento.setNombreComercial(request.getNombreComercial());
        establecimiento.setRazonSocial(request.getRazonSocial());
        establecimiento.setDireccion(request.getDireccion());
        establecimiento.setMunicipio(request.getMunicipio());
        establecimiento.setDepartamento(request.getDepartamento());
        establecimiento.setCodigoPostal(request.getCodigoPostal());
        establecimiento.setTelefono(request.getTelefono());
        establecimiento.setEmail(request.getEmail());
        establecimiento.setCodigoEstablecimiento(request.getCodigoEstablecimiento());
        establecimiento.setActivoCertificador(true);
        establecimiento.setActivo(true);
        return establecimiento;
    }

    public void updateEntity(EstablecimientoRequest request, Establecimiento establecimiento) {
        if (request == null || establecimiento == null) return;

        if (request.getNit() != null) establecimiento.setNit(request.getNit());
        if (request.getNombreComercial() != null) establecimiento.setNombreComercial(request.getNombreComercial());
        if (request.getRazonSocial() != null) establecimiento.setRazonSocial(request.getRazonSocial());
        if (request.getDireccion() != null) establecimiento.setDireccion(request.getDireccion());
        if (request.getMunicipio() != null) establecimiento.setMunicipio(request.getMunicipio());
        if (request.getDepartamento() != null) establecimiento.setDepartamento(request.getDepartamento());
        if (request.getCodigoPostal() != null) establecimiento.setCodigoPostal(request.getCodigoPostal());
        if (request.getTelefono() != null) establecimiento.setTelefono(request.getTelefono());
        if (request.getEmail() != null) establecimiento.setEmail(request.getEmail());
        if (request.getCodigoEstablecimiento() != null) establecimiento.setCodigoEstablecimiento(request.getCodigoEstablecimiento());
    }
}