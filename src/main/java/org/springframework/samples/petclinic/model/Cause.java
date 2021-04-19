package org.springframework.samples.petclinic.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="causes", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class Cause extends NamedEntity{

	@NotBlank
	private String description;

	@NotNull
	private Double target;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cause")
	private Set<Donation> donations;

	@NotBlank
	private String organization;

	@NotNull
	private Boolean closed;

}
