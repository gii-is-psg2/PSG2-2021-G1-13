package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.HotelReservation;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class HotelReservationServiceTest {

	@Autowired
	protected HotelReservationService hotelservice;
	
	@Autowired
	protected PetService petService;
	
	@Test
	void hotelReservationCountTest() {
		int size = hotelservice.hotelReservationCount();
		
		assertNotEquals(0, size);
		assertEquals(4, size);
	}
	
	
	@Test
	void findAllTest() {
		Iterable<HotelReservation> hotelReservations = hotelservice.findAll();
		
		assertNotNull(hotelReservations);
	}
	
	@Test
	void saveTest() {
		HotelReservation hotelReservation = new HotelReservation();
		
		Pet pet = petService.findPetById(2);
		
		hotelReservation.setStart(LocalDate.now());
		hotelReservation.setFinish(LocalDate.now().plusDays(2));
		hotelReservation.setPet(pet);
		
		assertDoesNotThrow(()->hotelservice.save(hotelReservation));
		assertEquals(5, hotelservice.hotelReservationCount());
	}
	
	@Test
	void deleteTest() {
		HotelReservation hotelReservation = hotelservice.findHotelReservationById(1).orElse(null);
		
		assertNotNull(hotelReservation);
		assertDoesNotThrow(()->hotelservice.delete(hotelReservation));
		assertEquals(3, hotelservice.hotelReservationCount());
	}
	
	
	@Test
	void findPetsTest() {
		Collection<Pet> pets = hotelservice.findPets();
		
		assertNotNull(pets);
		assertEquals(13, pets.size());
	}
	
	@Test
	void findByPetTest() {	
		Pet pet = petService.findPetById(2);
		
		List<HotelReservation> pets = hotelservice.findByPet(pet);
		assertNotNull(pets);
		assertEquals(1, pets.size());
	}
}
