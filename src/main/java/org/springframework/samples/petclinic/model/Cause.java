package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="causes", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class Cause extends NamedEntity{
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String description;
	
	@NotNull
	private Double target;
	
	//@OneToMany(cascade = CascadeType.ALL, mappedBy = "donation")
	//private Set<Donation> donations;
	
	@NotBlank
	private String organization;
	
	@NotNull
	private Boolean closed;

}
