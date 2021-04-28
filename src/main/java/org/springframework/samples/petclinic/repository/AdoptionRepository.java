package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Adoption;

public interface AdoptionRepository extends Repository<Adoption, Integer>{
	
	void save(Adoption adoption) throws DataAccessException;
	
	Adoption findById(int id);
	
	@Query("SELECT adoption FROM Adoption adoption WHERE adoption.pet.owner.id NOT LIKE :id")
	Collection<Adoption> findAdoptions(@Param("id") int id);
	
	@Modifying
	@Query("DELETE FROM Adoption adoption WHERE adoption.id LIKE :id")
	void deleteById(@Param("id") int id);
	
	@Query("SELECT adoption FROM Adoption adoption WHERE adoption.pet.owner.id LIKE :id")
	Collection<Adoption> findByOwnerId(@Param("id") int id);


}
