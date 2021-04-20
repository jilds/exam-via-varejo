package com.jonathan.exam.controller;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

import com.jonathan.exam.model.Apolice;
import com.jonathan.exam.repository.ApoliceRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("service/apolice")
@Api(tags = "Apolice", description = "Operações para cadastro, consulta, alteração e exclusão de Apolices")
@Validated
public class ApoliceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApoliceController.class);

	private ApoliceRepository aRepository;

	public ApoliceController(@Autowired ApoliceRepository aRepository) {
		this.aRepository = aRepository;
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Cadastra uma nova apolice com base no modelo de exemplo")
	public ResponseEntity<Object> save(@RequestBody @Valid Apolice apolice, BindingResult bindingResult) {
		try {
			LOGGER.debug("create() with body {} of type {}", apolice, apolice.getClass());

			if (!ObjectUtils.isEmpty(apolice.getId())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao salvar Registro.");
			}
			
			apolice.validateData();
			
			apolice.setNumero(generateApoliceNumber());
			
						
			this.aRepository.save(apolice);
			return new ResponseEntity<>(apolice, HttpStatus.CREATED);

		} catch (ValidationException ve) {
			LOGGER.error(ve.getLocalizedMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ve.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
		}
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Atualiza os dados de uma determinada apolice.", notes = "Ao realizar a atualização deve ser provido o identificador do objeto que será atualizado, assim como suas novas informações.")
	public ResponseEntity<Object> update(
			@ApiParam(value = "Identificador da apolice que será atualizada.", required = true) @PathVariable("id") String id,
			@RequestBody @Valid Apolice apolice) {

		try {
			LOGGER.info("update() of id#{} with body {}", id, apolice);

			Optional<Apolice> apoliceData = this.aRepository.findById(id);

			if (apoliceData.isPresent()) {
				Apolice _apolice = apoliceData.get();
				_apolice.setNumero(apolice.getNumero());
				_apolice.setPlacaVeiculo(apolice.getPlacaVeiculo());
				_apolice.setInicioVigencia(apolice.getInicioVigencia());
				_apolice.setFimVigencia(apolice.getFimVigencia());
				_apolice.setValor(apolice.getValor());
				return ResponseEntity.ok(aRepository.save(_apolice));
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
	@ApiOperation(value = "Retorna uma determinada apolice com base no identificador fornecido.", notes = "Deve ser fornecido um identificador valido.")
	public @ResponseBody ResponseEntity<Object> get(
			@ApiParam(value = "Identificador do apolice que será pesquisado.", required = true) @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		Optional<Apolice> optionalEntity;
		try {

			optionalEntity = this.aRepository.findById(id);

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
	
	@GetMapping(value = "/numero/{numero}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retorna uma determinada apolice com base no numero de identificação fornecido.", notes = "Deve ser fornecido o número de uma apolice valido.")
	public @ResponseBody ResponseEntity<Object> getByNumero(
			@ApiParam(value = "Número de identificação da apolice que será pesquisado.", required = true) @PathVariable("numero") Integer numero,
			HttpServletRequest request, HttpServletResponse response) {
		Optional<Apolice> optionalEntity;
		try {

			optionalEntity = this.aRepository.findByNumero(numero);

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
	@ApiOperation(value = "Retorna uma lista de apolices")
	public @ResponseBody ResponseEntity<Object> get(HttpServletRequest request, HttpServletResponse response) {
		List<Apolice> list = Collections.emptyList();
		try {

			list = this.aRepository.findAll();

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
	@ApiOperation(value = "Realiza remoção de uma determinada apolice com base no identificador.", notes = "Deve ser fornecido um identificador valido na URL. Uma vez deletado o recurso não poderá mais ser recuperado.")
	public @ResponseBody ResponseEntity<String> delete(
			@ApiParam(value = "Identificador da apolice que será removida", required = true) @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		try {

			if (!aRepository.existsById(id)) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Registro não cadastrado.");
			}

			aRepository.deleteById(id);

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
	
	@SuppressWarnings("deprecation")
	private Integer generateApoliceNumber() {
		Random random = new Random();
		
		int numero = random.nextInt(Calendar.getInstance().getTime().getMonth()+Calendar.getInstance().getTime().getDay()+Calendar.getInstance().getTime().getDay());
		
		aRepository.findByNumero(numero);
		
		if (aRepository.existsByNumero(numero)) {
			generateApoliceNumber();
		}
		
		return numero;
	}

}
