package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/causes")
public class CauseController {
	
	@Autowired
	private CauseService causeService;
	
	@Autowired
	private CauseValidator causeValidator;
	
	@InitBinder("cause")
	public void initCauseBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(this.causeValidator);
	}
	
	@GetMapping()
	public String causesList(final ModelMap modelMap) {
		CauseController.log.info("Loading list of causes view");
		final String view= "causes/causesList";
		final Iterable<Cause> causes=this.causeService.findAllCauses();
		modelMap.addAttribute("causes", causes);
		return view;
	}
	
	@GetMapping(path="/new")
	public String createCause(final ModelMap modelMap) {
		CauseController.log.info("Loading new cause form");
		final String view="causes/addCause";
		modelMap.addAttribute("cause", new Cause());
		return view;
	}
	
	@PostMapping(path="/save")
	public String saveCause(@Valid final Cause cause, final BindingResult result, final ModelMap modelMap) {
		CauseController.log.info("Saving cause: " + cause.getId());
		String view="causes/causesList";
		if(result.hasErrors()) {
			CauseController.log.warn("Found errors on insertion: " + result.getAllErrors());
			modelMap.addAttribute("cause", cause);
			return "causes/addCause";
		}else {
			CauseController.log.info("Cause validated: saving into DB");
			this.causeService.saveCause(cause);
			modelMap.addAttribute("message", "Cause successfully saved!");
			view=this.causesList(modelMap);
		}
		return view;
	}
	
	@GetMapping(path="/delete/{causeId}")
	public String deleteCause(@PathVariable("causeId") final int causeId, final ModelMap modelMap) {
		CauseController.log.info("Deleting cause: " + causeId);
		String view="causes/causesList";
		final Cause cause = this.causeService.findCauseById(causeId);
		if(cause!=null) {
			CauseController.log.info("Cause found: deleting");
			this.causeService.deleteCauseById(cause.getId());
			modelMap.addAttribute("message", "Cause successfully deleted!");
			view=this.causesList(modelMap);			
		}else {
			CauseController.log.warn("Cause not found in DB: " + causeId);
			modelMap.addAttribute("message", "Cause not found!");
			view=this.causesList(modelMap);
		}
		return view;
	}
	
	@GetMapping(value = "/{causeId}/edit")
	public String initUpdateCauseForm(@PathVariable("causeId") final int causeId, final ModelMap model) {
		CauseController.log.info("Loading update cause form");
		final Cause cause = this.causeService.findCauseById(causeId);	
		model.put("cause", cause);
		return "causes/updateCause";
	}

	@PostMapping(value = "/{causeId}/edit")
	public String processUpdateCauseForm(@Valid final Cause cause, final BindingResult result,
			@PathVariable("causeId") final int causeId, final ModelMap model) {
		cause.setId(causeId);
		CauseController.log.info("Updating cause: " + causeId);
		if (result.hasErrors()) {
			CauseController.log.warn("Found errors on update: " + result.getAllErrors());
			model.put("cause", cause);
			return "causes/updateCause";
		}
		else {
			CauseController.log.info("Cause validated: updating into DB");
			this.causeService.saveCause(cause);
			return "redirect:/causes";
		}
	}

}
