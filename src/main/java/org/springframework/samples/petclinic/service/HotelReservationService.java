package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.HotelReservation;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.HotelReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HotelReservationService {
	private final  HotelReservationRepository hotelReservationrepo; 

	@Autowired
	public HotelReservationService(final HotelReservationRepository hotelReservationrepo) {
		this.hotelReservationrepo = hotelReservationrepo;
	}
	
	@Transactional
	public int hotelReservationCount() {
		return (int)this.hotelReservationrepo.count();
	}
	
	@Transactional
	public Iterable<HotelReservation> findAll() {
		return this.hotelReservationrepo.findAll();
	}
	
	@Transactional(readOnly=true)
	public  Optional<HotelReservation> findHotelReservationById(final int id){ 
		return this.hotelReservationrepo.findById(id);
	}

	@Transactional
	public  void save(final HotelReservation hotelReservation) {   
		this.hotelReservationrepo.save(hotelReservation);
	}

	public  void delete(final HotelReservation hotelReservation) { 
		this.hotelReservationrepo.delete(hotelReservation);
	}

	public Collection<Pet> findPets() {
		// TODO Auto-generated method stub
		return this.hotelReservationrepo.findPets();
	}

	public Authorities getAuthority(final String username) {
		// TODO Auto-generated method stub
		return this.hotelReservationrepo.getAuthority(username);
	}

	public List<HotelReservation> findByPet(final Pet pet) {
		// TODO Auto-generated method stub
		return this.hotelReservationrepo.findByPet(pet);
	}
}
