package com.jonathan.exam.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Document(collection = "apolice")
@Data
@ApiModel(value = "apolice")
public class Apolice {

	@Id
	@ApiModelProperty(position = 0, hidden = true,  required = false, notes = "Identificador da apólice, valor gerado automaticamente.")
	private String id;
	
	@ApiModelProperty(position = 1, required = false, notes = "Numero da apolice, valor gerado automaticamente.")
	private Integer numero;
	
	@ApiModelProperty(position = 2, required = false, notes = "Data de inicio da vigência da apólice.")
	@NotNull(message = "Obrigatorio informar a data de inicio da vigência da apólice.")
	private Date inicioVigencia;
	
	@ApiModelProperty(position = 3, required = false, notes = "Data de fim da vigência da apólice.")
	@NotNull(message = "Obrigatorio informar a data de fim da vigência da apólice.")
	private Date fimVigencia;
	
	@ApiModelProperty(position = 4, required = false, notes = "Placa para identificação do veiculo segurado pela apólice.")
	@NotEmpty(message = "Obrigatorio informar a placo do veiculo contemplado pela apólice.")
	private String placaVeiculo;
	
	@ApiModelProperty(position = 5, required = false, notes = "Valor da apolice.")
	@Min(value = 1, message = "Obrigatorio informar um valor minimo para a apólice.")
	private Double Valor;
	
	@DBRef
	@ApiModelProperty(position = 5, required = true, notes = "Cliente do qual é proprietario da apolice.")
	@NotNull(message = "Obrigatorio informar o dono da apólice.")
	private Cliente cliente;
	
	public void validateData() throws ValidationException {
		Calendar calendarDateBefore = Calendar.getInstance();
		calendarDateBefore.add(Calendar.DATE, -1);
		
		if (this.inicioVigencia.before(calendarDateBefore.getTime())) {
			throw new ValidationException("Inicio de vigencia não pode ser anterior ao dia atual: " + new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
		};
		
		if (this.inicioVigencia.after(this.fimVigencia)) {
			throw new ValidationException("Inicio de vigencia não pode ser posterior ao periodo final de vigencia: " + new SimpleDateFormat("dd-MM-yyyy").format(this.fimVigencia));
		};
		
		if (this.fimVigencia.before(this.inicioVigencia)) {
			throw new ValidationException("Fim da vigencia não pode ser anterior ao periodo inicial da vigencia: " + new SimpleDateFormat("dd-MM-yyyy").format(this.inicioVigencia));
		};
		
		
	}
}
