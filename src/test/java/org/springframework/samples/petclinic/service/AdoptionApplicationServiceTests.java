package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.AdoptionApplicationRepository;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AdoptionApplicationServiceTests {
	
	@Autowired
	protected AdoptionApplicationRepository adoptionApplicationRepository;
	
	@Autowired
	protected AdoptionApplicationService adoptionApplicationService;
	
	@Autowired
	protected AdoptionService adoptionService;
	
	@Autowired
	protected OwnerService ownerService;
	
	@Test
	void shouldFindWithCorrectId() {
		AdoptionApplication adoptionApplication1 = this.adoptionApplicationService.findById(1);
		assertThat(adoptionApplication1.getDescription()).isEqualTo("Prueba");
		assertThat(adoptionApplication1.getApproved()).isEqualTo(Boolean.FALSE);
	}
	
	@Test
	void shouldSaveAdoptionApplication() {
		Adoption adoption1 = this.adoptionService.findById(1);
		Owner owner1 = this.ownerService.findOwnerById(1);
		int found = adoption1.getAdoptionApplications().size();
		
		AdoptionApplication adoptionApplication = new AdoptionApplication();
		adoptionApplication.setDescription("Prueba");
		adoptionApplication.setApproved(Boolean.FALSE);
		adoptionApplication.setAdoption(adoption1);
		adoptionApplication.setOwner(owner1);
		
		adoption1.addAdoptionApplication(adoptionApplication);
		owner1.setAdoptionApplication(adoptionApplication);
		
		this.adoptionService.saveAdoption(adoption1);
		this.adoptionApplicationService.saveAdoptionApplication(adoptionApplication);
		
		assertThat(adoptionApplication.getId()).isNotNull();
		assertThat(adoption1.getAdoptionApplications().size()).isEqualTo(found+1);
	}
	
	@Test
	void shouldDeleteAdoptionApplication() {
		int InitSize = this.adoptionApplicationRepository.findAll().size();
		
		this.adoptionApplicationService.deleteById(1);
		
		int PostSize = this.adoptionApplicationRepository.findAll().size();
		assertThat(PostSize).isEqualTo(InitSize-1);
		
	}
	
}
