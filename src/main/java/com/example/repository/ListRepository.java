package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.ListOfBear;

@Repository
public interface ListRepository extends MongoRepository<ListOfBear, String>{
	
	public ListOfBear findOneByDealerId(String id);
	public void deleteOneByDealerId(String id);
	
	@Query(value="{$and:[{dealerId:?0},{list:?1}]}",count = true)
	public int doesItExists(String id,String listId);

}
