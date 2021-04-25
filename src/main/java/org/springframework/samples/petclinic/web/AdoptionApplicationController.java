package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.AdoptionApplicationService;
import org.springframework.samples.petclinic.service.AdoptionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdoptionApplicationController {
	
	
	private final OwnerService ownerService;
	private final AdoptionService adoptionService;
	private final AdoptionApplicationService adoptionApplicationService;
	
	private final String adoptionApplicationForm = "adoptionApplications/formAdoptionApplication";
	
	private final String adoptionApllicationDetails = "adoptionApplications/adoptionApplicationDetails";
	@Autowired
	public AdoptionApplicationController(AdoptionApplicationService adoptionApplicationService, OwnerService ownerService, AdoptionService adoptionService) {
		this.adoptionApplicationService=adoptionApplicationService;
		this.ownerService = ownerService;
		this.adoptionService = adoptionService;
	}
	
	@GetMapping(value="/adoptionApplication/new/{ownerId}/{adoptionId}")
	public String initFormAdoptionApplication(@PathVariable("ownerId") int ownerId, @PathVariable("adoptionId") int adoptionId, Map<String, Object> model) {
		AdoptionApplication adoptionApplication = new AdoptionApplication();
		Owner owner = this.ownerService.findOwnerById(ownerId);
		Adoption adoption = this.adoptionService.findById(adoptionId);
		adoptionApplication.setOwner(owner);
		adoptionApplication.setAdoption(adoption);
		adoptionApplication.setApproved(false);
		model.put("adoptionApplication", adoptionApplication);
		return adoptionApplicationForm;
	}
	
	@PostMapping(value="/adoptionApplication/new")
	public String processFormAdoptionApplication(@ModelAttribute("adoptionApplication") @Validated AdoptionApplication adoptionApplication, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			model.put("adoptionApplication", adoptionApplication);
			return adoptionApplicationForm;
		}else {
			this.adoptionApplicationService.saveAdoptionApplication(adoptionApplication);
			return "redirect:/";
		}
	}
	
	@GetMapping(value="/adoptionApplication/{adoptionId}")
	public String applicationsByAdoption(@PathVariable("adoptionId") int adoptionId, ModelMap model) {
		Collection<AdoptionApplication> adoptionApplicationList = adoptionApplicationService.findApplicationsByAdoption(adoptionId);
		model.addAttribute("adoptionApllicationDetails", adoptionApplicationList);
		return adoptionApllicationDetails;
	}
	
	
	@GetMapping(value="/adoptionApplication/{ownerId}/{adoptionApplicationId}")
	public String initAdoptionApplicationDetails(@PathVariable("ownerId") int ownerId, @PathVariable("adoptionApplicationId") int adoptionApllicationId, Map<String, Object> model) {
		AdoptionApplication adoptionApplication = this.adoptionApplicationService.findById(adoptionApllicationId);
		model.put("ownerId", ownerId);
		model.put("adoptionApplication", adoptionApplication);
		return adoptionApllicationDetails;
	}
	
	@GetMapping(value="/adoptionApplication/{adoptionId}/{adoptionApplicationId}/delete")
	public String rejectAdoptionApplication(@PathVariable("adoptionId") int adoptionId, @PathVariable("adoptionApplicationId") int adoptionApllicationId, Map<String, Object> model) {
		adoptionApplicationService.deleteById(adoptionApllicationId);
		return "redirect:/adoptionApplication/"+adoptionId;
	}
	
	
	@PostMapping(value="/adoptionApplication/{ownerId}/{adoptionApplicationId}")
	public String processAdoptionApplicationDetails(@Valid AdoptionApplication adoptionApplication,@PathVariable("ownerId") int ownerId, @PathVariable("adoptionApplicationId") int adoptionApllicationId, Map<String, Object> model, BindingResult result) {
		if(result.hasErrors()) {
			return adoptionApllicationDetails;
		}else {
			if(adoptionApplication.getApproved()) {
				adoptionApplication.getAdoption().getPet().newOwner(adoptionApplication.getOwner());
				this.adoptionApplicationService.saveAdoptionApplication(adoptionApplication);
			}else {
				this.adoptionApplicationService.deleteById(adoptionApllicationId);
			}
			return "/adoption/" + ownerId;
		}
	}

}
