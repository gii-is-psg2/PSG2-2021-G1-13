package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.CauseRepository;
import org.springframework.samples.petclinic.repository.DonationRepository;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class CauseServiceTests {

	@Mock
	private CauseRepository causeRepo;

	@Mock
	private DonationRepository donationRepo;

	protected CauseService causeService;

	@BeforeEach
    void setup() {
		this.causeService = new CauseService(this.causeRepo,this.donationRepo);
    }

	@Test
	void addingCause() {
		final Cause new_cause = new Cause();
		new_cause.setName("CauseName");
		new_cause.setClosed(false);
		new_cause.setDescription("CauseDescription");
		new_cause.setDonations(new HashSet<Donation>());
		new_cause.setOrganization("CauseOrganization");
		new_cause.setTarget(1000.0);
		new_cause.setId(1);
		final Collection<Cause> sampleCauses = new ArrayList<Cause>();
		sampleCauses.add(new_cause);
        Mockito.when(this.causeRepo.findAll()).thenReturn(sampleCauses);
        Mockito.when(this.causeRepo.findById(1)).thenReturn(Optional.of(new_cause));


		final List<Cause> causes = new ArrayList<>(this.causeService.findAllCauses());
		Cause saved_cause = causes.get(causes.size()-1);
		Assertions.assertEquals("CauseName", saved_cause.getName());
		Assertions.assertEquals(false, saved_cause.getClosed());
		Assertions.assertEquals("CauseDescription",saved_cause.getDescription());
        Assertions.assertEquals("CauseOrganization", saved_cause.getOrganization());
        Assertions.assertEquals(1000.0, saved_cause.getTarget());

		saved_cause = this.causeService.findCauseById(1);
        Assertions.assertEquals("CauseName", saved_cause.getName());
        Assertions.assertEquals(false, (boolean) saved_cause.getClosed());
        Assertions.assertEquals("CauseDescription", saved_cause.getDescription());
        Assertions.assertEquals("CauseOrganization", saved_cause.getOrganization());
        Assertions.assertEquals(1000.0, saved_cause.getTarget());
	}

	@Test
	void addingDonation() {
		final Donation new_donation = new Donation();
		new_donation.setAmount(100.0);
		new_donation.setCause(new Cause());
		new_donation.setClient(new Owner());
		new_donation.setDate(LocalDate.of(2021, 4, 15));
		new_donation.setId(1);
		final List<Donation> sampleDonations = new ArrayList<Donation>();
		sampleDonations.add(new_donation);
        Mockito.when(this.donationRepo.findAll()).thenReturn(sampleDonations);
        Mockito.when(this.donationRepo.findById(1)).thenReturn(Optional.of(new_donation));

        final List<Donation> donations = new ArrayList<>(this.causeService.findAllDonations());
		Donation saved_donation = donations.get(donations.size()-1);
		Assertions.assertTrue(saved_donation.getDate().isEqual(LocalDate.of(2021, 4, 15)));
        Assertions.assertEquals(100.0, saved_donation.getAmount());

		saved_donation = this.causeService.findDonationById(1);
		Assertions.assertTrue(saved_donation.getDate().isEqual(LocalDate.of(2021, 4, 15)));
        Assertions.assertEquals(100.0, saved_donation.getAmount());

	}

}
