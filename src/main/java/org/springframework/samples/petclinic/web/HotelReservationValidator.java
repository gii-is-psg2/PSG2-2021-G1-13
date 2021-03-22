package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.HotelReservation;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.HotelReservationService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class HotelReservationValidator implements Validator {
	
	private static final String REQUIRED = "required";

	@Autowired
	private HotelReservationService hotelReservationService;	
	
	@Override
	public void validate(final Object obj, final Errors errors) {
		final HotelReservation hotelReservation = (HotelReservation) obj;
		final LocalDate start = hotelReservation.getStart();
		final LocalDate finish = hotelReservation.getFinish();
		final Pet pet = hotelReservation.getPet();
		// start validation
		if (start == null) {
			errors.rejectValue("start", HotelReservationValidator.REQUIRED, HotelReservationValidator.REQUIRED);
		}else if(start.isBefore(LocalDate.now())) {
			errors.rejectValue("start", "The reservation must be for the future", "The reservation must be for the future");
		}
		// finish validation
		if (finish == null) {
			errors.rejectValue("finish", HotelReservationValidator.REQUIRED, HotelReservationValidator.REQUIRED);
		}else if(finish.isBefore(start)) {
			errors.rejectValue("finish", "Finish date must be the same date or a date after the start", "Finish date must be the same date or a date after the start");
		}
		// pet validation
		if (pet == null) {
			errors.rejectValue("pet", HotelReservationValidator.REQUIRED, HotelReservationValidator.REQUIRED);
		}		
	}

	/**
	 * This Validator validates *just* Pet instances
	 */
	@Override
	public boolean supports(final Class<?> clazz) {
		return HotelReservation.class.isAssignableFrom(clazz);
	}
	
}
