/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.*;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class VetController {

	private final VetService vetService;
    private static final String VIEWS_VET_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForm";

    @Autowired
	public VetController(VetService clinicService) {
		this.vetService = clinicService;
	}

	@GetMapping(value = { "/vets" })
	public String showVetList(Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		model.put("vets", vets);
		return "vets/vetList";
	}

	@GetMapping(value = { "/vets.xml"})
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		return vets;
	}

    @ModelAttribute("specialties")
    public Collection<Specialty> populatePetTypes() {
        return this.vetService.findSpecialties();
    }

    @InitBinder("vet")
    public void initVetBinder(WebDataBinder dataBinder) {
        dataBinder.addCustomFormatter(new SpecialtyFormatter(vetService),Specialty.class);
    }

    @GetMapping(value = "/vets/new")
    public String initCreationForm(Map<String, Object> model) {
        Vet vet = new Vet();
        model.put("vet", vet);
        return VIEWS_VET_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/vets/new")
    public String processCreationForm(@Valid Vet vet, @RequestParam Optional<String[]> specialties, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_VET_CREATE_OR_UPDATE_FORM;
        }
        else {
            //creating vet
            if (specialties.isPresent()) {
                String[] spec_list = specialties.get();
                for (int i = 0; i < spec_list.length; i++) {
                    String specName = spec_list[i];
                    try {
                        vet.addSpecialty(parseSpec(specName));
                    } catch (ParseException e) {

                    }
                }
            }
            this.vetService.saveVet(vet);

            return "redirect:/vets";
        }
    }

    private Specialty parseSpec(String text) throws ParseException {
        Collection<Specialty> findSpecialties = this.vetService.findSpecialties();
        for (Specialty specialty : findSpecialties) {
            if (specialty.getName().equals(text)) {
                return specialty;
            }
        }
        throw new ParseException("type not found: " + text, 0);
    }

    @GetMapping(value = "/vets/{vetId}/edit")
    public String initUpdateForm(@PathVariable("vetId") int vetId, ModelMap model) {
        Vet vet = this.vetService.findVetById(vetId);
        model.put("vet", vet);
        return VIEWS_VET_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/vets/{vetId}/edit")
    public String processCreationForm(@Valid Vet vet, @PathVariable int vetId, @RequestParam Optional<String[]> specialties, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_VET_CREATE_OR_UPDATE_FORM;
        }

        else {
            //creating vet
            Set<Specialty> specialtySet = new HashSet<>();
            if (specialties.isPresent()) {
                String[] spec_list = specialties.get();
                for (int i = 0; i < spec_list.length; i++) {
                    String specName = spec_list[i];
                    try {
                        specialtySet.add(parseSpec(specName));
                    } catch (ParseException e) {

                    }
                }
            }
            Vet vetToUpdate = vetService.findVetById(vetId);
            BeanUtils.copyProperties(vet,vetToUpdate,"id");
            vetToUpdate.setSpecialtiesInternal(specialtySet);
            this.vetService.saveVet(vetToUpdate);

            return "redirect:/vets";
        }
    }

}
