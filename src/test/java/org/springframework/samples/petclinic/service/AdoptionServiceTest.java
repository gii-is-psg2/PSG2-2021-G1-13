package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AdoptionServiceTest {
	
	@Autowired
	AdoptionService adoptionService;
	
	@Test
	void findAllAdoptionsTest() {
		Collection<Adoption> adoptions = adoptionService.findAdoptions(1);
		
		assertNotNull(adoptions);
		assertEquals(3, adoptions.size());
	}
	
	@Test
	void findByOwnerIdAdoptionTest() {

		Collection<Adoption> adoption = adoptionService.findByOwnerId(1);
		
		assertNotNull(adoption);
		assertEquals(1, adoption.size());
	}
	
	@Test
	void findByIdAdoptionTest() {

		Adoption adoption = adoptionService.findById(4);
		
		assertNotNull(adoption);
		assertEquals("Owner having personal problems", adoption.getDescription());
	}
	
	@Test
	void saveAdoptionTest() {

		Adoption adoption = new Adoption();
		
		adoption.setId(5);
		adoption.setDescription("Adoption Test");
		adoption.setPet(null);
		
		adoptionService.saveAdoption(adoption);
		
		assertEquals(5, adoptionService.findAll().size());
	}
	
	@Test
	void deleteAdoptionTest() {

		Adoption adoption = adoptionService.findById(4);
		
		adoptionService.deleteAdoption(adoption);
		
		assertEquals(3, adoptionService.findAll().size());
	}
	
	

}
