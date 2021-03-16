package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.HotelReservation;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.HotelReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/hotelreservations")
public class HotelReservationController {

	@Autowired
	private HotelReservationService hotelReservationService;
	
	@Autowired
	private HotelReservationValidator hotelReservationValidator;
	
	@InitBinder("hotelReservation")
	public void initHotelReservationBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(this.hotelReservationValidator);
	}
	
	@GetMapping()
	public String hotelReservationsList(final ModelMap modelMap) {
		HotelReservationController.log.info("Loading list of hotel reservations view");
		final String view= "hotelreservations/hotelReservationsList";
		final Iterable<HotelReservation> hotelReservations=this.hotelReservationService.findAll();
		modelMap.addAttribute("hotelreservations", hotelReservations);
		return view;
	}
	
	@GetMapping(path="/new")
	public String createHotelReservation(final ModelMap modelMap) {
		HotelReservationController.log.info("Loading new hoter reservations form");
		final String view="hotelreservations/addHotelReservation";
		modelMap.addAttribute("hotelReservation", new HotelReservation());
		return view;
	}
	
	@PostMapping(path="/save")
	public String saveHotelReservation(@Valid final HotelReservation hotelReservation, final BindingResult result, final ModelMap modelMap) {
		HotelReservationController.log.info("Saving hotel reservation: " + hotelReservation.getId());
		String view="hotelreservations/hotelReservationsList";
		if(result.hasErrors()) {
			HotelReservationController.log.warn("Found errors on insertion: " + result.getAllErrors());
			modelMap.addAttribute("hotelReservation", hotelReservation);
			return "hotelreservations/addHotelReservation";
		}else {
			HotelReservationController.log.info("Hotel reservation validated: saving into DB");
			this.hotelReservationService.save(hotelReservation);
			modelMap.addAttribute("message", "Hotel reservation successfully saved!");
			view=this.hotelReservationsList(modelMap);
		}
		return view;
	}
	
	@GetMapping(path="/delete/{hotelReservationId}")
	public String deleteDish(@PathVariable("hotelReservationId") final int hotelReservationId, final ModelMap modelMap) {
		HotelReservationController.log.info("Deleting hotel reservation: " + hotelReservationId);
		String view="hotelreservations/hotelReservationsList";
		final Optional<HotelReservation> hotelReservation = this.hotelReservationService.findHotelReservationById(hotelReservationId);
		if(hotelReservation.isPresent()) {
			HotelReservationController.log.info("Hotel reservation found: deleting");
			this.hotelReservationService.delete(hotelReservation.get());
			modelMap.addAttribute("message", "Hotel reservation successfully deleted!");
			view=this.hotelReservationsList(modelMap);
		}else {
			HotelReservationController.log.warn("Hotel reservation not found in DB: " + hotelReservationId);
			modelMap.addAttribute("message", "Hotel reservation not found!");
			view=this.hotelReservationsList(modelMap);
		}
		return view;
	}
	
	@GetMapping(value = "/{hotelReservationId}/edit")
	public String initUpdateCasTbForm(@PathVariable("hotelReservationId") final int hotelReservationId, final ModelMap model) {
		HotelReservationController.log.info("Loading update hotel reservation form");
		final HotelReservation hotelReservation = this.hotelReservationService.findHotelReservationById(hotelReservationId).get();	
		model.put("hotelReservation", hotelReservation);
		return "hotelteservations/updateHotelReservation";
	}

	@PostMapping(value = "/{hotelReservationId}/edit")
	public String processUpdateCasTbForm(@Valid final HotelReservation hotelReservation, final BindingResult result,
			@PathVariable("hotelReservationId") final int hotelReservationId, final ModelMap model) {
		hotelReservation.setId(hotelReservationId);
		HotelReservationController.log.info("Updating hotel reservation: " + hotelReservationId);
		if (result.hasErrors()) {
			HotelReservationController.log.warn("Found errors on update: " + result.getAllErrors());
			model.put("hotelReservation", hotelReservation);
			return "hotelreservations/updateHotelReservation";
		}
		else {
			HotelReservationController.log.info("Hotel reservation validated: updating into DB");
			this.hotelReservationService.save(hotelReservation);
			return "redirect:/hotelreservations";
		}
	}
	
	@ModelAttribute("pets")
	public Collection<Pet> populatePets() {
		return this.hotelReservationService.findPets();
	}
	
}
