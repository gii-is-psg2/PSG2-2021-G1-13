package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.HotelReservationService;
import org.springframework.stereotype.Component;

@Component
public class PetFormatter implements Formatter<Pet> {
	
	private final HotelReservationService hotelReservationService;

	@Autowired
	public PetFormatter(final HotelReservationService hotelReservationService) {
		this.hotelReservationService = hotelReservationService;
	}

	@Override
	public String print(final Pet pet, final Locale locale) {
		return pet.getName();
	}

	@Override
	public Pet parse(final String text, final Locale locale) throws ParseException {
		final Collection<Pet> findPets = this.hotelReservationService.findPets();
		for (final Pet pet : findPets) {
			if (pet.getName().equals(text)) {
				return pet;
			}
		}
		throw new ParseException("type not found: " + text, 0);
	}

}
