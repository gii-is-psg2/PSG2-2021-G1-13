package org.springframework.samples.petclinic.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.HotelReservation;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.HotelReservationService;
import org.springframework.samples.petclinic.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
		final String username = UserUtils.getUser();
		final Authorities authority = this.hotelReservationService.getAuthority(username);
		HotelReservationController.log.info("Loading list of hotel reservations view");
		final String view= "hotelreservations/hotelReservationsList";
		List<HotelReservation> hotelReservations= new ArrayList<>();
		if(authority.getAuthority().equals("owner")) {
			final List<HotelReservation> allReservations=StreamSupport.stream(this.hotelReservationService.findAll().spliterator(), false).collect(Collectors.toList());
			for(final HotelReservation res: allReservations) {
				if(res.getPet().getOwner().getUser().getUsername().equals(username)) hotelReservations.add(res);
			}
		}else {
			hotelReservations=StreamSupport.stream(this.hotelReservationService.findAll().spliterator(), false).collect(Collectors.toList());
		}
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
		if(result.hasErrors()) {
			HotelReservationController.log.warn("Found errors on insertion: " + result.getAllErrors());
			modelMap.addAttribute("hotelReservation", hotelReservation);
			return "hotelreservations/addHotelReservation";
		}else {
			HotelReservationController.log.info("Hotel reservation validated: saving into DB");
			this.hotelReservationService.save(hotelReservation);
			modelMap.addAttribute("message", "Hotel reservation successfully saved!");
			return this.hotelReservationsList(modelMap);
		}
	}

	@GetMapping(path="/delete/{hotelReservationId}")
	public String deleteDish(@PathVariable("hotelReservationId") final int hotelReservationId, final ModelMap modelMap) {
		HotelReservationController.log.info("Deleting hotel reservation: " + hotelReservationId);
		final Optional<HotelReservation> hotelReservation = this.hotelReservationService.findHotelReservationById(hotelReservationId);
		if(hotelReservation.isPresent()) {
			HotelReservationController.log.info("Hotel reservation found: deleting");
			this.hotelReservationService.delete(hotelReservation.get());
			modelMap.addAttribute("message", "Hotel reservation successfully deleted!");
		}else {
			HotelReservationController.log.warn("Hotel reservation not found in DB: " + hotelReservationId);
			modelMap.addAttribute("message", "Hotel reservation not found!");
		}
		return this.hotelReservationsList(modelMap);
	}

	@GetMapping(value = "/edit/{hotelReservationId}")
	public String initUpdateCasTbForm(@PathVariable("hotelReservationId") final int hotelReservationId, final ModelMap model) {
		HotelReservationController.log.info("Loading update hotel reservation form");
		Optional<HotelReservation> recovered = this.hotelReservationService.findHotelReservationById(hotelReservationId);
		if(recovered.isPresent()) {
            final HotelReservation hotelReservation = recovered.get();
            model.put("hotelReservation", hotelReservation);
            return "hotelreservations/updateHotelReservation";
        }else
            return "redirect:/hotelreservations";
	}

	@PostMapping(value = "/edit/{hotelReservationId}")
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
		final String username = UserUtils.getUser();
		final Authorities authority = this.hotelReservationService.getAuthority(username);
		if(authority.getAuthority().equals("owner")) {
			final Collection<Pet> allPets = this.hotelReservationService.findPets();
			final List<Pet> validPets = new ArrayList<>();
			for(final Pet pet:allPets) {
				if(pet.getOwner().getUser().getUsername().equals(username)) validPets.add(pet);
			}
			return validPets;
		}else {
			return this.hotelReservationService.findPets();
		}
	}

}
