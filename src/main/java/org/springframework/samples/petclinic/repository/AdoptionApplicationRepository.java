package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.AdoptionApplication;

public interface AdoptionApplicationRepository extends Repository<AdoptionApplication, Integer>{

	void save(AdoptionApplication adoptionApplication) throws DataAccessException;
	
	AdoptionApplication findById(int id);
	
	void deleteById(int id) throws DataAccessException;
}
