package org.springframework.samples.petclinic.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "adoptions")
public class Adoption extends BaseEntity{
	
	@Column(name = "description")
	@NotEmpty
	private String description;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="pet", unique= true)
	private Pet pet;

	@OneToMany(mappedBy = "adoption",cascade=CascadeType.ALL)
	private Set<AdoptionApplication> adoptionApplications;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public Set<AdoptionApplication> getAdoptionApplications() {
		return adoptionApplications;
	}

	public void setAdoptionApplications(Set<AdoptionApplication> adoptionApplications) {
		this.adoptionApplications = adoptionApplications;
	}

	protected Set<AdoptionApplication> getAdoptionApplicationInternal() {
		if (this.adoptionApplications == null) {
			this.adoptionApplications = new HashSet<>();
		}
		return this.adoptionApplications;
	}
	
	public void addAdoptionApplication(AdoptionApplication adoptionApplication) {
		getAdoptionApplicationInternal().add(adoptionApplication);
		adoptionApplication.setAdoption(this);
	}
	
	public boolean removeAdoptionApplication(AdoptionApplication adoptionApplication) {
		return getAdoptionApplicationInternal().remove(adoptionApplication);
	}
	
	
	

}
