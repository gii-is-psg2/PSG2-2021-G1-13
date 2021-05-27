package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.AdoptionApplication;

public interface AdoptionApplicationRepository extends Repository<AdoptionApplication, Integer>{

	void save(AdoptionApplication adoptionApplication) throws DataAccessException;
	
	AdoptionApplication findById(int id);

	Collection<AdoptionApplication> findAll();

	@Modifying
	@Query("delete from AdoptionApplication a where a.id = :id")
	void deleteById(@Param("id") int id);
	
	@Query("SELECT a FROM AdoptionApplication a WHERE adoption.id LIKE :id")
	Collection<AdoptionApplication> findApplicationsByAdoption(@Param("id") int id);
	
	@Query("SELECT COUNT(a) FROM AdoptionApplication a WHERE approved = false AND adoption.pet.owner.id = :ownerId")
	int numAdoptionAppNoAceptadas(@Param("ownerId") int ownerId);
}