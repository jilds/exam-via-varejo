package com.jonathan.exam.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jonathan.exam.model.Cliente;
import com.jonathan.exam.repository.ClienteRepository;
import com.jonathan.exam.util.ValidacaoUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Validated
@RequestMapping("service/cliente")
@Api(tags = "Cliente", description = "Operações para cadastro, consulta, alteração e exclusão de Clientes")
public class ClienteController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClienteController.class);

	private ClienteRepository cRepository;

	public ClienteController(@Autowired ClienteRepository cRepository) {
		this.cRepository = cRepository;
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Cadastra um novo cliente com base no modelo de exemplo")
	public ResponseEntity<Object> save(@RequestBody @Valid Cliente cliente, BindingResult bindingResult) {
		try {
			LOGGER.debug("create() with body {} of type {}", cliente, cliente.getClass());

			if (!ObjectUtils.isEmpty(cliente.getId())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao salvar Registro.");
			}

			if (cRepository.existsByCpf(cliente.getCpf())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Cliente já cadastrado. Favor corrigir e tentar novamente.");
			}

			if (ValidacaoUtil.cpfValido(cliente.getCpf())) {

				this.cRepository.save(cliente);
				return new ResponseEntity<>(cliente, HttpStatus.CREATED);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("CPF inválido. Favor corrigir e tentar novamente.");
			}

		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Atualiza os dados de um determinado cliente.", notes = "Ao realizar a atualização deve ser provido o identificador do objeto que será atualizado, assim como suas novas informações.")
	public ResponseEntity<Object> update(
			@ApiParam(value = "Identificador do cliente que será atualizado.", required = true) @PathVariable("id") String id,
			@RequestBody Cliente cliente) {

		try {
			LOGGER.info("update() of id#{} with body {}", id, cliente);

			Optional<Cliente> clienteData = this.cRepository.findById(id);

			if (clienteData.isPresent()) {
				Cliente _cliente = clienteData.get();
				_cliente.setNome(cliente.getNome());
				_cliente.setCpf(cliente.getCpf());
				_cliente.setUf(cliente.getUf());
				_cliente.setCidade(cliente.getCidade());
				return ResponseEntity.ok(cRepository.save(_cliente));
			} else {
				return ResponseEntity.notFound().build();
			}

		} catch (ValidationException ve) {
			LOGGER.error(ve.getLocalizedMessage());
			return ResponseEntity.badRequest().body(ve.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
		}

	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna um determinado cliente com base no identificador fornecido.", notes = "Deve ser fornecido um identificador valido.")
	public @ResponseBody ResponseEntity<Object> get(
			@ApiParam(value = "Identificador do cliente que será pesquisado.", required = true) @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		Optional<Cliente> optionalEntity;
		try {

			optionalEntity = this.cRepository.findById(id);

			if (optionalEntity.isPresent()) {
				return new ResponseEntity<>(optionalEntity.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOGGER.debug(e.getLocalizedMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
		}
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna uma lista de clientes")
	public @ResponseBody ResponseEntity<Object> get(HttpServletRequest request, HttpServletResponse response) {
		List<Cliente> list = Collections.emptyList();
		try {

			list = this.cRepository.findAll();

			if (CollectionUtils.isEmpty(list)) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nenhum registro cadastrado.");
			}

			return ResponseEntity.ok(list);
		} catch (Exception e) {
			LOGGER.debug(e.getLocalizedMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
		}
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Realiza remoção de um determinado cliente com base no identificador.", notes = "Deve ser fornecido um identificador valido na URL. Uma vez deletado o recurso não poderá mais ser recuperado.")
	public @ResponseBody ResponseEntity<String> delete(
			@ApiParam(value = "Identificador do recurso que será removido", required = true) @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		try {

			if (!cRepository.existsById(id)) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Registro não cadastrado.");
			}

			cRepository.deleteById(id);

			return ResponseEntity.ok("Registro excluido com sucesso!");

		} catch (ConstraintViolationException e) {
			LOGGER.debug("Message: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (DataIntegrityViolationException e) {
			LOGGER.debug("1: " + e.getMostSpecificCause());
			LOGGER.debug("2: " + e.getRootCause());
			LOGGER.debug("Message: " + e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao excluir registro, favor entrar em contado com o administrador do sistema.");
		}
	}

}
