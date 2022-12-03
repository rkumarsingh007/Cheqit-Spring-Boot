package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.model.Certificate;

public interface CertRepo extends MongoRepository<Certificate, String> {
	public Certificate save(Certificate c);
	public Certificate findOneById(String id);
}
