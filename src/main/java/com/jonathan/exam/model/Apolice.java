package com.jonathan.exam.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;

@Document(collection = "apolice")
@Data
public class Apolice {

	@MongoId
	private String id;
	private Integer numero;
	private Date inicioVigencia;
	private Date fimVigencia;
	private String placaVeiculo;
	private Double Valor;

	
}
