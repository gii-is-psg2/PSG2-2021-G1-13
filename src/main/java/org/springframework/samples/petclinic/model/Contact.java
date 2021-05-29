package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="contact")
public class Contact  extends BaseEntity{

	@NotBlank
	private String name;

	@NotBlank
	private String email;

	@NotBlank
	private String phone;
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public void setEmail(final String email) {
		this.email = email;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public String getName() {
		return this.name;
	}
	
	public String getEmail() {
		return this.email;
	}

	public String getPhone() {
		return this.phone;
	}
	
	public Contact(@NotBlank final String name, @NotBlank final String email, @NotBlank final String phone) {
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
	}
}