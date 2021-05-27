package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.service.AdoptionApplicationService;
import org.springframework.samples.petclinic.service.AdoptionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AdoptionController {
    public static final String OWNER_ID = "ownerId";
    private final AdoptionService adoptionService;
    private final AdoptionApplicationService adoptionApplicationService;
	private final PetService petService;
	private final OwnerService ownerService;


	private static final String adoptionForm = "adoptions/formAdoption";

	@Autowired
	public AdoptionController(final AdoptionService adoptionService, final PetService petService, final OwnerService ownerService, final AdoptionApplicationService adoptionApplicationService) {
		this.adoptionService = adoptionService;
		this.adoptionApplicationService = adoptionApplicationService;
		this.petService = petService;
		this.ownerService = ownerService;
	}

	@GetMapping("/adoptions")
	public String selectOwner(final ModelMap model) {
		Collection<Owner> results = this.ownerService.findOwnerByLastName("");
		final String username = UserUtils.getUser();
        final User user = this.ownerService.getUser(username);
        for(final Authorities auth: user.getAuthorities()) {
        	if(auth.getAuthority().equals("owner")) {
        		results = this.ownerService.findOwnersByUsername(username);
        	}
        }
		model.put("ownerList", results);
		model.put("selectOwnerForm", new SelectOwnerForm());
		return "adoptions/selectOwner";
	}

	@GetMapping("/adoptions/{ownerId}")
	public String adoptionsOwner(final Map<String, Object> model,@PathVariable(OWNER_ID) final int ownerId) {
		final List<Adoption> adoptions = this.adoptionService.findByOwnerId(ownerId).stream().collect(Collectors.toList());
		model.put("adoptions", adoptions);
		model.put("numPeticiones", adoptionApplicationService.numAdoptionAppNoAceptadas(ownerId));
		return "adoptions/adoptionsOwner";
	}
	
	@PostMapping("/adoptions")
	public String adoptionsMessageOwner(@Valid @ModelAttribute("owner") final Owner owner, final BindingResult result, final Map<String, Object> model) {
		return "redirect:/adoptions/"+owner.getId();
	}

	@GetMapping("/adoptions/{ownerId}/list")
	public String adoptionsList(final Map<String, Object> model,@PathVariable(OWNER_ID) final int ownerId) {
		final List<Adoption> adoptions = this.adoptionService.findAdoptions(ownerId).stream().collect(Collectors.toList());
		model.put("adoptions", adoptions);
		model.put(OWNER_ID, ownerId);
		model.put("numPeticiones", adoptionApplicationService.numAdoptionAppNoAceptadas(ownerId));
		return "adoptions/adoptionList";
	}

	@GetMapping(value="/adoptions/menu")
	public String adoptionMenu(@Valid @ModelAttribute("owner") final Owner owner, final BindingResult result
			, final Map<String, Object> model) {
		model.put(OWNER_ID, owner.getId());
		model.put("numPeticiones", adoptionApplicationService.numAdoptionAppNoAceptadas(owner.getId()));
		return "adoptions/adoptionMenu";
	}


	@GetMapping(value="/adoptions/{ownerId}/{petId}/new")
	public String initCreationForm(final Map<String, Object> model, @PathVariable(OWNER_ID) final int ownerId, @PathVariable("petId") final int petId) {
		final Pet pet = this.petService.findPetById(petId);
		final Adoption adoption = new Adoption();
		adoption.setPet(pet);
		model.put("adoption", adoption);
		model.put(OWNER_ID, ownerId);
		model.put("numPeticiones", adoptionApplicationService.numAdoptionAppNoAceptadas(ownerId));
		return this.adoptionForm;
	}

	@PostMapping(value="/adoptions/{ownerId}/{petId}/new")
	public String processCreationForm(@Valid final Adoption adoption, final BindingResult result, @PathVariable(OWNER_ID) final int ownerId, final ModelMap model) {
		try {
			if(result.hasErrors()) {
				return this.adoptionForm;
			}else {
				this.adoptionService.saveAdoption(adoption);
				model.put("message", "La adopción se ha registrado correctamente.");
				return this.adoptionsList(model, ownerId);
			}
		}catch (final Exception e) {
			model.put("adoption", adoption);
			model.put(OWNER_ID, ownerId);
			model.put("error", "duplicatedAdoption");
			model.put("numPeticiones", adoptionApplicationService.numAdoptionAppNoAceptadas(ownerId));
			return this.adoptionForm;
		}
	}

	@GetMapping("/adoptions/{adoptionId}/{ownerId}/delete")
	public String deleteAdoption(@PathVariable("adoptionId") final int adoptionId, @PathVariable(OWNER_ID) final int ownerId, final Map<String, Object> model) {
		final Adoption adoption = this.adoptionService.findById(adoptionId);
		this.adoptionService.deleteAdoption(adoption);
		model.put("message", "La adopción se ha eliminado correctamente.");
		return this.adoptionsOwner(model, ownerId);
	}


	@GetMapping("/adoptions/details/{adoptionId}")
	public String adoptionsDetails(final Map<String, Object> model, @PathVariable("adoptionId") final int adoptionId) {
		final Adoption adoption = this.adoptionService.findById(adoptionId);
		model.put("pet", adoption.getPet());
		return "adoptions/adoptionDetails";
	}
}
