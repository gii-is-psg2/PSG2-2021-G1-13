package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/causes")
public class CauseController {

	private static final String CREATE_DONATION_VIEW = "causes/createDonationForm";
	private static final String CREATE_UPDATE_CAUSE_VIEW = "causes/createOrUpdateCauseForm";

	@Autowired
	private CauseService causeService;

	@Autowired
    private OwnerService ownerService;

	@Autowired
	private CauseValidator causeValidator;

	@InitBinder("cause")
	public void initCauseBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(this.causeValidator);
	}

	@GetMapping()
	public String causesList(ModelMap modelMap) {
		CauseController.log.info("Loading list of causes view");
		final String view= "causes/causesList";
		final Iterable<Cause> causes=this.causeService.findAllCauses();
		modelMap.addAttribute("causes", causes);
		return view;
	}

	@GetMapping(path="/new")
	public String createCause(ModelMap modelMap) {
		CauseController.log.info("Loading new cause form");
		modelMap.addAttribute("cause", new Cause());
		return CREATE_UPDATE_CAUSE_VIEW;
	}

	@PostMapping(path="/new")
	public String saveCause(@Valid Cause cause, final BindingResult result, ModelMap modelMap) {
		CauseController.log.info("Saving cause: " + cause.getId());
		if(result.hasErrors()) {
			CauseController.log.warn("Found errors on insertion: " + result.getAllErrors());
			modelMap.addAttribute("cause", cause);
			return CREATE_UPDATE_CAUSE_VIEW;
		}else {
			CauseController.log.info("Cause validated: saving into DB");
			cause.setDonations(new HashSet<>());
			cause.setClosed(false);
			this.causeService.saveCause(cause);
			modelMap.addAttribute("message", "Cause successfully saved!");
			return "redirect:/causes/";
		}
	}

	@GetMapping(path="/{causeId}/delete")
	public String deleteCause(@PathVariable("causeId") final int causeId, ModelMap modelMap) {
		CauseController.log.info("Deleting cause: " + causeId);
		String view="causes/causesList";
		final Cause cause = this.causeService.findCauseById(causeId);
		if(cause!=null) {
			CauseController.log.info("Cause found: deleting");
			this.causeService.deleteCauseById(cause.getId());
			modelMap.addAttribute("message", "Cause successfully deleted!");
		}else {
			CauseController.log.warn("Cause not found in DB: " + causeId);
			modelMap.addAttribute("message", "Cause not found!");
		}
		return "redirect:/causes/";
	}

	@GetMapping(value = "/{causeId}/edit")
	public String initUpdateCauseForm(@PathVariable("causeId") final int causeId, ModelMap model) {
		CauseController.log.info("Loading update cause form");
		final Cause cause = this.causeService.findCauseById(causeId);
		model.put("cause", cause);
		return CREATE_UPDATE_CAUSE_VIEW;
	}

	@PostMapping(value = "/{causeId}/edit")
	public String processUpdateCauseForm(@Valid Cause cause, BindingResult result,
			@PathVariable("causeId") int causeId, ModelMap model) {
		cause.setId(causeId);
		cause.setClosed(false);
		CauseController.log.info("Updating cause: " + causeId);
		if (result.hasErrors()) {
			CauseController.log.warn("Found errors on update: " + result.getAllErrors());
			model.put("cause", cause);
			return "causes/updateCause";
		}
		else {
			CauseController.log.info("Cause validated: updating into DB");
			this.causeService.saveCause(cause);
			return "redirect:/causes/{causeId}";
		}
	}

	@GetMapping("/{causeId}")
	public String causeDetails(@PathVariable("causeId") final int causeId, ModelMap modelmap) {
		final String view = "causes/causeDetails";
		final List<Donation> donations = this.causeService.findDonationsByCause(causeId);
		modelmap.addAttribute("donations", donations);
		modelmap.addAttribute("cause", this.causeService.findCauseById(causeId));
		return view;
	}

	@GetMapping("/{causeId}/donate")
	public String initCreationDonationForm(@PathVariable("causeId") final int causeId, ModelMap modelmap) {
		Donation donation = new Donation();
        Collection<Owner> results = this.ownerService.findOwnerByLastName("");
        modelmap.addAttribute("ownerList", results);
		modelmap.addAttribute("donation", donation);
		modelmap.addAttribute("causeId", causeId);
		return CauseController.CREATE_DONATION_VIEW;

	}

	@PostMapping("/{causeId}/donate")
	public String processCreateDonationForm(@Valid Donation donation, BindingResult result, @PathVariable("causeId") final int causeId, ModelMap modelmap){
		if(result.hasErrors()) {
			modelmap.addAttribute("donation", donation);
			return CauseController.CREATE_DONATION_VIEW;
		}else {
		    Cause cause = this.causeService.findCauseById(causeId);
		    donation.setCause(cause);
			donation.setDate(LocalDate.now());
            cause.getDonations().add(donation);
			if (cause.getDonations().stream().mapToDouble(d -> d.getAmount()).sum()>=cause.getTarget())
			    cause.setClosed(true);
            this.causeService.saveCause(cause);
			return "redirect:/causes/{causeId}";
		}
	}

}
