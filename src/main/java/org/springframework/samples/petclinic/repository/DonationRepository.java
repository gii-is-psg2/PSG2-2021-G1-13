package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends CrudRepository<Donation, Integer>{
	@Override
	public List<Donation> findAll();

	@Query("SELECT c.donations FROM Cause c WHERE c.id = :id")
	public List<Donation> findDonationsByCauseId(@Param("id") int id);

}
