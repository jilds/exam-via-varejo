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
	@ApiModelProperty(position = 0, hidden = true,  required = false, notes = "Identificador da ap�lice, valor gerado automaticamente.")
	private String id;
	
	@ApiModelProperty(position = 1, required = false, notes = "Numero da apolice, valor gerado automaticamente.")
	private Integer numero;
	
	@ApiModelProperty(position = 2, required = false, notes = "Data de inicio da vig�ncia da ap�lice.")
	@NotNull(message = "Obrigatorio informar a data de inicio da vig�ncia da ap�lice.")
	private Date inicioVigencia;
	
	@ApiModelProperty(position = 3, required = false, notes = "Data de fim da vig�ncia da ap�lice.")
	@NotNull(message = "Obrigatorio informar a data de fim da vig�ncia da ap�lice.")
	private Date fimVigencia;
	
	@ApiModelProperty(position = 4, required = false, notes = "Placa para identifica��o do veiculo segurado pela ap�lice.")
	@NotEmpty(message = "Obrigatorio informar a placo do veiculo contemplado pela ap�lice.")
	private String placaVeiculo;
	
	@ApiModelProperty(position = 5, required = false, notes = "Valor da apolice.")
	@Min(value = 1, message = "Obrigatorio informar um valor minimo para a ap�lice.")
	private Double Valor;
	
	@DBRef
	@ApiModelProperty(position = 5, required = true, notes = "Cliente do qual � proprietario da apolice.")
	@NotNull(message = "Obrigatorio informar o dono da ap�lice.")
	private Cliente cliente;
	
	public void validateData() throws ValidationException {
		Calendar calendarDateBefore = Calendar.getInstance();
		calendarDateBefore.add(Calendar.DATE, -1);
		
		if (this.inicioVigencia.before(calendarDateBefore.getTime())) {
			throw new ValidationException("Inicio de vigencia n�o pode ser anterior ao dia atual: " + new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
		};
		
		if (this.inicioVigencia.after(this.fimVigencia)) {
			throw new ValidationException("Inicio de vigencia n�o pode ser posterior ao periodo final de vigencia: " + new SimpleDateFormat("dd-MM-yyyy").format(this.fimVigencia));
		};
		
		if (this.fimVigencia.before(this.inicioVigencia)) {
			throw new ValidationException("Fim da vigencia n�o pode ser anterior ao periodo inicial da vigencia: " + new SimpleDateFormat("dd-MM-yyyy").format(this.inicioVigencia));
		};
		
		
	}
}
