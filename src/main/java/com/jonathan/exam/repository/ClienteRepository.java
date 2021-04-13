package com.jonathan.exam.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jonathan.exam.model.Cliente;

public interface ClienteRepository extends MongoRepository<Cliente, UUID> {

}
