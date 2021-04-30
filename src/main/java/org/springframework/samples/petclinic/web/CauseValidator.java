package org.springframework.samples.petclinic.web;

import java.util.Set;

import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
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
		if (name == null || name.trim().equals("")) {
			errors.rejectValue("name", CauseValidator.REQUIRED, CauseValidator.REQUIRED);
		}
		// description validation
		if (description == null || description.trim().equals("")) {
			errors.rejectValue("description", CauseValidator.REQUIRED, CauseValidator.REQUIRED);
		}
		// target validation
		if (target == null) {
			errors.rejectValue("target", CauseValidator.REQUIRED, CauseValidator.REQUIRED);
		}else if(target<=0) {
			errors.rejectValue("target", "invalid_target", "<fmt:message key=\"invalid_target\"/>");
		}
		// organization validation
		if (organization == null || organization.trim().equals("")) {
			errors.rejectValue("organization", CauseValidator.REQUIRED, CauseValidator.REQUIRED);
		}
	}
	
	public void validateDonation(final Donation donation, final BindingResult result) {
		final Owner owner = donation.getClient();
		final Double amount = donation.getAmount();
		final Double target = donation.getCause().getTarget();
		Double collected = 0.0;
		if (amount != null) {
			final Set<Donation> oldDonations = donation.getCause().getDonations();
			for(final Donation d: oldDonations) {
				collected += d.getAmount();
			}
		}
		
		
		// owner validation
		if (owner == null) {
			result.addError(new FieldError("donation", "owner", CauseValidator.REQUIRED));
		}
		// amount validation
		if (amount == null) {
			result.addError(new FieldError("donation", "amount", CauseValidator.REQUIRED));
		}else if (amount <= 0) {
			result.addError(new FieldError("donation", "amount", "Must be positive"));
		}else if (amount+collected > target) {
			final Double remaining = target - collected;
			result.addError(new FieldError("donation", "amount", "Maximum donation for this cause is: " + remaining));
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
