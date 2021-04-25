package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.SelectOwnerForm;
import org.springframework.samples.petclinic.service.AdoptionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.util.UserUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdoptionController {
	private final AdoptionService adoptionService;
	
	private final PetService petService;
	
	private final OwnerService ownerService;

	
	private final String adoptionForm = "adoptions/formAdoption";
	
	@Autowired
	public AdoptionController(AdoptionService adoptionService, PetService petService, OwnerService ownerService) {
		this.adoptionService = adoptionService;
		this.petService = petService;
		this.ownerService = ownerService;
	}
	
	@GetMapping("/adoptions")
	public String selectOwner(ModelMap model) {
		Collection<Owner> results = this.ownerService.findOwnerByLastName("");
		model.put("ownerList", results);
		model.put("selectOwnerForm", new SelectOwnerForm());
		return "adoptions/selectOwner";
	}
	
	@GetMapping("/adoptions/{ownerId}")
	public String adoptionsOwner(Map<String, Object> model,@PathVariable("ownerId") int ownerId) {
		List<Adoption> adoptions = this.adoptionService.findByOwnerId(ownerId).stream().collect(Collectors.toList());
		model.put("adoptions", adoptions);
		model.put("ownerId", ownerId);
		return "adoptions/adoptionsOwner";
	}
	
	@GetMapping("/adoptions/{ownerId}/list")
	public String adoptionsList(Map<String, Object> model,@PathVariable("ownerId") int ownerId) {
		List<Adoption> adoptions = this.adoptionService.findAdoptions(ownerId).stream().collect(Collectors.toList());
		model.put("adoptions", adoptions);
		model.put("ownerId", ownerId);
		return "adoptions/adoptionList";
	}
	
	@GetMapping(value="/adoptions/menu")
	public String adoptionMenu(@Valid @ModelAttribute("owner")Owner owner, BindingResult result
			, Map<String, Object> model) {
		model.put("owner", owner);
		return "adoptions/adoptionMenu";
	}
	
	
	@GetMapping(value="/adoptions/{ownerId}/{petId}/new")
	public String initCreationForm(Map<String, Object> model, @PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId) {
		Pet pet = petService.findPetById(petId);
		Adoption adoption = new Adoption();
		adoption.setPet(pet);
		model.put("adoption", adoption);
		model.put("ownerId", ownerId);
		return adoptionForm;
	}
	
	@PostMapping(value="/adoptions/{ownerId}/{petId}/new")
	public String processCreationForm(@Valid Adoption adoption, BindingResult result,@PathVariable("ownerId") int ownerId) {
		if(result.hasErrors()) {
			return adoptionForm;
		}else {
			this.adoptionService.saveAdoption(adoption);
			return "redirect:/adoptions/" + ownerId + "/list";
		}
	}
	
	@GetMapping("/adoptions/{adoptionId}/{ownerId}/delete")
	public String deleteAdoption(@PathVariable("adoptionId") int adoptionId, @PathVariable("ownerId") int ownerId, Map<String, Object> model) {
		this.adoptionService.deleteAdoption(adoptionId);
		return "redirect:/adoptions/" + ownerId + "/list";
	}
	
	
	@GetMapping("/adoptions/{ownerId}/{adoptionId}")
	public String adoptionsOwner(Map<String, Object> model,@PathVariable("ownerId") int ownerId,@PathVariable("adoptionId") int adoptionId) {
		Adoption adoption = this.adoptionService.findById(adoptionId);
		model.put("adoption", adoption);
		model.put("ownerId", ownerId);
		return "adoptions/adoptionDetails";
	}
}
