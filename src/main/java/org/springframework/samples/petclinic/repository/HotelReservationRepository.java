package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.HotelReservation;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelReservationRepository  extends CrudRepository<HotelReservation, Integer>{

	@Query("SELECT pet FROM Pet pet ORDER BY pet.id")
	List<Pet> findPets() throws DataAccessException;

}
