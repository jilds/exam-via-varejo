package com.jonathan.exam.model;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jonathan.exam.util.ValidacaoUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Document(collection = "cliente")
@Data
@ApiModel(value = "cliente")
public class Cliente {

	@Id
	@ApiModelProperty(position = 0, hidden = true,  required = false, notes = "Identificador do cliente, valor gerado automaticamente.")
	private String id;

	@NotEmpty(message = "Obrigatorio informar o nome do cliente.")
	@ApiModelProperty(position = 1, required = true, notes = "Nome do cliente", example = "Joaquim")
	private String nome;

	@ApiModelProperty(position = 2, required = true, notes = "CPF do cliente", example = "288.734.550-85")
	@NotEmpty(message = "Obrigatorio informar o CPF do cliente.")
	private String cpf;

	@ApiModelProperty(position = 3, required = true, notes = "Cidade de domicilio do reside", example = "Cuiabá")
	@NotEmpty(message = "Obrigatorio informar o cidade de residencia do cliente.")
	private String cidade;

	@ApiModelProperty(position = 4, required = true, notes = "Estado de domicilio do cliente", example = "Mato Grosso ou MT")
	@NotEmpty(message = "Obrigatorio informar o estado de residencia do cliente.")
	private String uf;

	public void setCpf(String cpf) {
		this.cpf = ValidacaoUtil.removerMascara(cpf);
	}

}
