package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.repository.AdoptionApplicationRepository;
import org.springframework.samples.petclinic.repository.AdoptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdoptionService {
	private AdoptionRepository adoptionRepository;
	
	private AdoptionApplicationRepository adoptionApplicationRepository;
	
	@Autowired
	public AdoptionService(AdoptionRepository adoptionRepository, AdoptionApplicationRepository adoptionApplicationRepository) {
		this.adoptionRepository = adoptionRepository;
		this.adoptionApplicationRepository = adoptionApplicationRepository;
	}
	
	@Transactional(readOnly = true)
	public Collection<Adoption> findAdoptions(int id){
		return adoptionRepository.findAdoptions(id);
	}
	
	@Transactional(readOnly = true)
	public Collection<Adoption> findAll(){
		return adoptionRepository.findAll();
	}
	
	@Transactional
	public void saveAdoption(Adoption adoption) throws DataAccessException {
		adoptionRepository.save(adoption);
	}

	@Transactional
	public void deleteAdoption(Adoption adoption) throws DataAccessException{
		
		List<AdoptionApplication> adoptionApplications = adoption.getAdoptionApplications().stream().collect(Collectors.toList()); 
		for(AdoptionApplication adoptionApplication : adoptionApplications) {
			adoption.removeAdoptionApplication(adoptionApplication);
			this.adoptionApplicationRepository.deleteById(adoptionApplication.getId());
		}
		
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
