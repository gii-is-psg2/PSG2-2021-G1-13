package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.repository.NoRepositoryBean;

@Entity
@Table(name="adoption_applications")
public class AdoptionApplication extends BaseEntity{
	
	@Column(name="description")
	@NotBlank
	private String description;
	
	@Column(name="approved")
	@NotNull
	private Boolean approved;
	
	@ManyToOne
	@JoinColumn(name="adoption")
	private Adoption adoption;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="owner")
	private Owner owner;

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Adoption getAdoption() {
		return adoption;
	}

	public void setAdoption(Adoption adoption) {
		this.adoption = adoption;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}
	
	
	
}
