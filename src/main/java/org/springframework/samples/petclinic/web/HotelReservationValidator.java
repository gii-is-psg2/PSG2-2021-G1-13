package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.List;

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

	public Boolean concurrentDates(final HotelReservation reservation) {
		final List<HotelReservation> reservations = this.hotelReservationService.findByPet(reservation.getPet());
		final LocalDate start = reservation.getStart();
		final LocalDate finish = reservation.getFinish();
		for(final HotelReservation oldRes:reservations) {
			final Boolean startBeforeOldStart = start.isBefore(oldRes.getStart());
			final Boolean startAfterOldStart = start.isAfter(oldRes.getStart());
			final Boolean startEqualOldStart = start.isEqual(oldRes.getStart());
			final Boolean startBeforeOldFinish = start.isBefore(oldRes.getFinish());
			final Boolean startEqualOldFinish = start.isEqual(oldRes.getFinish());
			final Boolean finishAfterOldStart = finish.isAfter(oldRes.getStart());
			final Boolean finishEqualOldStart = finish.isEqual(oldRes.getStart());
			final Boolean finishBeforeOldFinish = finish.isBefore(oldRes.getFinish());
			final Boolean finishAfterOldFinish = finish.isAfter(oldRes.getFinish());
			final Boolean finishEqualOldFinish = finish.isEqual(oldRes.getFinish());
			if((startBeforeOldStart && finishAfterOldStart) ||
				startEqualOldStart || startEqualOldFinish || finishEqualOldStart || finishEqualOldFinish ||
				(startAfterOldStart && finishBeforeOldFinish ) ||
				(startBeforeOldFinish && finishAfterOldFinish)) return true;
		}
		return false;
	}

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
			errors.rejectValue("start", "past_reservation", "<fmt:message key=\\\"past_reservation\\\"/>");
		}
		// finish validation
		if (finish == null) {
			errors.rejectValue("finish", HotelReservationValidator.REQUIRED, HotelReservationValidator.REQUIRED);
		}else if(start != null && finish.isBefore(start)) {
			errors.rejectValue("finish", "date_mixup", "<fmt:message key=\\\"date_mixup\\\"/>");
		}
		// pet validation
		if (pet == null) {
			errors.rejectValue("pet", "pet_null", "<fmt:message key=\\\"pet_null\\\"/>");
		}
		// other reservations validation
		if(start != null && finish != null && this.concurrentDates(hotelReservation)) {
		    errors.rejectValue("pet", "concurrent_date", "<fmt:message key=\"concurrent_date\"/>");
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
