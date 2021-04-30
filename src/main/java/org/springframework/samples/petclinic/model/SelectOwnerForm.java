package org.springframework.samples.petclinic.model;

public class SelectOwnerForm {
	
	private Owner owner;

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public SelectOwnerForm(Owner owner) {
		super();
		this.owner = owner;
	}
	
	public SelectOwnerForm() {
		super();
	}
	
	

}
