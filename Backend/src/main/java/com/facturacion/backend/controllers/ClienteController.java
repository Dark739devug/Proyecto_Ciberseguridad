package com.facturacion.backend.controllers;

import com.facturacion.backend.models.Cliente;
import com.facturacion.backend.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement; // Importar

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@SecurityRequirement(name = "bearerAuth")

public class ClienteController {

    // Inyección de dependencia para usar los métodos CRUD
    @Autowired
    private ClienteRepository clienteRepository;

    // Endpoint: GET http://localhost:9090/api/clientes
    // Devuelve todos los clientes
    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    // Endpoint: POST http://localhost:9090/api/clientes
    // Crea un nuevo cliente a partir del JSON enviado
    @PostMapping
    public Cliente crearCliente(@RequestBody Cliente cliente) {
        return clienteRepository.save(cliente);
    }
}