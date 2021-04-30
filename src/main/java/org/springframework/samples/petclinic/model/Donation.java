package org.springframework.samples.petclinic.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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
