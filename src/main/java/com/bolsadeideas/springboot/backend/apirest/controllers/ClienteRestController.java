package com.bolsadeideas.springboot.backend.apirest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;

import exceptions.ClienteNotFoundException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
public class ClienteRestController {
    
    @Autowired
    private IClienteService clienteService;

    @GetMapping("/clientes")
    public List<Cliente> index() {
        return clienteService.findAll();
    }

    @GetMapping("/clientes/{id}")
    public Cliente show(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id);

        if(cliente == null) {
            throw new ClienteNotFoundException();
        }

        return cliente;
    }

    @PostMapping("/clientes")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente store(@RequestBody Cliente cliente) {
        return clienteService.save(cliente);
    }

    @PutMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente update(@PathVariable Long id, @RequestBody Cliente cliente) {
        Cliente clienteExistente = clienteService.findById(id);

        if(clienteExistente == null) {
            throw new ClienteNotFoundException();
        }

        String nombre = (cliente.getNombre() == null)? clienteExistente.getNombre() : cliente.getNombre();
        String apellido = (cliente.getApellido() == null)? clienteExistente.getApellido() : cliente.getApellido();
        String email = (cliente.getEmail() == null)? clienteExistente.getEmail() : cliente.getEmail(); 

        clienteExistente.setNombre(nombre);
        clienteExistente.setApellido(apellido);
        clienteExistente.setEmail(email);
        clienteService.save(clienteExistente);

        return clienteExistente;
    }

    @DeleteMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        clienteService.deleteById(id);
    }
}