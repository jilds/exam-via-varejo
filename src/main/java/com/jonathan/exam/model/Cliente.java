package com.jonathan.exam.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "cliente")
@Data
public class Cliente {
 
    @Id
    private UUID id;
    private String nome;
    private String cpf;
    private String cidade;
    private String uf;

}
