package org.springframework.samples.petclinic.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="hotelreservations")
public class HotelReservation extends BaseEntity{

	@NotNull
	@DateTimeFormat(pattern= "yyyy/MM/dd")
    private LocalDate finish;

	@NotNull
	@DateTimeFormat(pattern= "yyyy/MM/dd")
    private LocalDate start;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;

}
