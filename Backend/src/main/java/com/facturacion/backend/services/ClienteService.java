package com.facturacion.backend.services;

import com.facturacion.backend.models.Cliente;
import com.facturacion.backend.repositories.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    public List<Cliente> obtenerActivos() {
        return clienteRepository.findByActivo(true);
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
    }

    public Cliente obtenerPorNit(String nit) {
        return clienteRepository.findByNit(nit)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con NIT: " + nit));
    }

    public List<Cliente> buscarPorRazonSocial(String razonSocial) {
        return clienteRepository.findByRazonSocialContainingIgnoreCase(razonSocial);
    }

    @Transactional
    public Cliente crear(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente actualizar(Long id, Cliente clienteActualizado) {
        Cliente cliente = obtenerPorId(id);

        cliente.setNit(clienteActualizado.getNit());
        cliente.setRazonSocial(clienteActualizado.getRazonSocial());
        cliente.setNombreComercial(clienteActualizado.getNombreComercial());
        cliente.setDireccion(clienteActualizado.getDireccion());
        cliente.setMunicipio(clienteActualizado.getMunicipio());
        cliente.setDepartamento(clienteActualizado.getDepartamento());
        cliente.setTelefono(clienteActualizado.getTelefono());
        cliente.setEmail(clienteActualizado.getEmail());

        return clienteRepository.save(cliente);
    }

    @Transactional
    public void desactivar(Long id) {
        Cliente cliente = obtenerPorId(id);
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    @Transactional
    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }
}
