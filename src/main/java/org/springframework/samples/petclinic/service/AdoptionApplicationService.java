package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.repository.AdoptionApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdoptionApplicationService {
	
	private  AdoptionApplicationRepository adoptionApplicationRepository;
	
	@Autowired
	public AdoptionApplicationService(AdoptionApplicationRepository adoptionApplicationRepository) {
		this.adoptionApplicationRepository = adoptionApplicationRepository;
	}
	
	@Transactional
	public void saveAdoptionApplication(AdoptionApplication adoptionApplication) throws DataAccessException {
		this.adoptionApplicationRepository.save(adoptionApplication);
	}
	
	@Transactional(readOnly = true)
	public AdoptionApplication findById(int id) {
		return this.adoptionApplicationRepository.findById(id);
	}
	
	@Transactional
	public void deleteById(int id) {
		this.adoptionApplicationRepository.deleteById(id);
	}
}
