package org.springframework.samples.petclinic.web;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@Controller
public class AdoptionApplicationController {


    public static final String OWNER_ID = "ownerId";
    private final OwnerService ownerService;
	private final AdoptionService adoptionService;
	private final AdoptionApplicationService adoptionApplicationService;

	private static final String adoptionApplicationForm = "adoptionApplications/formAdoptionApplication";
	private static final String adoptionApllicationDetails = "adoptionApplications/adoptionApplicationDetails";

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
	public String processFormAdoptionApplication(@ModelAttribute("adoptionApplication") @Valid AdoptionApplication adoptionApplication, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			model.put("adoptionApplication", adoptionApplication);
			return adoptionApplicationForm;
		}else {
			this.adoptionApplicationService.saveAdoptionApplication(adoptionApplication);
			model.put("message", "La solicitud de adopción se ha registrado correctamente.");
			return "welcome";
		}
	}

	@GetMapping(value="/adoptionApplication/{adoptionId}/{ownerId}")
	public String applicationsByAdoption(@PathVariable("adoptionId") int adoptionId,@PathVariable("ownerId") int ownerId, ModelMap model) {
		Collection<AdoptionApplication> adoptionApplicationList = adoptionApplicationService.findApplicationsByAdoption(adoptionId);
		model.addAttribute("adoptionApplicationDetails", adoptionApplicationList);
		model.addAttribute(OWNER_ID, ownerId);
		return adoptionApllicationDetails;
	}


	@GetMapping(value="/adoptionApplication/{ownerId}/{adoptionApplicationId}/accept")
	public String acceptAdoptionApplication(@PathVariable("ownerId") int ownerId, @PathVariable("adoptionApplicationId") int adoptionApllicationId, Map<String, Object> model) {
		AdoptionApplication adoptionApplication = this.adoptionApplicationService.findById(adoptionApllicationId);
		Adoption adoption = adoptionApplication.getAdoption();
		adoption.getPet().newOwner(adoptionApplication.getOwner());


		this.adoptionService.deleteAdoption(adoption);

		model.put(OWNER_ID, ownerId);
		return "/adoptions/adoptionMenu";
	}

	@GetMapping(value="/adoptionApplication/{ownerId}/{adoptionApplicationId}/reject")
	public String rejectAdoptionApplication(@PathVariable("ownerId") int ownerId, @PathVariable("adoptionApplicationId") int adoptionApllicationId, Map<String, Object> model) {
		this.adoptionApplicationService.deleteById(adoptionApllicationId);
		model.put(OWNER_ID, ownerId);
		return "/adoptions/adoptionMenu";
	}
}
