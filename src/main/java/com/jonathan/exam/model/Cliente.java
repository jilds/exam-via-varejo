package com.jonathan.exam.model;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Document(collection = "cliente")
@Data
@ApiModel(value = "Endereço")
public class Cliente {
 
    @Id
    private String id;
    
    @ApiModelProperty(position = 1, required = false, notes = "Nome do cliente", example = "Joaquim")
    private String nome;
    
    @CPF(message = "CPF inválido. Favor corrigir e tentar novamente.")
    private String cpf;
    
    private String cidade;
    
    private String uf;
    
    private Apolice apolice;

}
