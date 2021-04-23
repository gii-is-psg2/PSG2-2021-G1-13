package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.Cause;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CauseValidator implements Validator {
	
	private static final String REQUIRED = "required";

	@Override
	public void validate(final Object obj, final Errors errors) {
		final Cause cause = (Cause) obj;
		final String name = cause.getName();
		final String description = cause.getDescription();
		final Double target = cause.getTarget();
		final String organization = cause.getOrganization();
		
		// name validation
		if (name == null) {
			errors.rejectValue("name", CauseValidator.REQUIRED, CauseValidator.REQUIRED);
		}
		// description validation
		if (description == null) {
			errors.rejectValue("description", CauseValidator.REQUIRED, CauseValidator.REQUIRED);
		}
		// target validation
		if (target == null) {
			errors.rejectValue("target", CauseValidator.REQUIRED, CauseValidator.REQUIRED);
		}else if(target<=0) {
			errors.rejectValue("target", "invalid_target", "<fmt:message key=\"invalid_target\"/>");
		}
		// organization validation
		if (organization == null) {
			errors.rejectValue("organization", CauseValidator.REQUIRED, CauseValidator.REQUIRED);
		}
	}

	/**
	 * This Validator validates *just* Pet instances
	 */
	@Override
	public boolean supports(final Class<?> clazz) {
		return Cause.class.isAssignableFrom(clazz);
	}
	

}
