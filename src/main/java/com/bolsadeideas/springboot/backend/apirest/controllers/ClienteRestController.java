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
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Cliente datosCliente) {
        Map<String, Object> response = new HashMap<>();
        Cliente clienteExistente = clienteService.findById(id);
        Cliente clienteActualizado = null;

        if (clienteExistente == null) {
            response.put("mensaje", "No se ha encontrado al cliente con id " + id.toString() + " en nuestros registros");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        String nombre = (datosCliente.getNombre() == null) ? clienteExistente.getNombre() : datosCliente.getNombre();
        String apellido = (datosCliente.getApellido() == null) ? clienteExistente.getApellido() : datosCliente.getApellido();
        String email = (datosCliente.getEmail() == null) ? clienteExistente.getEmail() : datosCliente.getEmail();
        
        try {
            clienteExistente.setNombre(nombre);
            clienteExistente.setApellido(apellido);
            clienteExistente.setEmail(email);
            clienteActualizado = clienteService.save(clienteExistente);

        } catch(DataAccessException ex) {
            response.put("mensaje", "Ocurrio un error en la operación");
            // response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Cliente actualizado correctamente");
        response.put("data", clienteActualizado);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            clienteService.deleteById(id);

        } catch(DataAccessException ex) {
            response.put("mensaje", "Se produjo un error al eliminar el cliente con id "+ id);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Cliente eliminado correctamente");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}