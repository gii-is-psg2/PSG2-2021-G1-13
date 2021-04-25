package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.repository.AdoptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdoptionService {
	private AdoptionRepository adoptionRepository;
	
	@Autowired
	public AdoptionService(AdoptionRepository adoptionRepository) {
		this.adoptionRepository = adoptionRepository;
	}
	
	@Transactional(readOnly = true)
	public Collection<Adoption> findAdoptions(int id){
		return adoptionRepository.findAdoptions(id);
	}
	
	@Transactional
	public void saveAdoption(Adoption adoption) throws DataAccessException {
		adoptionRepository.save(adoption);
	}
	@Modifying
	@Transactional
	public void deleteAdoption(int id) throws DataAccessException{
		Adoption adoption = this.adoptionRepository.findById(id);
		adoption.setPet(null);
		adoption.setAdoptionApplications(null);
		this.adoptionRepository.save(adoption);
		this.adoptionRepository.deleteById(adoption.getId());
	}
	
	@Transactional(readOnly = true)
	public Adoption findById(int id) throws DataAccessException{
		return this.adoptionRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Collection<Adoption> findByOwnerId(int id) throws DataAccessException{
		return this.adoptionRepository.findByOwnerId(id);
	}
}
