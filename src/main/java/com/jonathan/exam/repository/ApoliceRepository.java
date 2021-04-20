package com.jonathan.exam.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jonathan.exam.model.Apolice;

public interface ApoliceRepository extends MongoRepository<Apolice, String> {

	Optional<Apolice> findByNumero(Integer numero);
	
	boolean existsByNumero(Integer numero);

}
