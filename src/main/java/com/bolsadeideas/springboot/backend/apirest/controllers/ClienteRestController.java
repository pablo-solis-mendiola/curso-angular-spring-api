package com.bolsadeideas.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@CrossOrigin(origins = { "http://localhost:4200" }, methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT, RequestMethod.DELETE })
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/clientes")
    public ResponseEntity<?> index() {
        Map<String, Object> response = new HashMap<>();
        List<Cliente> clientes;

        try {
            clientes = clienteService.findAll();

        } catch (DataAccessException ex) {
            response.put("mensaje", "Ocurrio un error al realizar esta consulta");
            // response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(clientes.isEmpty()) {
            response.put("mensaje", "No se ha encontrado ningún registro");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        response.put("data", clientes);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<String, Object>();
        Cliente cliente = null;

        try {
            cliente = clienteService.findById(id);

        } catch (DataAccessException ex) {
            response.put("mensaje", "Ocurrio un error al realizar esta consulta");
            // response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (cliente == null) {
            response.put("mensaje", "No se ha encontrado al cliente con id " + id.toString() + " en nuestros registros");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        response.put("data", cliente);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    // Alternate method: Throw an exception and set responseStatus there
    // Check ClienteNotFoundException class for an example.

    // @GetMapping("/clientes/{id}")
    // public Cliente show(@PathVariable Long id) {
    // Cliente cliente = clienteService.findById(id);

    // if(cliente == null) {
    // throw new ClienteNotFoundException();
    // }

    // return cliente;
    // }

    @PostMapping("/clientes")
    public ResponseEntity<?> store(@RequestBody Cliente cliente) {
        Map<String, Object> response = new HashMap<>();
        Cliente newCliente;

        try {
            newCliente = clienteService.save(cliente);

        } catch (DataAccessException ex) {
            response.put("mensaje", "Ocurrio un error en la operación");
            // response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        response.put("mensaje", "Cliente Registrado correctamente");

        response.put("data", newCliente);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Cliente clienteActualizado) {
        Map<String, Object> response = new HashMap<>();
        Cliente cliente = null;
        
        try {
            cliente = clienteService.findById(id);

        } catch(DataAccessException ex) {
            response.put("mensaje", "Ocurrio un error en la operación");
            // response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (cliente == null) {
            response.put("mensaje", "No se ha encontrado al cliente con id " + id.toString() + " en nuestros registros");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        String nombre = (clienteActualizado.getNombre() == null) ? cliente.getNombre() : clienteActualizado.getNombre();
        String apellido = (clienteActualizado.getApellido() == null) ? cliente.getApellido() : clienteActualizado.getApellido();
        String email = (clienteActualizado.getEmail() == null) ? cliente.getEmail() : clienteActualizado.getEmail();

        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setEmail(email);
        clienteService.save(cliente);

        response.put("mensaje", "Cliente Actualizado correctamente");
        response.put("data", cliente);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        clienteService.deleteById(id);
    }
}