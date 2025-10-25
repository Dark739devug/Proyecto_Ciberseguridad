package com.facturacion.backend.mapper;

import com.facturacion.backend.dto.request.ClienteRequest;
import com.facturacion.backend.dto.response.ClienteResponse;
import com.facturacion.backend.models.Cliente;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClienteMapper {

    public ClienteResponse toResponse(Cliente cliente) {
        if (cliente == null) return null;

        ClienteResponse response = new ClienteResponse();
        response.setIdCliente(cliente.getIdCliente());
        response.setNit(cliente.getNit());
        response.setRazonSocial(cliente.getRazonSocial());
        response.setNombreComercial(cliente.getNombreComercial());
        response.setDireccion(cliente.getDireccion());
        response.setMunicipio(cliente.getMunicipio());
        response.setDepartamento(cliente.getDepartamento());
        response.setTelefono(cliente.getTelefono());
        response.setEmail(cliente.getEmail());
        response.setFechaRegistro(cliente.getFechaRegistro());
        response.setActivo(cliente.getActivo());
        return response;
    }

    public List<ClienteResponse> toResponseList(List<Cliente> clientes) {
        return clientes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Cliente toEntity(ClienteRequest request) {
        if (request == null) return null;

        Cliente cliente = new Cliente();
        cliente.setNit(request.getNit());
        cliente.setRazonSocial(request.getRazonSocial());
        cliente.setNombreComercial(request.getNombreComercial());
        cliente.setDireccion(request.getDireccion());
        cliente.setMunicipio(request.getMunicipio());
        cliente.setDepartamento(request.getDepartamento());
        cliente.setTelefono(request.getTelefono());
        cliente.setEmail(request.getEmail());
        cliente.setActivo(true);
        return cliente;
    }

    public void updateEntity(ClienteRequest request, Cliente cliente) {
        if (request == null || cliente == null) return;

        if (request.getNit() != null) cliente.setNit(request.getNit());
        if (request.getRazonSocial() != null) cliente.setRazonSocial(request.getRazonSocial());
        if (request.getNombreComercial() != null) cliente.setNombreComercial(request.getNombreComercial());
        if (request.getDireccion() != null) cliente.setDireccion(request.getDireccion());
        if (request.getMunicipio() != null) cliente.setMunicipio(request.getMunicipio());
        if (request.getDepartamento() != null) cliente.setDepartamento(request.getDepartamento());
        if (request.getTelefono() != null) cliente.setTelefono(request.getTelefono());
        if (request.getEmail() != null) cliente.setEmail(request.getEmail());
    }
}