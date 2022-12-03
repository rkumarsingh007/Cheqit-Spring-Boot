package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.model.DemoBharatrath;

@Repository
public interface BharatRathRepository extends MongoRepository<DemoBharatrath, String> {
	public DemoBharatrath save(DemoBharatrath d);
	public DemoBharatrath findOneById(String id);
	public void deleteOneById(String id);
}