package org.springframework.samples.petclinic.web;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdoptionApplicationController {
	
	
	private final OwnerService ownerService;
	private final AdoptionService adoptionService;
	private final AdoptionApplicationService adoptionApplicationService;
	
	private final String adoptionApplicationForm = "adoptionApplications/form";
	
	private final String adoptionApllicationDetails = "adoptionApplications/adoptionApplicationDetails";
	@Autowired
	public AdoptionApplicationController(AdoptionApplicationService adoptionApplicationService, OwnerService ownerService, AdoptionService adoptionService) {
		this.adoptionApplicationService=adoptionApplicationService;
		this.ownerService = ownerService;
		this.adoptionService = adoptionService;
	}
	
	@GetMapping(value="adoptionApplication/new/{ownerId}/{adoptionId}")
	public String initFormAdoptionApplication(@PathVariable("ownerId") int ownerId, @PathVariable("adoptionId") int adoptionId, Map<String, Object> model) {
		AdoptionApplication adoptionApplication = new AdoptionApplication();
		Owner owner = this.ownerService.findOwnerById(ownerId);
		Adoption adoption = this.adoptionService.findById(adoptionId);
		adoptionApplication.setOwner(owner);
		adoption.addAdoptionApplication(adoptionApplication);
		adoptionApplication.setApproved(false);
		model.put("adoptionApplication", adoptionApplication);
		return adoptionApplicationForm;
	}
	
	@PostMapping(value="adoptionApplication/new/{ownerId}/{adoptionId}")
	public String processFormAdoptionApplication(@Valid AdoptionApplication adoptionApplication,@PathVariable("ownerId") int ownerId, @PathVariable("adoptionId") int adoptionId, Map<String, Object> model,BindingResult result) {
		if(result.hasErrors()) {
			return adoptionApplicationForm;
		}else {
			this.adoptionApplicationService.saveAdoptionApplication(adoptionApplication);
			return "";
		}
	}
	
	@GetMapping(value="adoptionApplication/{ownerId}/{adoptionApplicationId}")
	public String initAdoptionApplicationDetails(@PathVariable("ownerId") int ownerId, @PathVariable("adoptionApplicationId") int adoptionApllicationId, Map<String, Object> model) {
		AdoptionApplication adoptionApplication = this.adoptionApplicationService.findById(adoptionApllicationId);
		model.put("ownerId", ownerId);
		model.put("adoptionApplication", adoptionApplication);
		return adoptionApllicationDetails;
	}
	@PostMapping(value="adoptionApplication/{ownerId}/{adoptionApplicationId}")
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
