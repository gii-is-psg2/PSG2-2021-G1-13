package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="donations")
public class Donation extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "cause_id")
    private Cause cause;

	@Column(name="donation_date")
	@DateTimeFormat(pattern= "yyyy/MM/dd")
	private LocalDate date;

	@NotNull
	@Column(name="amount")
	private Double amount;
	
	@NotNull
	@ManyToOne
	private Owner client;
}