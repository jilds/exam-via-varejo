package com.jonathan.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonathan.exam.model.Cliente;
import com.jonathan.exam.repository.ClienteRepository;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("service/cliente")
@Api(tags = "Cliente", description = "Operações para cadastro, consulta, alteração e exclusão de Clientes")
public class ClienteController {
	
	private ClienteRepository cRepository;
	
	public ClienteController (@Autowired ClienteRepository cRepository) {
		this.cRepository = cRepository;
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> save(@RequestBody Cliente c, BindingResult bindingResult) {
		
		cRepository.save(c);
		
		if (c.getId().isEmpty()) {
			return ResponseEntity.badRequest().body("Erro ao salvar");
		}
		
		return ResponseEntity.ok("Salvo com sucesso");
    }
	
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> update(@RequestBody Cliente c) {
		
        return ResponseEntity.ok("Salvo com sucesso");
    }
	
}
